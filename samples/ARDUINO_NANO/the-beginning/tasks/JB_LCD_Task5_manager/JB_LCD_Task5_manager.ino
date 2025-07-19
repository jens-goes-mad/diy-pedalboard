// loops 607197 in 10 [sec]

#include <Wire.h>
#include <LiquidCrystal_I2C.h>

LiquidCrystal_I2C g_lcd(39, 16, 2);  // (address, columns, rows)
unsigned long g_loopCounter = 0;

// ----------

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

// ----------

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

// ----

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
  // int numberOfElements = sizeof(_pTasks) / sizeof(Task<DATATYPE>*);
  for (int i = 0; i < _elements; i++) {
    _pTasks[i]->update(current_ms);
  }
  return current_ms;
}

// ----------

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

void loop() 
{
  _taskManager.update();
  g_loopCounter++;
}
