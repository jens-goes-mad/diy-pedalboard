---
title: "Prototyping software parts"
summary: "Prototyping software parts"
date: 2025-06-01
layout: article
# type: article
XType: design
draft: false
# tags: ["chatgpt", "humor"]
# categories: ["humor"]
weight: 101
# image: "D_HC_chapter.jpeg"
categoryWeight: "020.002"
---
## Prototyping?
*Of course, paper doesn’t run code — but it’s a good start.*

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
I start applying meaningful naming conventions, and structure things like I’m building something that’ll still
make sense three months from now. Why does this matter? Because:
* Good structure grows with the project
* Clean naming avoids “what the hell is this doing again?” later
* It sets the tone for maintainable, testable code from the beginning

> -- <cite>"Single responsibility rule, Inversion of Control, Abstraction is the key"</cite>

### Unit Tests and the War on Mocks
Next, I add unit tests to verify each service. This step always forces me to rethink the code's structure:
Is it too stateful? Too tightly coupled? Do I need to redesign it to make testing possible without cheating?

And now a personal opinion — brace yourself:<br>
If you're relying on mocks, something probably went wrong during the architecture phase.
Mocks often signal over-coupling or impure design. I aim for services that don’t need mocks to be tested —
and that means keeping them stateless and modular by design.

---
