#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD I2C address (usually 0x27 or 0x3F)
LiquidCrystal_I2C lcd(39, 16, 2);  // (address, columns, rows)

void setup() 
{
  lcd.init();
  lcd.backlight();
}

long instructions_micro = 0;
long total_micro = 0;

void loop() 
{
  instructions_micro = 0;
  total_micro = micros();

  lcd.clear();
  for (int i=1; i < 15; i++) {
    long i_ms = micros();

    lcd.setCursor(0, 0);  // column 0, row 0
    lcd.print("[");
    for (int p=1; p <= i; p++) {
        lcd.setCursor(p, 0);  // column 0, row 0
        lcd.print((char)0xFF);
    }
    for (int p=i+1; p <= 14; p++) {
        lcd.setCursor(p, 0);  // column 0, row 0
        lcd.print("_");
    }
    lcd.setCursor(15, 0);  // column 0, row 0
    lcd.print("]");

    instructions_micro += micros() - i_ms;

    delay(250);
  }

  total_micro = micros() - total_micro;

  lcd.clear();
  lcd.setCursor(0, 0);  // column 0, row 0
  lcd.print("I_ms: ");
  lcd.print(instructions_micro);
  lcd.setCursor(0, 1);  // column 0, row 0
  lcd.print("T_ms: ");
  lcd.print(total_micro);

  delay(10000);
}

