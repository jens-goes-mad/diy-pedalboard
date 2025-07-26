---
title: "Simulating Components"
summary: "
Simulation"
date: 2025-06-01
layout: article
XType: design
draft: false
weight: 101
---
## Simulation vs. Emulation – What's the Difference?
Although the terms simulation and emulation are sometimes used interchangeably, 
they serve different purposes in software and hardware development — and understanding the distinction is key 
when choosing the right approach.

### Simulation (Modeling Behavior)
Simulation is about mimicking the behavior of a system or component in a simplified, abstract way. 
It doesn’t try to perfectly replicate the underlying internals — instead, it models just enough to let you observe, 
analyze, or test how something would behave under certain conditions.

- Simulating sensor data to test how an application reacts to temperature changes
- Simulating network latency to see how a service handles delays
- Simulating user interactions in a UI prototype to gather feedback
- Simulations are usually faster and more lightweight, making them ideal for early design stages, 
  performance analysis, or algorithm testing — especially when exact hardware or real-world conditions aren’t available yet.

### Reproducing Reality
Emulation, on the other hand, is about reproducing the real behavior of a system or component as closely as possible. 
The goal is to make other software or systems believe they’re interacting with the real thing — often at a low, 
detailed level.

- Running mobile apps on a desktop emulator that mimics an Android device
- Emulating a microcontroller so you can test firmware without physical hardware
- Emulating an old gaming console to play original games on modern machines
- Emulators aim for high fidelity, often down to the instruction set or timing behavior. 
  This makes them essential when you need to test real code, legacy compatibility, or low-level integration, 
  especially when access to actual hardware is limited or expensive.

## Simulation of Hardware

Let’s pretend we don’t have a [running prototype](/arduino/i2c/i2c-task-mess/#overhead) to control LCDs just yet.
No hardware soldered, no wires tangled — just an idea.
So how do we explore how data could be distributed across multiple displays during development?
Easy: we simulate it.
We treat each LCD as a component — without caring whether it’s software or hardware. If the interface is clean and well-designed, we can swap the implementation later without breaking anything. That’s the beauty of abstraction.
And just to embrace polyglot programming, we’ll skip Dart and C++ this time and do it in Java.
Because why not?

### Idea

The main idea is simple: We use a TrueType Font which shows text like a dot matrix, show 4 rows of 20 character, done.
And to prevent you get bored, let me show you the final result, before we get into nasty details

![LCD Simulator in JavaFX](LCD_display_simulation_javafx.jpg)

Curious? Great! Instead of repeating everything here, check out the [Tools and Visuals](/tools/visuals#truetype-fonts) 
section for details on how we handle TrueType fonts and why they play a surprisingly important role.

First, let’s put our AI companion to work, burning a bit of fossil energy to spin up a JavaFX-based skeleton. 
I’ve polished the entire conversation into a [PDF](ChatGPT-JavaFX_program_example.pdf), but honestly, 
it only took four well-placed questions to get the initial “single display” simulator app up and running, with maven.


