#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD I2C address (usually 0x27 or 0x3F)
LiquidCrystal_I2C lcd(39, 16, 2);  // (address, columns, rows)

void setup() 
{
  lcd.init();
  lcd.backlight();
}

void loop() 
{
  lcd.clear();

  for (int i=0; i < 15; i++) {
    lcd.setCursor(0, 0);  // column 0, row 0
    lcd.print("[");
    for (int p=1; p <= i; p++) {
      lcd.setCursor(p, 0);  // column 0, row 0
      lcd.print((char)0xFF);
    }
    for (int p=i+1; p < 16; p++) {
      lcd.setCursor(p, 0);  // column 0, row 0
      lcd.print("_");
    }
    lcd.setCursor(15, 0);  // column 0, row 0
    lcd.print("]");
    delay(250);
  }

  delay(2000);
}

