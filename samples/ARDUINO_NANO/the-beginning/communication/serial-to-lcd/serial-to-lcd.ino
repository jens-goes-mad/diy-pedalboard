#include <Wire.h>
#include <LiquidCrystal_I2C.h>

// Set the LCD I2C address (usually 0x27 or 0x3F)
LiquidCrystal_I2C g_lcd(39, 16, 2);  // (address, columns, rows)

void setup()
{
  g_lcd.init();
  g_lcd.backlight();
  g_lcd.clear();

  Wire.begin();
  Wire.setClock(400000); // Set I2C clock to 400 kHz

  Serial.begin(115200);
}

void loop()
{
  if (Serial.available()) {
    String cmd = Serial.readStringUntil('\n');
    cmd.trim();

    g_lcd.setCursor(0, 0);
    g_lcd.print(cmd);
  }
}
