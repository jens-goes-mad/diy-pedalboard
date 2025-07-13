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
