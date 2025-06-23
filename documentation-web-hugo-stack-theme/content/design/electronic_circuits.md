---
title: "Design hardware (electronics)"
summary: "
How to design circuits, burn resistors and LEDs, wire chaos
"
date: 2025-06-01
layout: article
# type: article
XType: design
draft: false
# tags: ["chatgpt", "humor"]
# categories: ["humor"]
weight: 100
image: "Apple2e_motherboard.jpg"
categoryWeight: "010.001"
---
## Electronics circuits

Electronics are everywhere when you're working with knobs, LCD displays, and control surfaces.
But when it comes to microcontrollers, the real magic is how they combine dozens of discrete components
into a single chip — parts that, 45 years ago, would have filled an entire board in DIL (Dual In-Line) format.

Just compare the motherboard of an Apple IIe from 1983 with a modern ESP32. 
The ESP not only outperforms the Apple IIe in every aspect — CPU speed, memory, connectivity —
but it’s also smaller than a single 64KB memory chip from that era.

![Apple IIe motherboard from 1983 and ESP32 from 2020+
https://www.oldcomputr.com/apple-iie-1983/
](apple2e_esp32.jpg)

| Specification           | Apple IIe (1983)                               | ESP32 (modern)                          |
|-------------------------|-----------------------------------------------|-----------------------------------------|
| **CPU Architecture**    | MOS 6502 (8-bit)                              | Xtensa LX6 (32-bit)                     |
| **CPU Cores**           | 1 core                                        | 2 cores                                 |
| **Clock Speed**         | 1 MHz                                         | Up to 240 MHz                           |
| **RAM**                 | 64 KB                                         | 520 KB SRAM + optional external PSRAM  |
| **Storage (ROM/Flash)**| ROM: 16 KB (no internal flash)                | Up to 16 MB Flash                       |
| **Networking**          | None (external modem required)               | Wi-Fi, Bluetooth                        |
| **Display Support**     | Composite video (text & low-res graphics)     | SPI, OLED, LCD via GPIO                 |
| **Peripheral Interfaces**| Expansion slots, serial/parallel (via cards) | UART, SPI, I2C, PWM, ADC, DAC, GPIO     |
| **Power Consumption**   | High (for its era)                            | Ultra-low (battery-friendly)            |
| **Size**                | Full motherboard (~30×30 cm)                 | Single chip (~2.5×2.5 cm or smaller)    |

We started with transistors and resistors, grouped them into functional blocks, packaged those blocks in
DIL (Dual In-Line) ICs, and eventually packed everything into microcontrollers —
shrinking the footprint, but not the concept. In the end, it’s still about building modular, purpose-driven systems.
Only now, we can fit the entire lab in a chip the size of your thumbnail.
But while the hardware has evolved dramatically, our thinking hasn’t changed all that much.

It's a reminder that while the tools have changed, the challenges — and the fun — remain timeless.

## Throw away 5 pieces a week challenge

Over time, every cupboard fills with once-cutting-edge, now-dust-covered tech.
Right after I invested in a Brenner to burn programs onto tiny chips, microcontrollers stepped onto the stage.
Long before Arduino became the maker world's darling, true masterpieces were already being built —
often by small, inventive companies. I still remember getting my first microcontroller development boards
from New Zealand, hand-delivered by a close friend. But before I ever managed to integrate them into a real project...
Arduino and ESP took over the world.

Trash, with Tears
Today, I can buy an ESP32-S3 for 5€, complete with class-compliant USB, WiFi, Bluetooth, and more performance
than my first five dev boards combined.

And now?

My heart breaks, but it's time to say goodbye to gear I've kept for two decades.
Neatly boxed, carefully stored, mostly unused — but always cherished.  But I can keep them for now, 
next in line for the bin is: 100s of books like “JBoss”, “EJB”, “JMS”, “EJB 2”, “JMX”...


The hardware will follow soon after. There’s no way around it.
Progress moves forward. And sometimes, it steps right over your lovingly hoarded treasures.

## Breadboard and Simulation

### The simple LED --> resistor --> Switch component on a breadboard 

These days, we don’t need to solder.
We don’t need to squint at tiny resistors, double-check jumper wires, or wonder why the breadboard smells like
burning plastic. Consider this simple circuit:<br> 
```LED --> resistor --> switch.```
<br>On a breadboard it looks like this:

![LED switch on breadboard
https://circuitdigest.com/electronic-circuits/push-button-led-circuit
](Breadboard_Push-Button.jpg)

### Circuit Simulation

Thanks to tools like Falstad's Circuit Simulator (https://www.falstad.com/circuit/circuitjs.html),
we can build, tweak, and debug electronic circuits virtually — no breadboard, no burnt fingers, no accidental shorts.
What once took a full afternoon and a drawer of parts now takes minutes, a mouse, and a bit of imagination.

We just simulate our circuits.<br>
In the browser.<br>
With JavaScript.

Seriously.

{{< video src="LED_switch.mp4" type="video/mp4" autoplay="true" loop="true" >}}

We’ll dive into the details in the upcoming sections — where things get a little more hands-on.
That’s when we’ll get our hands dirty with Eagle, designing PCBs and soldering components like the
button matrix scanner and the MIDI I/O interface.

It's time to move from theory to hardware — and yes, probably burn a few fingertips in the process.
