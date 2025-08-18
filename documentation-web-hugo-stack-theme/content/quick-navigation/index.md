---
title: "Content / Quick navigation"
menu:
    main:
        name: Content / Quick navigation
        weight: 2000
        params:
            icon: link
---
# Content

This page summarizes the key areas I explored while developing the<br>
DIY-MIDI-MUSIC-WORKSTATION-PEDAL-BOARD.

## Hardware

### Microcontroller

The project uses two microcontrollers, the [Arduino Nano](/arduino) for simplicity and the 
[ESP32-S3](/esp32) for higher performance and [USB class-compliant](/esp32/midi-class-compliance) connectivity. 
[Pinouts](/electronics/mcu#pin-out) for both are documented. Along the way, I revisited [microcontroller fundamentals](/arduino/avr),
[opcodes and instructions](/arduino/avr/instruction-basics), reflecting on [“the good old days”](/design/electronic_circuits). 
I even simulated circuits without touching a soldering iron, 
using [online tools](/design/electronic_circuits#circuit-simulation).

### LCD Displays

Even LCD Displays are a bit outdated, they are helpful to show info in bad lighting conditions. And they are fun
to use with an [Arduino](/arduino/hardware_lcd) while inventing a simple protocol over I²C. And nothing prevents us 
from simulating LCDs with [JavaFX](/prototyping/simulation_lcd#idea) by using a simple true type font, or even simulate
to [hardware serial interface](/prototyping/simulation_arduino). 

### I²C

----

## Software

### Java / JavaFX

### C++

### Flutter / Dart

### GO

----

## Simulation

### Hardware and Software

### Electronic Circuits

Thanks to tools like [Falstad's Circuit Simulator](https://www.falstad.com/circuit/circuitjs.html), we can simulate
our button matrix hardware, without burned fingers.

----

## Tools

### HUGO

The blog itself is powered by [HUGO](https://gohugo.io/), a static site generator written in Go. 
Combined with the [StackTheme](https://github.com/CaiJimmy/hugo-theme-stack), it forms the backbone of the site
by simple [Markdown](https://www.markdownguide.org/basic-syntax/). 
Naturally, I extended the menu system and sidebar to fit my workflow (
[see](https://github.com/jens-goes-mad/diy-pedalboard/tree/master/documentation-web-hugo-stack-theme)).

Thanks to tools like Falstad’s Circuit Simulator, I can model hardware like the button matrix 
without burnt fingers or blown components.

### DOCKER

### Graphics

When writing documentation, it’s surprising how many non-development tools end up in the toolbox.
For this pedalboard project, apart from pure coding, I found myself juggling:
[GIMP](/tools/visuals#gimp) to slice, rotate, and scale images for diagrams and blog visuals.
[Online video editors](/tools/visuals#videos) for quick overlays, captions, and step-by-step visuals without heavy desktop suites.
[TrueType fonts](/tools/visuals#truetype-fonts) tools to 
[simulate LCD displays](/prototyping/simulation_lcd#simulation-of-hardware) in software and design 
[custom glyphs](https://github.com/jens-goes-mad/diy-pedalboard/blob/master/documentation/LCD5x7SegmentMonospace-Regular.otf) 
for special characters.

UML tools – because a picture (or a component diagram) really can explain in seconds what a thousand words struggle to.

In the end, documentation becomes its own mini-project, with as many moving parts as the hardware itself.

#### 2D

#### 3D

### Repository


You find everything on [github](https://github.com/jens-goes-mad/diy-pedalboard/)