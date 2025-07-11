---
title:  I²C
layout: article
draft: false
summary: "Arduino and I²C + LCD displays"
weight: 200
image: "PB_I2C_LCD.jpg"
tags: ["hardware", "microcontroller", " I²C", "Arduino"]
categories: ["arduino"]
---
##  I²C

We already discussed the use of [I²C for driving LCD](/electronics/i2c) displays. Now lets start with a small 
sample [GitHub](https://github.com/jens-goes-mad/diy-pedalboard/tree/master/samples/ARDUINO_NANO/the-beginning/i2c/i2c_lcd_progressbar/i2c_lcd_progressbar.ino)
which initializes the I²C and display a simple progress bar.

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

But wait a second.<br>
This program runs in a loop, and each loop takes about 
14 * 250 [ms] + 2.000 [ms] = 5.500 [ms], or 5.5 [sec] total.
As we discussed in [instructions and cycles](/arduino/instruction-loop-sample) , an Arduino Nano runs at 16 [MHz].
That means in 5.5 [sec], it could theoretically execute:

> 16 [Mhz] * 5.5 [instructions / sec] = 88.000.000 [instructions]

So, what’s happening here? Well, that program above must be an enormous waste of processing time.
Why?

