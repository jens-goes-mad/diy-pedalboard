# Target setup

<cite>Sometimes a Division by Zero Is Better Than an Infinite Loop</cite>

Coming from the macOS world, I left Windows behind nearly 15 years ago.
Why? Because for most of my work — especially with Java — the underlying machine became irrelevant.
Macs gave me rock-solid uptime (150+ days without a reboot in my home office), and that was good enough.
As a hobby musician, I can say this too: macOS is rock solid. It’s reliable, quiet, and great for 
creative work. Even my iMac 24" runs without issues since 10 years now, and is fast enough still for my development. 

## Operating System

Every system needs a soul, the operating system, whether you're working with a humble Raspberry Pi
or a beastly 24-core Intel workstation. Without an operating system, your hardware is just an expensive paperweight.

And that’s where installers enter the game.

It doesn’t matter if you’re prepping a live synth rig, building a MIDI pedal board brain,
or just provisioning your dev machine — at some point, you’re clicking through setup screens.

## Installers, Scripts… and the Pain of Repetition

Sure, you can go the classic route:

- Download a Debian or Ubuntu live "CD"
- Boot your target device from USB
- Click your way through the installer
- Use dd at the end to back up your image
- Use that backup as your golden template

Let’s face it — that’s neither fun nor exciting.

And when it comes to provisioning, you have a whole toolbox to choose from, for example: Ansible, Chef, Bash scripting...
But wait, we live in a Docker-centric world now.
So why not use Docker — a container system designed for repeatability and control — to build something even cooler?
Let the container build the ISO for your target platform!

And here we go.

## Docker ISO build

When it came to creating our own Linux ISO/IMGs, ChatGPT and I agreed on a clear winner: Docker.
Think about it: you get all the flexibility and isolation of Docker, and the result is a bootable ISO image
custom-tailored to your device — created programmatically.

- Reproducibility
Docker ensures that our builds run identically across all environments —
whether on macOS, Linux, or inside CI pipelines. No more “it worked on my machine” excuses.
- Isolation
Low-level image creation tools like losetup, parted, and kpartx can get messy on a shared system.
Docker gives us a clean, disposable build environment every time — no host pollution, no conflicts.
- Cross-Architecture Flexibility
By embedding QEMU in our Docker setup, we can build for multiple architectures
(e.g. aarch64, x86_64) from the same host.
- One setup. Multiple targets.
- Dependency Encapsulation
Tools like debootstrap, grub, xorriso, and squashfs-tools don’t need to clutter your host system.
Docker containers handle all of them, keeping your workstation clean and your build environment self-contained.
- Host-Agnostic Builds
Whether we’re on Ubuntu, macOS (via UTM), a CI pipeline, or a cloud VM,
the Dockerized build behaves the same.

## The Chicken-and-Egg Problem 🐣

But of course — no solution comes without a catch.

Docker on macOS (especially via Docker Desktop) doesn’t support all the low-level system calls needed
for building ISO images. You can’t just mount loop devices or run losetup directly from a macOS container.
So what now? Well, that gave us a classic chicken-and-egg problem.
To use Docker effectively for ISO building, we first need a Linux environment.
Which means setting up a virtualized Linux VM on macOS — which then becomes the actual host for our build system.

In other words: We tried to avoid clicking through installers, but… ironically we had to.

Once the Linux VM is running, though, it becomes a reliable, headless build farm for our Dockerized workflow.
No dual-boot. No clutter. Just a clean Linux box hiding inside macOS, doing the dirty work.

VM Setup: Harder Than It Should’ve Been
Of course, setting up the VM was more complicated than expected.
- VirtualBox didn’t work at all — likely due to my slightly ancient macOS Monterey (12.7.6).
- VMWare Fusion? Not even an option.
The VMware website seems to have been designed by a committee of marketing consultants from UI hell.
Crucial details are buried, and the download page completely ignores your OS and browser.
The result? I downloaded Version 13, which was not installable on my system.
And could I find Version 12 anywhere? Of course not.
- UTM — The Unexpected Hero

### UTM — The Unexpected Hero

With hopes low and patience drained, I turned to UTM (https://mac.getutm.app) — and surprisingly, it just worked.

- Easy setup.
- Clean interface.
- macOS-friendly.
- No jumping through flaming hoops of license keys or version mismatch warnings.

UTM saved the day — quietly and without drama.

