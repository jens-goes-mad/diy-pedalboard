---
title:  I²C and the "no-delay() approach"
layout: article
draft: false
summary: "
Arduino and I²C + LCD displays and why delay() is strictly forbidden and we get rid of it by by millis()  
"
weight: 201
image: "PB_I2C_LCD_no_delay.jpg"
tags: ["hardware", "microcontroller", "I2C", "Arduino"]
categories: ["arduino"]
---
##  Goodbye delay(), Hello millis()
The basic idea now is to get rid of the blocking `delay()` method, and keep track of time using `millis()`.
Instead of relying on a kernel manager or a process scheduler, we mimic scheduling ourselves in the simplest possible way.
In the earlier sample, we had a 250 [ms] delay between each progress bar block to simulate "background work."
Now, instead of halting everything with `delay()`, we calculate when the next action should start (using a timestamp), 
and allow the program to keep looping freely in the game loop without interruption.

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

      // Wire.begin();
      // Wire.setClock(400000); // Set I2C clock to 400 kHz

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


- When we look at the figures displayed on the LCD, we see a total runtime of 3,547.220 [ms].
- Time spent actually driving the LCD is about 715.735 [ms], which is slightly more than in our first version.
- Our loop still includes 14 delays, each 250 [ms], so:
  14 × 250 [ms] = 3,500 [ms].
- The expected runtime in this case would be about 3,500 [ms]. Why?
  In the new version, we reschedule our task by setting a timer offset to "current time + 250 [ms]."
  In the old version, the delay() came after the display update instructions, so the two times added up.
  In the new version, the display update time is included within the "wait window," effectively shortening the actual wait.
- The resulting drift is around 47.220 [ms], which is small enough to be considered measurement error.

But here’s the important part
Our LCD processing alone takes up about 715.735 [ms] / 3,547.220 [ms] ≈ 20% of the total runtime.

- So yes, we "lost" about 4% efficiency due to loop and computation overhead,
- but we gained back access to 80% of the CPU time that was previously wasted in blocking delays.

##  Speed, it is all about speed!

Hang on! Can we make things even faster?

Yes! But before we dive into fancy techniques like double buffering and advanced optimizations,
why not try the simplest solution first: shift gears on the I²C bus! By default, the I²C bus runs at 100 [kHz].
But we can crank it up to 400 [kHz] with just two lines of code:

      // BE CAREFUL, PLACE AFTER lcd.init()
      Wire.begin();
      Wire.setClock(400000); // Set I2C clock to 400 kHz (maximum on Arduino)

... and surprise, the new figures are: 

- Total runtime drops to 3,518.744 [ms].
- Time spent actually driving the LCD drops to 290.500 [ms], a boost of around 60%!
- That means we now spend only 8% of the overall time updating the LCD, leaving a massive 92% of CPU time free for other tasks.

Even though this is a clear step forward, the code still needs serious polishing.
Right now, the variables controlling the scheduling live in the public space, and as we add more tasks, 
things will quickly descend into chaos. This is exactly the moment to jump into OOA (Object-Oriented Analysis) 
and OOD (Object-Oriented Design). It’s time to introduce classes, encapsulate our logic, 
we start thinking in components once again — just like we do with [hardware blocks](/design/hardware_components)
or [overall goal](/prototyping/birds_view). But never forget [build fast, fail fast, no fear](/prototyping/fast_samples).

Now unpack it and we move from global variable mess to clean [classes and task](/arduino/i2c/i2c-task-mess) lists
