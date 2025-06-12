---
title: "Design hardware (components)"
summary: "Summary: Design hardware components"
date: 2025-06-01
layout: article
# type: article
XType: design
draft: false
# tags: ["chatgpt", "humor"]
# categories: ["humor"]
weight: 100
image: "D_HC_chapter.jpeg"
categoryWeight: "010.002"
---
## From Bird's Eye to Building Blocks

Zooming out gives us a high-level understanding of how the device naturally breaks into modular components
each with a distinct responsibility.

{{< video src="DIY-Pedalboard_06-11.mp4" type="video/mp4" autoplay="true" loop="true" >}}

Here's a first breakdown of the core I/O subsystems and their roles:

---

### Front view

![Image: front view](D_HC_FV.jpeg)

#### Foot Pedal Buttons
These act as output components that send press/release events to other modules.
Now here’s a question worth pausing on: How do we represent this data internally?
We could define our own protocol — something like “Button 1 pressed” over a custom API.
But there's also a smarter route: treat the entire foot controller buttons as a MIDI device itself.
If we can convince an Arduino (or similar microcontroller) to behave like a MIDI-compliant USB device,
integration becomes almost effortless. Always think in reusable, swappable components, regardless of software or hardware.

#### UI and MIDI Processing
This will be a major focus of its own. The UI and processing layer handles all real-time interactions, 
message transformation, routing logic, and visual feedback. Think of it as the conductor — orchestrating everything 
from button presses to SFZ playback.

---

### Rear view

![Image: rear view](D_HC_RV.jpeg)

#### MIDI (Hardware & Data)
Handles traditional DIN MIDI connectors, manages the serial interface, and transfers MIDI data between components.
It’s the backbone for communication with external MIDI gear.

#### USB Device Handling
USB isn't a core topic here, but it still plays a support role.
The system needs to detect device connections/disconnections, track connected peripherals,
and trigger a MIDI device list refresh whenever a new USB MIDI device shows up — even if no MIDI event has occurred yet.

#### Network Connectivity
Not essential for a pedal board — unless you’re developing.
Thanks to the API-driven architecture (details coming later), remote access becomes possible.
This means you can test and tweak the system from your desk, even if the pedal board is stationed on the other
side of the studio next to my keyboard rig.

## Of course, paper doesn’t run code — but it’s a good start.

When I tackle a new problem, my first goal is simple: 

### Get a sample running — fast.
I don’t worry about perfect naming, method size, or clean separation. No fancy architecture. No premature abstractions. 
Just a quick main() method, hardcoded values, and a vague sense of purpose. Why?
Because getting something to run immediately tells me:
* Which libraries I’ll actually need
* Whether JDK 24 is going to play nice
* And whether I’m about to spend a weekend in dependency hell

### From Prototype to Project
Once that minimal sample works, I start refactoring. Code moves from the playground into proper service classes, 
I start applying meaningful naming conventions, and structure things like I’m building something that’ll still 
make sense three months from now. Why does this matter? Because:
* Good structure grows with the project
* Clean naming avoids “what the hell is this doing again?” later
* It sets the tone for maintainable, testable code from the beginning

### Unit Tests and the War on Mocks
Next, I add unit tests to verify each service. This step always forces me to rethink the code's structure:
Is it too stateful? Too tightly coupled? Do I need to redesign it to make testing possible without cheating?

And now a personal opinion — brace yourself:
If you're relying on mocks, something probably went wrong during the architecture phase.
Mocks often signal over-coupling or impure design. I aim for services that don’t need mocks to be tested — 
and that means keeping them stateless and modular by design.













## Thinking Naively (and Breaking the Rules on Purpose)<br>
Let’s ignore all best practices and architectural principles for a moment, and look at the software from a completely
naive perspective. Sure, we could just write one big application that does everything — the monolith approach.
No layers. No (micro-)services. No abstractions. Just one app to rule them all.

Lets assume Java is our hammer, every problem starts to look suspiciously like a nail.

As a Java developer since version 1.8, I’ve been through my fair share of pain — from the awkwardness of EJB 2.0,
to the madness of OSGi, and more than a few over-engineered frameworks that promised everything but delivered therapy bills instead.
MIDI can be handled easily by the javax.sound.midi package, and for USB access, there’s multiple cross-platform libraries
like usb4java (https://?????) — which wraps native USB access with JNI. On a low-powered device like a Raspberry Pi,
you can even offload some work to the client via a fancy JavaScript UI, instead of rendering UI on the device,
backed by a simple internal REST API. But I've opted for a 7-inch touchscreen on this board—and that changes things,
because from a user's perspective, a native UI just feels more responsive, polished, and frankly, more satisfying.
And over the years I've used more UI libraries than I'd like to admit:

* XVT / XVTDSC (a cross-platform C/C++ framework, see: https://providencesoftware.com/products/)
* Turbo Vision and OWL (on Windows, back in the C++ days, funny: https://github.com/magiblot/tvision)
* Delphi, C++ Builder (Borland, now https://en.wikipedia.org/wiki/Embarcadero_Technologies)
* Xcode/Objective-C, list goes on…

And Java gives us several options here:

* Swing (https://docs.oracle.com/javase/tutorial/uiswing/)
* SWT / JFace (https://eclipse.dev/eclipse/swt/, https://www.vogella.com/tutorials/EclipseJFace/article.html#definition-of-eclipse-jface)
* JavaFX

But these days, JavaFX combined with SceneBuilder (https://gluonhq.com/products/scene-builder/)
feels like the closest thing to drag-and-drop UI design since my time with Xcode. It’s fast to prototype,
visually intuitive, and still plays well with the JAVA ecosystem.

On Paper, Java Has It All, at least on paper:

* MIDI handling
* USB access
* REST APIs
* Native UI capabilities
* A rich set of libraries (and decades of developer muscle memory)