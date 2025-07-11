---
title: "I2C"
# date: 2025-06-03
layout: article
draft: false
summary: "Why it matters and replaces to old UART and helps connecting multiple LCD displays to one micro controller
 with less wires
"
weight: 100
categoryWeight: "250.300"
image: "I2C.jpeg"
---
## From UART to I2C

In the world of embedded systems and electronics, data communication between devices is crucial. 
One of the oldest and most widely used methods for serial communication is UART — short for 
Universal Asynchronous Receiver/Transmitter. It's simple, reliable, point-to-point communication, 
built into almost every microcontroller like the [arduino](/arduino/i2c-basics).

UART is a hardware communication protocol that allows two devices to send and receive data serially, 
one bit at a time over a single wire for transmission (TX) and one for receiving (RX). 
Unlike protocols like I²C or SPI, UART doesn't use a clock line. 
Instead, it works asynchronously, meaning both devices must agree on the communication speed beforehand 
(called the baud rate).

## I2C

In the world of embedded systems and electronics, efficient communication between components is essential. 
One protocol that has become a standard for short-distance, low-speed data exchange is I²C (Inter-Integrated Circuit). 
Originally developed by Philips (now NXP) in 1982, I²C has proven to be a compact, flexible, and powerful solution 
for connecting multiple devices using minimal wiring.

### What is I²C?

I²C is a two-wire serial communication protocol designed to allow multiple integrated circuits (ICs) to communicate 
with one another over a single bus. It uses just two lines:

    SDA (Serial Data Line): for data transfer
    SCL (Serial Clock Line): for clock synchronization

This simplicity in design is one of the reasons for its widespread adoption in embedded and consumer electronic systems.

### How I²C Works

I²C operates on a master-slave architecture, where one or more master devices control the communication and initiate 
data transfers. The slaves are peripheral devices such as sensors, memory chips, or displays. 
Each slave is assigned a unique address, allowing the master to communicate with one device at a time 
without signal collisions.

The master also provides the clock signal (SCL), which keeps all communication synchronized.
This synchronous nature makes I²C more reliable and deterministic than asynchronous methods.

![LCD
https://www.infineon.com
](I2C_Bus.png)

### Why I²C is More Advanced Than UART?

1. Multi-Device Support
2. Fewer Wires
3. Synchronized Communication
4. Built-in Addressing
5. Faster (Standard: up to 100 kbps, Fast: up to 400 kbps, High-Speed: up to 3.4 Mbps)

I²C stands out as a robust and efficient communication protocol for modern electronic systems. 
Its two-wire design, support for multiple devices, and synchronous operation make it far superior to older serial 
methods like UART for many embedded applications. Whether you're working on a simple sensor network or a complex 
IoT system, I²C offers the simplicity and scalability needed to keep your designs clean and efficient.

--- 

## Fewer Wires? Why that??

Consider the following schematic for connecting an LCD display to a microcontroller.
In a standard setup, you typically need to connect 16 wires just to get one display working.
And since the DIY pedal board will use four of these displays, the wiring can quickly turn into a complete nightmare.
One option to manage this mess is to use additional TTL components with "Chip Enable" techniques.
This allows you to connect all LCDs to the same microcontroller ports but select only one at a time for communication —
like a switch on a railway track directing trains to different routes.

![LCD
https://www.electronicwings.com
](EW_LCD_Interfacing_Diagram-8bit.png)

Instead of wrestling with a spaghetti of wires and enable logic, we can use I²C LCD driver chips.
These drivers are soldered directly onto the LCD module and expose a simple 4-wire I²C interface.
Multiple displays can be chained on the same bus, dramatically reducing both the number of wires
and the number of microcontroller ports needed.

![I²C LCD controller](I2C_LCD.png)


It’s simpler, cleaner, and makes the whole system far more scalable — exactly what our pedal board needs.

