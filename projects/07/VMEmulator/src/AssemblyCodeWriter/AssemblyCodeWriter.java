package AssemblyCodeWriter;

import constants.Constants;

import java.util.ArrayList;
import java.util.List;

/*
*
* push i ->
*
* LCL
* D=M
* @SP
* A=M
* M=D
* // increase stack pointer
* @SP
* M = M + 1
*
*
* pop i
* */

public class AssemblyCodeWriter {

    boolean checkIfPushOperation(String line){
        return line.contains(Constants.PUSH_OPERATION);
    }

    static void convertToAssembly( List<String> assemblyInstructions, String line){

        String[] split = line.split(" ");
        String instructionType = split[0];
        String vmMemorySegment = split.length > 2 ? split[1] : Constants.STACK_ARITHMETIC_OPERATION;
        String number = split.length > 2 ? split[2] : " ";
        switch (instructionType){
            case Constants.PUSH_OPERATION :
                 getPushInstruction(assemblyInstructions, vmMemorySegment, instructionType, number);
                 break;
            case Constants.POP_OPERATION:
                 getPopInstruction(assemblyInstructions, vmMemorySegment, instructionType, number);
                 break;
            default:
                 getStackArithmeticInstruction(assemblyInstructions,instructionType);
                 break;
        }
    }

    private static void getStackArithmeticInstruction(List<String> instructions,String instructionType) {
        String trueKeyword, resumeKeyword;
        switch (instructionType){
            case Constants.STACK_ADDITION:
                instructions.add("@SP");
                instructions.add("AM=M-1");
                instructions.add("D=M");
                instructions.add("A=A-1");
                instructions.add("M=M+D");
                break;
            case Constants.STACK_SUBTRACTION:
                instructions.add("@SP");
                instructions.add("AM=M-1");
                instructions.add("D=M");
                instructions.add("A=A-1");
                instructions.add("M=M-D");
                break;
            case Constants.STACK_NEGATION:
                instructions.add("@SP");
                instructions.add("A=M-1");
                instructions.add("M=-M");
                break;
            case Constants.STACK_EQUALITY_CHECK:
                // D = RAM[sp-2] - RAM[sp-1]
                instructions.add("@SP");
                instructions.add("A=M-1");
                instructions.add("D=-M"); // D = - RAM[sp-1]
                instructions.add("A=A-1");
                instructions.add("D=M+D"); //  D = RAM[sp-2] - RAM[sp-1]

                decrementStackPointer(instructions,2);

                trueKeyword = "TRUE_" + instructions.size();
                resumeKeyword = "RESUME_" + instructions.size();
                instructions.add("@"+trueKeyword);
                instructions.add("D;JEQ");
                addBooleanFalse(instructions,resumeKeyword);
                addBooleanTrue(instructions, trueKeyword, resumeKeyword);
                instructions.add("("+resumeKeyword + ")");
                break;
            case Constants.STACK_GREATER_THAN_CHECK:
                // D = RAM[sp-2] - RAM[sp-1]
                instructions.add("@SP");
                instructions.add("A=M-1");
                instructions.add("D=-M");
                instructions.add("A=A-1");
                instructions.add("D=M+D");

                decrementStackPointer(instructions,2);

                trueKeyword = "TRUE_" + instructions.size();
                resumeKeyword = "RESUME_" + instructions.size();
                instructions.add("@"+trueKeyword);
                instructions.add("D;JGT");
                addBooleanFalse(instructions,resumeKeyword);
                addBooleanTrue(instructions, trueKeyword, resumeKeyword);
                instructions.add("("+resumeKeyword + ")");
                break;
            case Constants.STACK_LESS_THAN_CHECK:
                // D = RAM[sp-2] - RAM[sp-1]
                instructions.add("@SP");
                instructions.add("A=M-1");
                instructions.add("D=-M");
                instructions.add("A=A-1");
                instructions.add("D=M+D");

                decrementStackPointer(instructions,2);

                trueKeyword = "TRUE_" + instructions.size();
                resumeKeyword = "RESUME_" + instructions.size();

                instructions.add("@"+trueKeyword);
                instructions.add("D;JLT"); // if D > 0 jump to TRUE keyword
                addBooleanFalse(instructions,resumeKeyword);
                addBooleanTrue(instructions, trueKeyword, resumeKeyword);
                instructions.add("("+resumeKeyword + ")");
                break;
            case Constants.STACK_LOGICAL_AND:
                // D = RAM[sp-2] & RAM[sp-1]
                instructions.add("@SP");
                instructions.add("AM=M-1");
                instructions.add("D=M");
                instructions.add("A=A-1");
                instructions.add("M=M&D");



                break;
            case Constants.STACK_LOGICAL_OR:
                // D = RAM[sp-2] | RAM[sp-1]
                instructions.add("@SP");
                instructions.add("AM=M-1");
                instructions.add("D=M");
                instructions.add("A=A-1");
                instructions.add("M=M|D");


                break;
            case Constants.STACK_LOGICAL_NOT:
                // D = -RAM[sp-1]
                instructions.add("@SP");
                instructions.add("A=M-1");
                instructions.add("M=!M");
                break;
        }
    }

