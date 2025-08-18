---
title: Visuals
links:
  - title: All tools online / offline I used to build the DIY-Music-Workstation-Pedal-Board
    description: a blog about software and hardware development of a DIY pedal board
layout: article
draft: false
tags: ["tools"]
categories: ["tools"]
image: "_.jpg"
imageSuppress: "Page"
---
## Visuals!

That may sound like a strange mix at first — but it’s absolutely essential for this project.

### Images

All illustrations and visuals throughout the blog were created or edited using [GIMP](https://www.gimp.org/).
You’ll even find the original .xcf Titles_and_Sections.xcf in the [documentation](https://github.com/jens-goes-mad/diy-pedalboard/blob/master/documentation), folder on GitHub.
A basic understanding of image editing is always helpful — especially when creating technical documentation or annotating diagrams.

![gimp](Gimp.jpg)

### Videos

Some things are hard to explain with static images alone — especially when demonstrating animations, progress over time, or interactive UI behavior.

To handle this, I use [clipchamp](https://clipchamp.com/en/) — a surprisingly good online tool 
(yes, it’s from Microsoft!) that lets you combine visuals, sound, and subtitles. 
It’s simple, intuitive, and free for solo developers. Just works.
Locally I use [HandBrake](https://handbrake.fr) to convert Videos from mov to mp4.

### TrueType Fonts

Wait... fonts? Why fonts?

In [prototyping](/prototyping/simulation_lcd#idea) we simulate LCD screens in software (JavaFX) to test UI components and concepts without physical hardware.
My first idea was to use a ready-made TrueType [LCD-style font](https://www.fontspace.com/search?q=LCD), 
brilliant for prototyping, or so I thought... Until I remembered those [user defined characters](/prototyping/thinking_outside_the_box#modern-tools-repurposed) 
on real LCDs. To simulate those missing custom characters, I used [glyphr studio](https://www.glyphrstudio.com/app/) 
to modify and extend the [font](https://github.com/jens-goes-mad/diy-pedalboard/blob/master/documentation/LCD5x7SegmentMonospace-Regular.otf), 
and [fontdrop](https://fontdrop.info/#/?darkmode=true) to inspect and verify the changes.

![glyphrstudio](glyphrstudio.jpg)

### UML

We don’t use [UML](https://circle.visual-paradigm.com/diagram-examples/) in this project — but as a developer, 
you should still know what it is. Why? Because UML (Unified Modeling Language) is often the “language” your customers, 
product owners, or managers will speak — or at least think developers speak.
It’s like the Esperanto of software architecture: not perfect, but widely recognized.
If you ever need to whip up a quick diagram (to communicate an idea, explain a process, or impress someone in a meeting), 
try a text to diagram approach with [plantum](https://plantuml.com/).

And for inspiration or ready-made examples, check out this collection of UML diagrams.
