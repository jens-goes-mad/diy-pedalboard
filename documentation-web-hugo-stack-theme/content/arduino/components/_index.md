---
title: Components and Arduino 
links:
  - title: Arduino / I²C / Components 
    description: a blog about software and hardware development of a DIY pedal board
menu:
    main:
        identifier: "arduino"
        weight: 80
        params:
            icon: cpu
toc: true
tags: ["hardware", "arduino", "i2c", "components"]
categories: ["arduino", "components"]
image: Car-Components.jpg
imageSuppress: "Page"
---
# Components

In the previous chapters we discussed Arduino programming, I²C and LCDs.<br>
The DIY-MIDI-Music-Workstation-Pedal-Board uses four 2004 LCD Displays (20 characters, 4 rows) which
offers plenty of room to display information even in bad lighting conditions. We can use 4 Rows or use BigChar 
techniques to show characters spanning two, three, or even four rows. It is time to turn the breadboard hardware into
a reusable component. 

![7" Touchscreen and 4 LCD 2004 Displays](DIY-Pedalboard_04.jpg)

## Component Diagram

A component diagram (see [UML](design/uml)) breaks down the actual system under development into various high levels of functionality. 
Each component is responsible for one clear aim within the entire system and only interacts with other essential 
elements on a [need-to-know basis](https://www.visual-paradigm.com/guide/uml-unified-modeling-language/what-is-component-diagram/).
