---
title:  I²C and more (Multitasking from 1991, Interrupts and Context Switching)
layout: article
draft: false
summary: "
Arduino and I²C + LCD displays and why delay() is strictly forbidden and how this relates to interrupts, 
context switching and even cooperative or preempting multitasking  
"
weight: 200
image: "PB_I2C_LCD.jpg"
tags: ["hardware", "microcontroller", " I²C", "Arduino"]
categories: ["arduino"]
---
##  I²C

We already discussed the use of [I²C for driving LCD](/electronics/i2c) displays. Now lets start with a small 
sample [GitHub](https://github.com/jens-goes-mad/diy-pedalboard/tree/master/samples/ARDUINO_NANO/the-beginning/i2c/i2c_lcd_progressbar/i2c_lcd_progressbar.ino)
which initializes the I²C and display a simple progress bar and measures overall run time.

### Schematic

The PCF8574 device is an 8-bit I/O expander designed for 2.5-V to 6-V VCC operation. 
It provides general-purpose remote I/O expansion for most micro-controller families via the I²C
interface (serial clock, SCL, and serial data, SDA, pins). 
You can download the specification from [TI](https://www.ti.com/lit/ds/symlink/pcf8574.pdf?ts=1752376412454).

![Arduino I2C + Port-Expander to drive LCDs
https://avrhelp.mcselec.com/index.html?lcd_i2c_pcf8574.htm
](I2C-LCD-schematic.jpg)

### Software

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

### Result

{{< video src="I2C-LCD-Arduino.mp4" type="video/mp4" autoplay="false" loop="false" >}}


- When we look at the figures displayed on the LCD, we see a total runtime of 4,175.216 [ms].
- Time spent actually driving the LCD is about 668.020 [ms].
- Our loop includes 14 delays, each 250 [ms], so:
  14 × 250 [ms] = 3,500 [ms].
- The expected runtime would then be:
  3,500 [ms] + 668.020 [ms] = 4,168.020 [ms].
- This results in a minor drift of about 7.196 [ms], or roughly 0.17% — pretty negligible.

But here’s the important part:
- Our LCD processing alone takes up 668.020 [ms] / 4,175.216 [ms] ≈ 16% of the total runtime.
- In other words, 84% of the time is wasted sitting in blocking delays.

And it’s painfully clear: With this basic approach, there’s no way we could run more than one progress bar 
(or other tasks) at the same time.

The lesson?
We have to avoid `delay()` — there’s simply no way around it.

## Serial and Parallel processing
If you have one core you can process exactly one program, instruction by instruction at a time. You can't process things
in parallel, but you can predict doing so, by simply process minor tasks serially and you switch between serial tasks 
fast to create an overall parallel processing feel. But lets look at a real world example to introduce concepts like
interrupts, cooperative and preemptive multitasking and context switch.

### Real-World Examples
Once again, let’s use men as an example — famously known for their limited parallel multitasking abilities.

Imagine sitting with your neighbor, having a beer, and gossiping in the sun.
This is a heavy task, and yet, men can easily keep at it for hours.

#### Interrupt
Now, picture someone passing by and greeting you. This is just like an interrupt.
It’s a brief interruption of your current task with some consequences:
You stop talking, raise your head, greet back, and then seamlessly continue the conversation.

In microcontroller terms, here’s what happens:

- Some external signal (for example, a pin being pulled low) triggers an interrupt.
- The processor saves the current Program Counter (PC) and all registers (those “last sentences” you were talking about).
- It then jumps to a predefined address (PC), known as the Interrupt Service Routine (ISR), 
  and executes that short task — in this case, greeting back.
- Once done, it restores all flags and the PC, returning exactly where it left off — so the gossip continues, 
  uninterrupted (almost).

Interrupts are cheap, because the overhead required to execute it, is minimal. But things change dramatically in case 
of context switches. 

#### Context Switch
A context switch can be thought of as a long-running interrupt.
Let’s say your neighbor’s wife opens the window and commands: “Bring out the rubbish!”

Now, two things can happen:
- You ignore it.
- You do it.

For the sake of young readers (and peaceful marriages), let’s skip option 1.

Here’s what happens:
You set your beer bottle on the table, stand up, head inside, check all the bins, 
drag the garbage container around the corner, and pull it down the street to the pickup point. 
Then you walk back, wash your hands, sit down, and wait for your friend to return. At that point, 
you struggle to remember what you were talking about before the interruption — and need to mentally 
"load the context" again to continue the conversation.

The processor does exactly the same:
It pushes all registers and state onto the stack, sets up the environment for the new process, 
and starts working on it. Because this new task is longer, the processor loses parts of its internal “mental cache”, 
like branch predictions and cached data — much like a human developer who finally understands a tough problem 
after hours of focus, only to have it all collapse when the boss yells, **“Please update your timesheet!”**

Context switches are expensive. Really expensive.

And, just like men with chores, processors (and developers) prefer to avoid them whenever possible.

### Multitasking

If our tasks never finish, there will be no "parallel" execution at all. That's the reason we differentiate between 
cooperative and preemptive multitasking. If your as old as I am, you may remember these terms since introduction of
Microsoft's Win 3.11 (cooperative multitasking) and IBM's OS/2 (preemtive multitasking) around 1991. 

- Cooperative means, a process has to be fair and stop processing by free will and hand processing power back to
  someone else.
- Preemptive means, a manager controls processing time (here: your wife). If you don't follow the rules, 
  you're getting killed.

In our example, the delay() method is a classic blocking approach — it completely halts execution 
and monopolizes the processor. There’s no actual multitasking happening; everything else simply has to wait.
But instead of blocking, we can design our code in a more "cooperative" spirit: 
by using a non-blocking strategy with millis(), we periodically check if it's time to act, 
while letting other logic continue running in between.

That’s exactly where we’re heading next: we’ll refactor the classic Arduino loop into a generic tasklet class in C++, 
built on millis()-based timers. This approach allows us to create modular, time-controlled "mini-tasks" 
that feel like multitasking — without actual OS-level scheduling.

### Serial Tasks

The Arduino consists of one core and a single main loop — the famous `loop()`.
Whenever your program reaches the end of this function, the Program Counter (PC) is simply reset to the start, 
and the whole process begins again. This structure is similar to most game engines or even Flutter, 
which we’ll use later for the UI. And it has one major advantage:

> No concurrency issues, no threads, no locks, no synchronize, no extra complexity — it all just disappears.

Follow up: [Goodbye delay(), Hello millis()](/arduino/i2c-no-delay)
