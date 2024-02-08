// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen
// by writing 'black' in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen by writing
// 'white' in every pixel;
// the screen should remain fully clear as long as no key is pressed.

//// Replace this comment with your code.
(CHECK)

@KBD
D=M


@WHITE
D;JLE

@BLACK
0;JMP




(WHITE)
    @i
    M=0
    (LOOPA)
    @8192
    D=A
    @i
    D=M-D
    @CHECK
    D;JEQ

    @i
    D=M;
    @SCREEN
    A=A+D;
    M=0
    @i
    M=M+1;

    @LOOPA
    0;JMP








(BLACK)
    @i
    M=0
    (LOOPB)
    @8192
    D=A
    @i
    D=M-D
    @CHECK
    D;JEQ

    @i
    D=M;
    @SCREEN
    A=A+D;
    M=-1
    @i
    M=M+1;

    @LOOPB
    0;JMP

