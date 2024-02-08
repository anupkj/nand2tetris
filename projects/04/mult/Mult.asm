
// find R0
@R0
D=M

// set i = r[0]
@i
M=D

// set R2 = 0
@R2
M=0

(LOOP)
@i
M=M-1  // i = i-1
D=M;
@END
D;JLT

@R1
D=M

@R2
M=M+D // r2 = r2 + r1
@LOOP
0;JMP


(END)
@END
0;JMP
