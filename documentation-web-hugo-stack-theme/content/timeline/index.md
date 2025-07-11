---
title: Timeline
links:
  - title: A blog about building a DIY pedal board
    description: a blog about software and hardware development of a DIY pedal board and here is the timeline
menu:
    main: 
        weight: 1000
        params:
            icon: calendar-stats

comments: false
toc: false
timeline:
  - label: "2025-05"
    title: "Launch"
    body: "
We finally launched the platform!

Surprisingly, not by working on the actual product, but by going down the wonderfully inefficient rabbit hole of
finding a blog system. We spent way too much time searching for something that was: Easy to use,
Themeable without hacking CSS for hours, Not allergic to Markdown and didn’t look like it was built in 2003

In the end, the most promising setup turned out to be a combination of Docker, HUGO (https://gohugo.io/
don't be scared, you can use HUGO without account), the HUGO Stack theme (https://github.com/CaiJimmy/hugo-theme-stack)
— and that’s what you’re reading right now. Naturally, when you get familiar with such a system you start to develop
your own templates. You can see progress (or not) on github
(https://github.com/jens-goes-mad/diy-pedalboard/tree/master/documentation-web-hugo-stack-theme)"
  - label: "2025-06-01"
    title: "Added some docs around ChatGPT"
    body: "
ChatGPT is such a powerful tool, so there is a section particular for [ChatGPT](/chatgpt) demonstrating usage and results.
But you can also spend time in useless comment sections and non-working iframes from cusdis (https://cusdis.com)
"
  - label: "2025-06-04"
    title: "Added 'Form follows Function' section"
    body: ""
  - label: "2025-06-13"
    title: "Added 'Prototype' section and polished text and images in general"
    body: ""
  - label: "2025-06-21"
    title: "Déjà Vu and LCD Displays, Tiles and more"
    body: "
Whatever I do lately, I can’t shake the feeling: I’ve done this before. 
Working with I²C LCD displays in 2025 feels eerily similar to 1986, when we hacked together our first games
in assembly, squinting at green or orange screens and praying our 80×40 character displays would hold steady
through the crash and programs were saved on cassette correctly. 
Or back in 1991, when I tried to squeeze every last cycle out of my Casio FX-602P — my first programmable calculator.
512 precious program steps. Solving a² + b = x equations on the fly, with hardware over-clocking.
A time when no teacher had any idea what this little pocket genius could actually do 
([prototyping - thinking out of the box](/prototyping/thinking_out_of_the_box/))
"
  - label: "2025-06-31"
    title: "Electronics"
    body: "
Added a new Section as introduction to electronics in general, and how to burn fingers, while soldering.
This is an introduction to upcoming topics like MIDI and Button Matrix devices 
([design - electronics](/design/electronic_circuits)).
"
  - label: "2025-07-06"
    title: "Arduino"
    body: "
I’ve added a new section as an introduction to Arduino and 8-bit processors in general ([Arduino](/arduino)).
We’ll take a closer look at instructions and cycles ([AVR](/arduino/instruction-basics)) and then really 
torture ourselves with disassembly, HEX opcodes, and low-level instruction analysis ([Instructions and Cycles](/arduino/instruction-loop-sample)).
Extended HUGO to show tags sorted (thanks to ChatGPT), added navigation links, some images, aso.
Last but not least, [I²C](/electronics/i2c) got some attention 
"
  - label: "2025-07-13"
    title: "Arduino / I²C / LCD"
    body: "
Now it’s getting tough.
We’re stepping back into 1991, into the world of cooperative and preemptive multitasking — concepts introduced 
(and fought over) with Windows 3.11 and OS/2. We are talking about ancient history to drive a simple LCD display!
Even something as simple as refreshing an [LCD over I²C](/arduino/i2c-basics) can teach us important lessons about timing, 
blocking calls, and pretend working in parallel.
"
---

# DIY Pedal board timeline

{{< timeline >}}