    static void decrementStackPointer(List<String> instructions, Integer decrementUnit){
        instructions.add("@SP");
        instructions.add("M=M-1");
        if(decrementUnit == 2) instructions.add("M=M-1");
    }

    static void addBooleanTrue(List<String> instructions, String trueKeyword, String resumeKeyword){
        instructions.add("(" + trueKeyword + ")");
        instructions.add("@SP");
        instructions.add("A=M");
        instructions.add("M=-1");
        instructions.add("@SP");
        instructions.add("M=M+1");
        instructions.add("@" + resumeKeyword);
        instructions.add("0;JMP");
    }

    static void addBooleanFalse(List<String> instructions, String resumeKeyword){
        instructions.add("@SP");
        instructions.add("A=M");
        instructions.add("M=0");
        instructions.add("@SP");
        instructions.add("M=M+1");
        instructions.add("@" + resumeKeyword);
        instructions.add("0;JMP");
    }

    static void getPopInstruction(List<String> instructions, String vmMemorySegment,
                            String instructionType, String number)
    {
        // this part calculate base address 'D' of the ram where data is to be written
        instructions.add("@" + number);
        instructions.add("D=A");
        switch (vmMemorySegment){
            case Constants.LOCAL :
                instructions.add("@LCL");
                instructions.add("D=D+M");
                break;
            case Constants.ARGUMENT:
                instructions.add("@ARG");
                instructions.add("D=D+M");
                break;
            case Constants.THIS:
                instructions.add("@THIS");
                instructions.add("D=D+M");
                break;
            case Constants.THAT:
                instructions.add("@THAT");
                instructions.add("D=D+M");
                break;
            case Constants.TEMP:
                instructions.add("@5");
                instructions.add("D=D+A");
                break;
            case Constants.STATIC:
                instructions.add("@16");
                instructions.add("D=D+A");
                break;
            case Constants.POINTER:
                instructions.remove(instructions.size()-1);
                if("0".equals(number)){
                    instructions.add("@THIS");
                    instructions.add("D=A");
                }
                else{
                    instructions.add("@THAT");
                    instructions.add("D=A");
                }
                break;
        }

        // this part assign ram[D] = Ram[SP-1]
        instructions.add("@SP");
        instructions.add("//decrementing stack pointer");
        instructions.add("M=M-1");
        instructions.add("A=M");
        instructions.add("//adding val to d");
        instructions.add("D=D+M");
        instructions.add("A=D-M");
        instructions.add("M=D-A");
    }

    static void getPushInstruction(List<String> instructions, String vmMemorySegment,
                            String instructionType, String number)
    {
        instructions.add("@" + number);
        instructions.add("D=A");
        switch (vmMemorySegment){
            case Constants.LOCAL :
                instructions.add("@LCL");
                instructions.add("D=D+M");
                break;
            case Constants.ARGUMENT:
                instructions.add("@ARG");
                instructions.add("D=D+M");
                break;
            case Constants.THIS:
                instructions.add("@THIS");
                instructions.add("D=D+M");
                break;
            case Constants.THAT:
                instructions.add("@THAT");
                instructions.add("D=D+M");
                break;
            case Constants.CONSTANT:
                addToStackFromDRegister(instructions);
                stackPointIncrement(instructions);
                return;
            case Constants.TEMP:
                instructions.add("@5");
                instructions.add("D=D+A");
                break;
            case Constants.STATIC:
                instructions.add("@16");
                instructions.add("D=D+A");
                break;
            case Constants.POINTER:
                if("0".equals(number)){
                    instructions.add("@THIS");
                    instructions.add("D=M");
                }
                else{
                    instructions.add("@THAT");
                    instructions.add("D=M");
                }
                addToStackFromDRegister(instructions);
                stackPointIncrement(instructions);
                return;
        }
        instructions.add("A=D");
        instructions.add("D=M");
        addToStackFromDRegister(instructions);
        stackPointIncrement(instructions);
    }
    static void addToStackFromDRegister(List<String> instructions){
        instructions.add("// assign value to  stack.");
        instructions.add("@SP");
        instructions.add("A=M");
        instructions.add("M=D");
    }

    static void stackPointIncrement(List<String> instructions){
        instructions.add("// stack pointer add.");
        instructions.add("@SP");
        instructions.add("M=M+1");
    }

    public static List<String> convertToAssembly(List<String> lines){
        List<String> assemblyInstructions = new ArrayList<>();
        for(String line : lines){
            assemblyInstructions.add("// INSTRUCTION :- " + line);
            convertToAssembly(assemblyInstructions, line);
        }
        return assemblyInstructions;
    }
}
