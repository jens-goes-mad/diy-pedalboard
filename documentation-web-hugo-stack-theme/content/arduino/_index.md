---
title: Arduino
links:
  - title: Arduino IDE and NANO
    description: a blog about software and hardware development of a DIY pedal board
menu:
    main: 
        weight: 60
        params:
            icon: cpu
toc: true
tags: ["hardware", "arduino"]
categories: ["arduino"]
---
# ARDUINO

I started with an 8-bit processor in the mid-80s on an Apple IIe, marking my first deep dive into computing.
Back then, I explored languages like Assembler and Pascal, slowly decoding the magic of opcodes, instructions, and CPU cycles.

Now, nearly 40 years later, the circle has closed.
I'm once again working with an 8-bit processor — this time on an Arduino Nano — rediscovering Assembler, playing with instructions and cycles just like I did when it all began.

In today’s world, we all use technically brilliant touch devices from the age of two.
But in that convenience, we often lose touch with the fundamentals — forgetting how these devices actually work under the hood.
At their core, nothing has fundamentally changed: only the size, speed, and power consumption have evolved.

I chose the Arduino Nano for this section because it’s far easier to set up and experiment with compared to using
Visual Studio Code and the ESP-IDF toolchain for the final device.

## Setup

Download the Arduino IDE from arduino.cc, install it, and you’re ready to go!

<p class="warning">
NOW WE ENTER THE DANGER ZONE<br>
I AM NOT RESPONSIBLE IF YOU ACCIDENTALLY WIPE YOUR MACHINE<br>
PROCEED AT YOUR OWN RISK!
</p>

When building software, a ton of artifacts get generated.
To speed up development cycles, I usually move build and cache directories to a RAM disk —
something we were already doing 40 years ago. Unbelievable, right?

> THE FOLLOWING SECTION IS FOR MACOSX OR LINUX

Unfortunately, you can’t just configure the build path directly in the Arduino IDE.
So, I asked my trusty sidekick ChatGPT:

<span class="chatgpt">Please, provide a shell script to create / delete a ram disk on MacOSX</span>

See result on github: samples/osx-ramdisk.sh

Next, we need to trick the Arduino IDE into using the RAM disk as the build destination.
We do this by forcing it through the TMP environment variable.

<span class="chatgpt">Can you force Arduino IDE to use the ram disk as build destination?</span>

See result on github: samples/ARDUINO_NANO/arduino-on-ramdisk.sh

Instead of clicking the shiny Arduino IDE icon, we simply start it from a shell script.
And honestly? We can live with that.

As a result, your builds no longer pollute your SSD with temporary files and endless cache junk.
Your SSD stays clean, your build times stay fast, and your inner perfectionist sleeps better at night.
But wait — there’s a small workaround needed.<br>
While we can’t directly move the entire cache directory, we can use a symbolic link to point it to our RAM disk.
A simple trick — but incredibly effective.

First we remove existing artefacts by 

<span class="cli-action">rm -rf ~/Library/Caches/arduino</span>

create ram disk and build dirs

<span class="cli-action">./osx-ramdisk.sh create 512 myramdisk</span><br>
<span class="cli-action">mkdir -p /Volumes/myramdisk/arduino</span>

and link it by the unix command

<span class="cli-action">ln -s /Volumes/myramdisk/arduino-cache ~/Library/Caches/arduino</span>


Now setup board type (Arduino Nano), bootloader (old), port (cu*), programmer (Arduino as ISP)  
Open <span class="menu-action">File ⇒ Examples ⇒ 01.Basic ⇒ Blink</span>, compile and upload. 
If you see a blinking on board LED we can enter the world of Instructions and Cycles!

[Let's start with the basics](/arduino/instruction-basics).