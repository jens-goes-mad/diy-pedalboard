---
title:  I²C and task manager
layout: article
draft: false
summary: "
Arduino and I²C + LCD displays using a task manager   
"
weight: 206
image: "PB_I2C_LCD_taskmanager.jpg"
tags: ["hardware", "microcontroller", "I2C", "Arduino", "Tasks"]
categories: ["arduino"]
---
## Goodbye Spaghetti, Hello Task Manager!
Now we enter the final refactoring round.

Since we’ve already moved our logic into classes — and classes inherently share behavior — we can now introduce a 
Task Manager ([GitHub](https://github.com/jens-goes-mad/diy-pedalboard/tree/master/samples/ARDUINO_NANO/the-beginning/tasks/JB_LCD_Task5_manager/JB_LCD_Task5_manager.ino))
that oversees and coordinates the execution of individual tasks.

In short: clarity and flexibility are improving, but we’re entering territory where elegance meets trade-offs.

## The Source

### Adding includes and global init

    #include <Wire.h>
    #include <LiquidCrystal_I2C.h>
    
    LiquidCrystal_I2C g_lcd(39, 16, 2);  // (address, columns, rows)
    unsigned long g_loopCounter = 0;

### Task Base class 


    class Task
    {
    private:
      unsigned long _nextTime_ms;
      unsigned long _interval_ms;
    
    public:
      Task(unsigned long interval_ms);
      virtual void update(unsigned long currentTime_ms);
      virtual void process() = 0;
    };

- The base class holds essential data like the interval and nextTime, which are used to schedule task execution.
- The update(...) method is marked as virtual, allowing derived classes to override it — a core principle of polymorphism.
- The process() method is defined as = 0, making it pure virtual. This means the base class is abstract and cannot 
  be instantiated on its own — any subclass must provide its own implementation of process().

### Task Base implementation

    Task::Task(unsigned long interval_ms)
    {  
      _interval_ms = interval_ms;
      _nextTime_ms = millis() + interval_ms;
    }
    
    void Task::update(unsigned long currentTime_ms)
    {
      // https://playground.arduino.cc/Code/TimingRollover/
      if (static_cast<signed>(currentTime_ms - _nextTime_ms) >= 0) {
        process();
        _nextTime_ms = currentTime_ms + _interval_ms - (currentTime_ms - _nextTime_ms);
      }
    }

- The update() method handles the timing logic. It checks whether the current time has reached the scheduled point.
  If so, it triggers process(), and then recalculates the next scheduled run.
- The following line recalculates the next scheduled time, but there's an important caveat:
  In a microcontroller environment, the millis() counter is based on an unsigned 32-bit value. 
  That means it can hold only a limited number of milliseconds — roughly 49 days at 16 [MHz| before it overflows and 
  wraps around to zero. This is called an overrun, and if you're not prepared for it, it can completely mess up your timing logic.
  To stay safe, always use subtraction to compare time values instead of relying on millis() >= nextTime.
- `_nextTime_ms = currentTime_ms + _interval_ms - (currentTime_ms - _nextTime_ms)`
- this line does not just add `_interval_ms` to the current time. Instead, it accounts for the time already elapsed 
  since the last scheduled time — even if the system was delayed or busy.
- `(currentTime_ms - _nextTime_ms)` gives the lateness — how far past the intended time we are.
  Subtracting this from the new target adjusts the next deadline back onto the intended schedule.
  This technique keeps your periodic tasks from drifting over time, even with delays or millis() overflows.
  It's a rollover-safe and jitter-minimizing scheduling pattern! Not a real issue for my `DIY midi music workstation pedal board`
  but in real-time critical systems — like pacemakers, automotive ECUs, or industrial control — timing drift or 
  rollover bugs could become life-threatening.

### LCDChar task

    class LCDChar : public Task
    {
    private:
      bool _toggle = false;
      char _c1, _c2, _colPos;
    
    public:
      LCDChar(char c1, char c2, char colPos, unsigned long interval);
      void process();
    };


    LCDChar::LCDChar(char c1, char c2, char colPos, unsigned long interval)
      : Task(interval)
    {
      _c1 = c1;
      _c2 = c2;
      _colPos = colPos;
    }
    
    void LCDChar::process()
    {
      _toggle = !_toggle;
      g_lcd.setCursor(_colPos, 1);
      g_lcd.print(_toggle ? _c1 : _c2);
    }

- Compared to our initial version, the timing logic is now encapsulated in a base class.
  Individual tasks no longer need to handle any if or scheduling checks — it’s all taken care of.
- Each task simply implements its own `process()` method, which now holds just the business logic.
  That’s all your class needs to focus on.
- `g_lcd` is currently a global reference to the I²C-connected LCD.
  For now, that’s fine — but once we start handling multiple displays in parallel, we’ll likely pass the display 
  instance as a constructor parameter to avoid tight coupling and enable reuse.

### LCDStatistics task

    class LCDStatistic : public Task
    {
    public:
      LCDStatistic(unsigned long interval);
      void process();
    };
    
    LCDStatistic::LCDStatistic(unsigned long interval)
      : Task(interval)
    {
    }
    
    void LCDStatistic::process()
    {
      g_lcd.setCursor(0, 0);
      g_lcd.print("Loops:          ");
      g_lcd.setCursor(7, 0);
      g_lcd.print(g_loopCounter);
      g_loopCounter = 0;
    }

- Previously, the loop counter was updated and displayed every 10 seconds.
  That behavior fits perfectly into a dedicated task as well — making it another ideal candidate for encapsulation.


### Task manager

    class TaskManager
    {
    private:
      Task* _pTasks[10] = { NULL };
      int _elements = 0;
    
    public:
      TaskManager();
      unsigned long update();
    
      void add(Task* pTask);
    };


    TaskManager::TaskManager()
    {
    }
    
    void TaskManager::add(Task* pTask)
    {
      _pTasks[_elements++] = pTask;
    }
    
    unsigned long TaskManager::update()
    {
      unsigned long current_ms = millis();
      for (int i = 0; i < _elements; i++) {
        _pTasks[i]->update(current_ms);
      }
      return current_ms;
    }

- This simple class skips memory management and supports up to 10 tasks — without bounds checking (use responsibly!).
- You can register tasks using `add(Task* pTask)`.
- The `update()` method iterates over all registered tasks and delegates timing checks and execution to each task.

### Program setup

    TaskManager _taskManager;
    
    void setup()
    {
      g_lcd.init();
      g_lcd.backlight();
      g_lcd.clear();
      
      Wire.begin();
      Wire.setClock(400000); // Set I2C clock to 400 kHz
      
      // ----
      
      _taskManager.add(new LCDChar('A', 'a', 1, 1000));
      _taskManager.add(new LCDChar('B', 'b', 2, 2000));
      _taskManager.add(new LCDChar('C', 'c', 3, 3000));
      _taskManager.add(new LCDStatistic(10000));
    }

- The first part of the `setup()` method mirrors earlier examples:
  it initializes the I²C library and boosts the bus speed for better performance.
- The second part instantiates four tasks and registers each one with the taskManager using `add()`.

### Program (game) loop

    void loop()
    {
      _taskManager.update();
      g_loopCounter++;
    }

- And just like that, our entire main loop collapses to two elegant lines — clean, efficient, and easy to maintain!

## When the Conductor Joins the Orchestra
By deriving TaskManager from Task, you turn the conductor into a player-manager — a special kind of task that also manages others. 
This introduces recursive scheduling with surprisingly elegant benefits:
- Hierarchical control: You can group tasks into logical units (e.g., one TaskManager for UI-related tasks, another for MIDI processing).
- Composability: A TaskManager is now just another task — you can nest managers as deeply as needed.
- Reuse & Encapsulation: Common behavior (e.g., startup delays, group-wide pause/resume) can now be abstracted at the manager level.
- Modularity: Swap out entire task groups without rewriting your main loop.

Think of a department head (task manager) who is also an engineer (task). They manage their team but also have work of their own. 
Now imagine each team leader (task and task manager at the same time) has their own sub-teams. Boom — hierarchy without chaos.
