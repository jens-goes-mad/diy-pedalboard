#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD I2C address (usually 0x27 or 0x3F)
LiquidCrystal_I2C lcd(39, 16, 2);  // (address, columns, rows)

void setup() 
{
  lcd.init();           // Initialize the LCD
  lcd.backlight();      // Turn on the backlight

  lcd.setCursor(0, 0);  // column 0, row 0
  lcd.print("Hello, World!!");
  lcd.setCursor(0, 1);  // column 0, row 1
  lcd.print("I2C LCD Test");
}

void loop() 
{
  // Nothing here for now
}

