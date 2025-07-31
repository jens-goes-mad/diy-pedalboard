---
title: "Simulating Components (Part 2 - I2C)"
summary: "
Simulation"
date: 2025-06-01
layout: article
XType: design
draft: false
weight: 102
---
## Simulation vs. Emulation

[In Part 1](/prototyping/simulation_lcd), we learned how to simulate an LCD display entirely in Java
using a custom TrueType font rendered in JavaFX. That was a great first step, but a display isn’t much use unless 
it actually displays something. Now it’s time to connect the dots.
To demonstrate component interaction, we’ll use a simple [Arduino based test program](https://github.com/jens-goes-mad/diy-pedalboard/tree/master/samples/ARDUINO_NANO/the-beginning/communication/serial-to-led) 
that listens for incoming strings via the serial port and displays them on an actual LCD.


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

This sketch puts the Arduino into a tight loop, waiting for incoming data on the serial line. 
Whatever text you type into the terminal (followed by RETURN) will be rendered on the first line of the LCD.
To identify your connected Arduino device open a terminal and enter: `ls -l /dev/cu.*`<br>
...which results in something like `/dev/cu.usbserial-nnnn`. Then, to open a serial connection at the correct 
baud rate (115200 bps), matching the `Serial.begin(115200)` by typing:<br>
`screen /dev/cu.usbserial-nnnn 115200`. Now you’re live! Type anything, press ENTER, and it shows up on the LCD.
To exit the terminal: Press `Ctrl+A` followed by `Ctrl+D`.


## Protocol

While having a physical interface is essential, communication requires structure, a protocol. 
Instead of relying on verbose formats like XML or JSON, we keep it simple and efficient with a minimal string-based 
syntax that’s easy to parse and ideal for resource-constrained environments like microcontrollers.

For now, we support a handful of core commands (all terminated by `\r`:

| Command | Parameters              | Description                                         |
|---------|-------------------------|-----------------------------------------------------|
| `CLR`   | `A`/`0`–`3`             | Clear one or all displays                           |
| `STC`   | `A`/`0`–`3` `row` `col` | Set cursor on given display                         |
| `WRT`   | `A`/`0`–`3` `text...`   | Print line of text to display, terminated by newline |
| `BLK`   | `A`/`0`–`3` `0\|1`      | Toggles blinking cursor                              |

### Serial test

Using `screen` to talk to your Arduino over serial isn’t exactly fun — you don’t even see what you type.
But hey, typing blindly is a core competency for seasoned developers, right?<br>
Typing `screen /dev/cu.usbserial-1410 115200` opens a terminal session, 
and whatever you type gets sent directly to the Arduino. There’s no visual feedback, no echo, 
just pure trust and instinct.
When terminating a `screen` terminal chances are high, sockets are still up and running blocking access from your 
Arduino IDE, preventing further uploads.

On MacOSX the following command shows the process that’s still holding the port. 
Once identified, you can forcefully terminate it: `sudo lsof /dev/cu.usbserial-1410`.
Now we send the blocking process to hell by `kill -9 <PROCESS_ID_FROM_LSOF>`<br>
Boom. Serial freed. Uploads possible again. Crisis averted.

### Serial use for Sissy's like me

Let's say I am to old for that shit (Bruce Willys). How can we communicate via a serial interface in a human compatible
manner? Let's write a terminal program in [Java](/DIY_PEDALBOARD/diy-pedalboard/samples/JAVA/the-beginning/lcd-display-with-wrapper-chatgpt/src/test/java/de/diy_pedalboard/controls/SerialTerminalApp.java)!




