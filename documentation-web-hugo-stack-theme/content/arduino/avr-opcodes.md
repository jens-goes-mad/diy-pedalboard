---
title: ATmega328P OpCodes
layout: article
draft: false
summary: "Overview about AVR instructions and Opcodes"
weight: 500
categoryWeight: "300.100"
image: "PB_LDI_SUB_CALL_2.jpg"
tags: ["hardware", "microcontroller", "ATmega328P", "Arduino"]
categories: ["arduino"]
---
## AVR OpCodes

Here’s a (hopefully) complete, detailed list of the instructions supported by the ATmega328P AVR core, 
including mnemonics, opcode formats, and typical cycle counts. This will be a thorough reference-style list 
grouped into logical categories for clarity.

- Total: ~130 instructions, depending on how you count variants (like immediate, indirect, etc.).
- Cycles: Most are single cycle, with some instructions (like calls, loads from program memory) requiring up to 3–4 cycles.
- Opcodes: AVR instructions have fixed 16-bit opcodes, some multi-word (like CALL, JMP).

### Arithmetic and Logic Instructions

| Instruction | Description               | Cycles | Opcode format example |
| ----------- | ------------------------- | ------ | --------------------- |
| `ADD`       | Add without carry         | 1      | `0000 11rd dddd rrrr` |
| `ADC`       | Add with carry            | 1      | `0001 11rd dddd rrrr` |
| `SUB`       | Subtract without carry    | 1      | `0001 10rd dddd rrrr` |
| `SBC`       | Subtract with carry       | 1      | `0000 10rd dddd rrrr` |
| `AND`       | Logical AND               | 1      | `0010 00rd dddd rrrr` |
| `OR`        | Logical OR                | 1      | `0010 10rd dddd rrrr` |
| `EOR`       | Exclusive OR              | 1      | `0010 01rd dddd rrrr` |
| `COM`       | One’s complement          | 1      | `1001 010d dddd 0000` |
| `NEG`       | Two’s complement          | 1      | `1001 010d dddd 0001` |
| `INC`       | Increment                 | 1      | `1001 010d dddd 0011` |
| `DEC`       | Decrement                 | 1      | `1001 010d dddd 1010` |
| `TST`       | Test for zero or negative | 1      | Same as `AND Rd, Rd`  |
| `CLR`       | Clear register (Rd = 0)   | 1      | Same as `EOR Rd, Rd`  |
| `SER`       | Set register to 0xFF      | 1      | `1110 KKKK dddd KKKK` |
| `CP`        | Compare                   | 1      | `0001 01rd dddd rrrr` |
| `CPC`       | Compare with carry        | 1      | `0000 01rd dddd rrrr` |
| `CPSE`      | Compare and skip if equal | 1/2    | `0001 00rd dddd rrrr` |
| `MOV`       | Copy register             | 1      | `0010 11rd dddd rrrr` |
| `MOVW`      | Copy register pair        | 1      | `0000 0001 dddd rrrr` |
| `LSR`       | Logical shift right       | 1      | `1001 010d dddd 0110` |
| `ASR`       | Arithmetic shift right    | 1      | `1001 010d dddd 0101` |
| `ROR`       | Rotate right              | 1      | `1001 010d dddd 0111` |
| `SWAP`      | Swap nibbles in register  | 1      | `1001 010d dddd 0010` |

### Branch and Control Instructions

| Instruction | Description                     | Cycles | Opcode format example               |
| ----------- | ------------------------------- | ------ | ----------------------------------- |
| `RJMP`      | Relative jump                   | 2      | `1100 kkkk kkkk kkkk`               |
| `IJMP`      | Indirect jump via Z             | 2      | `1001 0100 0000 1001`               |
| `JMP`       | Absolute jump                   | 3      | `1001 010k kkkk 110k` + 16-bit addr |
| `RCALL`     | Relative call                   | 3      | `1101 kkkk kkkk kkkk`               |
| `ICALL`     | Indirect call via Z             | 3      | `1001 0100 0001 1001`               |
| `CALL`      | Absolute call                   | 4      | `1001 010k kkkk 111k` + 16-bit addr |
| `RET`       | Return from subroutine          | 4      | `1001 0101 0000 1000`               |
| `RETI`      | Return from interrupt           | 4      | `1001 0101 0001 1000`               |
| `CPSE`      | Compare and skip if equal       | 1/2    | `0001 00rd dddd rrrr`               |
| `SBRC`      | Skip if bit in register cleared | 1/2    | `1111 110r rrrr 0bbb`               |
| `SBRS`      | Skip if bit in register set     | 1/2    | `1111 111r rrrr 0bbb`               |
| `SBIC`      | Skip if bit in I/O cleared      | 1/2    | `1001 1000 AAAA Abbb`               |
| `SBIS`      | Skip if bit in I/O set          | 1/2    | `1001 1010 AAAA Abbb`               |
| `SBI`       | Set bit in I/O register         | 2      | `1001 1010 AAAA Abbb`               |
| `CBI`       | Clear bit in I/O register       | 2      | `1001 1000 AAAA Abbb`               |
| `BRBS`      | Branch if status bit set        | 1/2    | `1111 00kk kkkk ksss`               |
| `BRBC`      | Branch if status bit cleared    | 1/2    | `1111 01kk kkkk ksss`               |

