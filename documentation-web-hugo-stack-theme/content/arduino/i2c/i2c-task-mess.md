---
title: I²C and tasks"
layout: article
draft: false
summary: "
Arduino and I²C + LCD displays using tasks  
"
weight: 205
image: "PB_I2C_LCD_task_mess.jpg"
tags: ["hardware", "microcontroller", "I2C", "Arduino", "Tasks"]
categories: ["arduino"]
---
##  Goodbye delay(), Hello Tasks!

### Starting procedural

In [Goodbye delay(), Hello millis()](/arduino/i2c/i2c-no-delay#goodbye-delay-hello-millis) I spread the idea of using time
based task execution instead of `delay()`. Now to demonstrate the disadvantages of global variables and `if else task`
handling, even in simple programs consider the following sample. 

It uses three tasks to display characters on the LCD, toggling between lower and uppercase for a given schedule.
- Task 1, scheduled to 1 [sec] displays `a` or `A` 
- Task 2, scheduled to 2 [sec] displays `b` or `B` 
- Task 3, scheduled to 3 [sec] displays `c` or `C` 

---

    #include <Wire.h>
    #include <LiquidCrystal_I2C.h>
    
    // Set the LCD I2C address (usually 0x27 or 0x3F)
    LiquidCrystal_I2C g_lcd(39, 16, 2);  // (address, columns, rows)
    
    const unsigned int OFFSET_ms = 10 * 1000;
    unsigned long g_endTime_ms;
    unsigned long g_loopCounter = 0;
    
    bool g_task_1_on = false;
    const unsigned int TASK_1_INTERVAL_ms = 1000;
    unsigned long g_task_1_nextTime_ms;
    
    bool g_task_2_on = false;
    const unsigned int TASK_2_INTERVAL_ms = 2000;
    unsigned long g_task_2_nextTime_ms;
    
    bool g_task_3_on = false;
    const unsigned int TASK_3_INTERVAL_ms = 3000;
    unsigned long g_task_3_nextTime_ms;

    // ----------

    void setup()
    {
        g_lcd.init();
        g_lcd.backlight();
        g_lcd.clear();
        
        Wire.begin();
        Wire.setClock(400000); // Set I2C clock to 400 kHz
        
        unsigned long current_ms = millis();
        g_endTime_ms = current_ms + OFFSET_ms;
        
        g_task_1_nextTime_ms = current_ms + TASK_1_INTERVAL_ms;
        g_task_2_nextTime_ms = current_ms + TASK_2_INTERVAL_ms;
        g_task_3_nextTime_ms = current_ms + TASK_3_INTERVAL_ms;
    }
    
    void loop()
    {
        g_loopCounter++;
        unsigned long current_ms = millis();
    
        if (current_ms >= g_task_1_nextTime_ms) {
            g_task_1_on = !g_task_1_on;
            g_lcd.setCursor(1, 1);
            g_lcd.print(g_task_1_on ? "A" : "a");
            g_task_1_nextTime_ms = current_ms + TASK_1_INTERVAL_ms;
        }
    
        if (current_ms > g_task_2_nextTime_ms) {
            g_task_2_on = !g_task_2_on;
            g_lcd.setCursor(2, 1);
            g_lcd.print(g_task_2_on ? "B" : "b");
            g_task_2_nextTime_ms = current_ms + TASK_2_INTERVAL_ms;
        }
    
        if (current_ms > g_task_3_nextTime_ms) {
            g_task_3_on = !g_task_3_on;
            g_lcd.setCursor(3, 1);
            g_lcd.print(g_task_3_on ? "C" : "c");
            g_task_3_nextTime_ms = current_ms + TASK_3_INTERVAL_ms;
        }
    
        // ----
        
        if (current_ms >= g_endTime_ms) {
            outputResults();
            g_loopCounter = 0;
            g_endTime_ms = millis() + OFFSET_ms;
        }
    }
    
    void outputResults()
    {
        g_lcd.setCursor(0, 0);
        g_lcd.print("Loops:          ");
        g_lcd.setCursor(7, 0);
        g_lcd.print(g_loopCounter);
    }

Even without any understanding of software development, you will visually detect repeating blocks. Basically each block
consists of state end execution code.

    ...
    bool g_task_1_on = false;
    const unsigned int TASK_1_INTERVAL_ms = 1000;
    unsigned long g_task_1_nextTime_ms;
    ...
    if (current_ms >= g_task_1_nextTime_ms) {
        g_task_1_on = !g_task_1_on;
        g_lcd.setCursor(1, 1);
        g_lcd.print(g_task_1_on ? "A" : "a");
        g_task_1_nextTime_ms = current_ms + TASK_1_INTERVAL_ms;
    }
    ...

And this is exactly what the nature of a task describes. And this is exactly a candidate for a class to hide all nesty
details behind a common interface.

### Starting with classes

<span class="chatgpt">Please give a short summary: what is a class in object oriented software development</span>
<div class="chatgpt-response">

> A class is a blueprint or template used to define objects in object-oriented programming (OOP). 
> It describes what an object will contain and what it can do — that is, it defines its attributes (properties or data) and methods (functions or behaviors).
> - When you create an object, you are creating an instance of a class
> - The class specifies the structure and capabilities, while each object created from it can hold its own specific data
> - Classes help organize and reuse code
> - Enable encapsulation, meaning data and behavior are bundled together
> - Support inheritance, allowing new classes to be based on existing ones
> - Promote polymorphism, letting different classes implement shared interfaces in different ways"
  
</div>

#### Abstract classes

First, we start by creating a simple task class, which handles the generic parts of a task — like the schedule 
and processing logic. This is an abstract class, which means it defines a virtual function: (`virtual void process() = 0`).
Because of this, the class can’t be instantiated directly. Instead, any derived class is forced to implement its own 
version of process(), while it can completely ignore all the scheduling boilerplate.

Previously global variables like  `TASK_1_INTERVAL_ms = 1000` and `g_task_1_nextTime_ms` now become class members, 
no longer polluting the global space. These are fully encapsulated and accessible only through each instance of the class. 

    class Task
    {
        private:
        unsigned long _nextTime_ms;
        unsigned long _interval_ms;
        
        public:
        Task(unsigned long interval_ms);
        void update(unsigned long currentTime_ms);
        
        virtual void process() = 0;
    };
    
    Task::Task(unsigned long interval_ms)
    {
        _interval_ms = interval_ms;
        _nextTime_ms = millis() + interval_ms;
    }
    
    void Task::update(unsigned long currentTime_ms)
    {
        if (currentTime_ms >= _nextTime_ms) {
            process();
            _nextTime_ms = currentTime_ms + _interval_ms - (currentTime_ms - _nextTime_ms);
        }
    }

#### Concrete classes

Concrete classes add the actual implementation details to these abstract classes or interfaces, 
making them usable at runtime.  In our example, we want to display three characters, each with a different schedule.
We can achieve this by creating three instances of the same concrete task class, each configured with its own 
interval and character.

    class LcdChar : public Task
    {
    private:
        bool _toggle = false;
        char _c1, _c2, _colPos;
        
    public:
        LcdChar(char c1, char c2, char colPos, unsigned long interval);
        void process();
    };
    
    LcdChar::LcdChar(char c1, char c2, char colPos, unsigned long interval)
        : Task(interval)
    {
        _c1 = c1;
        _c2 = c2;
        _colPos = colPos;
    }
    
    void LcdChar::process()
    {
        _toggle = !_toggle;
        g_lcd.setCursor(_colPos, 1);
        g_lcd.print(_toggle ? _c1 : _c2);
    }

The `process()` method now encapsulates what used to be handled by `if` blocks in our original example.
Class members replace global variables, further improving encapsulation and reducing potential bugs.
The result? Our main program becomes surprisingly simple:
- We just create three task instances.
- We pass each its interval and characters to display.
- And finally, we call them in the loop.

### Finally

Now, the code looks like this:

    ...

    void setup()
    {
        ...

        unsigned long current_ms = millis();
        g_endTime_ms =  + OFFSET_ms;

        Task* _task1 = new LcdChar('A', 'a', 1, 1000);
        Task* _task2 = new LcdChar('B', 'b', 2, 2000);
        Task* _task3 = new LcdChar('C', 'c', 3, 3000);
    }
    
    void loop()
    {
        g_loopCounter++;
        unsigned long current_ms = millis();
        
        _task1->update(current_ms);
        _task2->update(current_ms);
        _task3->update(current_ms);
    
        ...        
    }


The loop has now shortened dramatically — no more globals, full reuse of abstract and concrete implementations.
Compare it yourself and see the difference (full source on
[GitHub](https://github.com/jens-goes-mad/diy-pedalboard/tree/master/samples/ARDUINO_NANO/the-beginning/tasks/JB_LCD_Task2/JB_LCD_Task2.ino))!

    #include <Wire.h>
    #include <LiquidCrystal_I2C.h>
    
    LiquidCrystal_I2C g_lcd(39, 16, 2);  // (address, columns, rows)
    
    const unsigned int OFFSET_ms = 10 * 1000;
    unsigned long g_endTime_ms;
    unsigned long g_loopCounter = 0;
    
    // ---- CLASSES OMITED FOR CLARITY
    
    Task* _task1 = new LcdChar('A', 'a', 1, 1000);
    Task* _task2 = new LcdChar('B', 'b', 2, 2000);
    Task* _task3 = new LcdChar('C', 'c', 3, 3000);
    
    // ----------
    
    void setup()
    {
        g_lcd.init();
        g_lcd.backlight();
        g_lcd.clear();
        
        Wire.begin();
        Wire.setClock(400000); // Set I2C clock to 400 kHz
        
        unsigned long current_ms = millis();
        g_endTime_ms =  + OFFSET_ms;
    }
    
    void loop()
    {
        g_loopCounter++;
        unsigned long current_ms = millis();
        
        // ----
        
        _task1->update(current_ms);
        _task2->update(current_ms);
        _task3->update(current_ms);
        
        // ----
        
        if (current_ms >= g_endTime_ms) {
            outputResults();
        
            g_loopCounter = 0;
            g_endTime_ms = millis() + OFFSET_ms;
        }
    }
    
    void outputResults()
    {
        ...
    }

Unfortunately, we have to mention that clarity often comes at the cost of performance.
In this case, the class-based version completed 1,344,881 loops, while the naive "spaghetti code" version 
reached 1,706,220 loops. This means using classes introduced about 20% overhead, something we definitely need 
to investigate further.

### Overhead

Why the Overhead?
The 20% performance overhead in the class-based version likely comes from a few sources:

- Virtual function calls: Using virtual void process() introduces additional indirection.
- The CPU needs to look up the function in the virtual table (vtable) before jumping to it.
- Increased memory access: Class instances involve more pointer dereferencing and member variable access compared to direct global variables.
- Extra abstraction logic: Encapsulation layers, like constructors and state tracking within the class, add small runtime costs.

While this overhead is real, it’s often acceptable — especially when the benefits in code clarity, maintainability, 
and reusability outweigh the raw performance loss. But in resource-constrained environments (like microcontrollers), 
it’s always worth measuring and considering these trade-offs carefully.

### Result

{{< video src="I2C-LCD-tasks.mp4" type="video/mp4" autoplay="false" loop="false" >}}

Next we move from tasks to one [Taskmanager](/arduino/i2c/i2c-task-manager)
