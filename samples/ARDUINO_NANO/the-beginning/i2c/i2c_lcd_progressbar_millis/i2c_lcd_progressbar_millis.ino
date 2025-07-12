#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD I2C address (usually 0x27 or 0x3F)
LiquidCrystal_I2C lcd(39, 16, 2);  // (address, columns, rows)

long instructions_micro = 0;
long total_micro = 0;
long _next_schedule_ms = 0;
int  _index = 0;

void setup() 
{
  lcd.init();
  lcd.backlight();
  lcd.clear();

  Wire.begin();
  Wire.setClock(400000); // Set I2C clock to 400 kHz  

  total_micro = micros();
  _next_schedule_ms = millis();
}


void loop() 
{
  if (_next_schedule_ms <= millis()) {
    long i_micro = micros();
    _next_schedule_ms += 250;

    lcd.setCursor(0, 0);  // column 0, row 0
    lcd.print("[");
    for (int p=1; p <= _index; p++) {
        lcd.setCursor(p, 0);  // column 0, row 0
        lcd.print((char)0xFF);
    }
    for (int p=_index+1; p <= 14; p++) {
        lcd.setCursor(p, 0);  // column 0, row 0
        lcd.print("_");
    }
    lcd.setCursor(15, 0);  // column 0, row 0
    lcd.print("]");

    _index++;
    instructions_micro += micros() - i_micro;
  }

  if (_index == 15) {
    total_micro = micros() - total_micro;

    delay(250);
    lcd.clear();
    lcd.setCursor(0, 0);  // column 0, row 0
    lcd.print("I_ms: ");
    lcd.print(instructions_micro);
    lcd.setCursor(0, 1);  // column 0, row 1
    lcd.print("T_ms: ");
    lcd.print(total_micro);

    delay(10000);

    instructions_micro = 0;
    total_micro = micros();

    _next_schedule_ms = millis();
    _index = 0;
  }
}