### Load and Store Instructions

| Instruction | Description                      | Cycles | Opcode format example               |
| ----------- | -------------------------------- | ------ | ----------------------------------- |
| `LD`        | Load indirect from SRAM          | 2      | `1001 000d dddd 1100` (X)           |
| `LDS`       | Load direct from data space      | 2      | `1001 000d dddd 0000` + 16-bit addr |
| `LDD`       | Load indirect with displacement  | 2      | `10q0 qq0d dddd 1qqq`               |
| `ST`        | Store indirect to SRAM           | 2      | `1001 001r rrrr 1100` (X)           |
| `STS`       | Store direct to data space       | 2      | `1001 001d dddd 0000` + 16-bit addr |
| `STD`       | Store indirect with displacement | 2      | `10q0 qq1r rrrr 1qqq`               |
| `LDI`       | Load immediate                   | 1      | `1110 KKKK dddd KKKK`               |
| `MOV`       | Move between registers           | 1      | `0010 11rd dddd rrrr`               |
| `PUSH`      | Push to stack                    | 2      | `1001 001d dddd 1111`               |
| `POP`       | Pop from stack                   | 2      | `1001 000d dddd 1111`               |
| `LPM`       | Load from program memory         | 3      | `1001 0101 dddd 0100`               |
| `ELPM`      | Extended load program memory     | 3      | `1001 0101 dddd 0110`               |
| `SPM`       | Store to program memory          | 4      | `1001 0101 1110 1000`               |

### Bit and Bit-Test Instructions

| Instruction | Description                   | Cycles | Opcode format example |
| ----------- | ----------------------------- | ------ | --------------------- |
| `SBR`       | Set bit(s) in register        | 1      | `0110 KKKK dddd KKKK` |
| `CBR`       | Clear bit(s) in register      | 1      | `0111 KKKK dddd KKKK` |
| `BST`       | Store bit from register to T  | 1      | `1111 101d dddd 0bbb` |
| `BLD`       | Load bit from T to register   | 1      | `1111 100d dddd 0bbb` |
| `SEC`       | Set carry flag                | 1      | `1001 0100 0000 1000` |
| `CLC`       | Clear carry flag              | 1      | `1001 0100 0000 1001` |
| `SEN`       | Set negative flag             | 1      | `1001 0100 0010 0000` |
| `CLN`       | Clear negative flag           | 1      | `1001 0100 0010 0001` |
| `SEZ`       | Set zero flag                 | 1      | `1001 0100 0001 0000` |
| `CLZ`       | Clear zero flag               | 1      | `1001 0100 0001 0001` |
| `SEI`       | Set global interrupt enable   | 1      | `1001 0100 0111 1000` |
| `CLI`       | Clear global interrupt enable | 1      | `1001 0100 0111 1001` |

### Miscellaneous Instructions

| Instruction | Description              | Cycles | Opcode format example                      |
| ----------- | ------------------------ | ------ | ------------------------------------------ |
| `NOP`       | No operation             | 1      | `0000 0000 0000 0000`                      |
| `SLEEP`     | Enter sleep mode         | 1      | `1001 0101 1000 1000`                      |
| `WDR`       | Watchdog reset           | 1      | `1001 0101 1010 1000`                      |
| `BREAK`     | Breakpoint (debug)       | 1      | `1001 0101 1001 1000`                      |
| `DES`       | Data Encryption Standard | 1      | `1001 0100 KKKK 1011` *(optional support)* |
