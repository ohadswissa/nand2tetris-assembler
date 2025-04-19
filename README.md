# Hack Assembler – Nand2Tetris Project 6

This project is part of the [Nand2Tetris](https://www.nand2tetris.org/) course.  
It implements a full assembler in Java that translates Hack Assembly language (`.asm` files) into machine-level binary code (`.hack` files).

---

## 🛠 Features

- Parses A-instructions and C-instructions
- Handles labels and symbolic variables
- Outputs correct 16-bit binary code
- Includes unit tests for key components
- Fully written from scratch in Java (no external libraries)

---

## 📂 Folder Structure

```
src/                → Main assembler logic (Parser, Code, SymbolTable, etc.)  
test/               → JUnit test files for core components  
bin/                → Compiled `.class` files (Java build output)  
src/test/resources  → Sample input/output programs for testing  
pom.xml             → Maven project file
```

---

## ▶️ How to Run

javac -d bin src/hackassembler/*.java
java -cp bin hackassembler.HackAssembler input.asm

## 🧪 Sample Input

@2
D=A
@3
D=D+A
@0
M=D

## ➡️ Output

0000000000000010
1110110000010000
0000000000000011
1110000010010000
0000000000000000
1110001100001000

## 👨‍💻 Author
Ohad Swissa
Honors Student - Computer Science & Entrepreneurship
Ex-IDF Special Forces Major | Problem Solver  
[LinkedIn](https://www.linkedin.com/in/ohad-swissa-54728a2a6)


