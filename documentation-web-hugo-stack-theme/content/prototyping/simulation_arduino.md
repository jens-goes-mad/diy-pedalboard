---
title: "Simulating Components (Part 2 - Arduino)"
summary: "
We are simulating the behaviour of the Arduino –> I2C –> LCD 
by swapping a real hardware serial connection by software simulated connection, but using the same protocol.
"
layout: article
XType: design
draft: false
weight: 220
---
## Simulation vs. Emulation

[In Part 1](/prototyping/simulation_lcd), we learned how to simulate an LCD display entirely in Java
using a custom TrueType font rendered in JavaFX. The [hardware section](/prototyping/hardware_lcd) demonstrated, 
how to use a Java based terminal application to interact with the hardware LCD. Next we will tweak our JavaFX 
application to use the same serial interface implementation, which allows swapping them without disrupting or 
changing any other software component of our DIY-pedal board.

And simulating Arduino is a bit misleading: we are simulating the behaviour of the Arduino --> I2C --> LCD by
swapping a real hardware serial connection by software simulated connection, but using the same protocol. 
Thus from a callers perspective it does not play a role if the CMD changes text on real hardware or a simulated LCD.s