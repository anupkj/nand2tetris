package BinaryConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinaryConverter {


    static boolean isAInstruction(String line){
        return line.charAt(0) == '@';
    }

    static boolean isALabelSymbolInstruction(String line){
        return line.charAt(0) == '(';
    }


    static String getBinaryInstructionFromComp(String computation){
        Map<String, String> compBinaryTable = new HashMap<>();
        populateCompBinaryInstruction(compBinaryTable);
        boolean isMPresent = computation.indexOf('M') != -1;
        StringBuilder sb = new StringBuilder();
        if(!isMPresent){
            // contains A
            sb.append('0');
            sb.append(compBinaryTable.get(computation));
        }
        else{
            // contains M
            sb.append('1');
            sb.append(compBinaryTable.get(computation.replace('M','A')));
        }
        if(sb.toString().contains("null")){
            System.out.println(computation);
        }
        return sb.toString();
    }


    static String getBinaryInstructionFromDest(String destination){
        if(destination == null) return "000";
        StringBuilder sb = new StringBuilder();
        if(destination.contains("A")){
            sb.append('1');
        }
        else{
            sb.append('0');
        }
        if(destination.contains("D")){
            sb.append('1');
        }
        else{
            sb.append('0');
        }
        if(destination.contains("M")){
            sb.append('1');
        }
        else{
            sb.append('0');
        }
        return sb.toString();
    }
    static String getBinaryInstructionFromJump(String jump){
        if(jump == null) return "000";
        Map<String, String> jumpBinaryTable = new HashMap<>();
        populateJumpBinaryInstruction(jumpBinaryTable);
        return jumpBinaryTable.get(jump);
    }
    public static List<String> getBinaryLines(List<String> lines, Map<String, Integer> symbolTable){
        List<String> binaryLines = new ArrayList<>();
        for(String line : lines){
            if(isAInstruction(line)){
                Integer address = symbolTable.get(line.replace('@', ' ').trim());
                String binaryIns = Integer.toBinaryString(address);
                binaryLines.add("0".repeat(16 - binaryIns.length()) + binaryIns);
            }
            else{
                // if a label symbol, then continue
                if(isALabelSymbolInstruction(line)) continue;

                Integer equalIndex = line.indexOf('=');
                Integer semiColonIndex = line.indexOf(';');
                String destination = null, comp = null, jump = null;
                if(equalIndex != -1) destination = line.substring(0,equalIndex);
                if(semiColonIndex != -1) jump = line.substring(semiColonIndex+1);
                comp = line.substring(equalIndex == -1 ? 0 : equalIndex+1, semiColonIndex == -1 ? line.length() : semiColonIndex);
                binaryLines.add("111" +getBinaryInstructionFromComp(comp)
                                +getBinaryInstructionFromDest(destination)
                                +getBinaryInstructionFromJump(jump));
            }
        }
        return binaryLines;
    }



    static void populateCompBinaryInstruction(Map<String, String> compBinaryTable){
        compBinaryTable.put("!A" , "110001");
        compBinaryTable.put("A" , "110000");
        compBinaryTable.put("!D" , "001101");
        compBinaryTable.put("-1" , "111010");
        compBinaryTable.put("D" , "001100");
        compBinaryTable.put("A-1" , "110010");
        compBinaryTable.put("A+1" , "110111");
        compBinaryTable.put("D|A" , "010101");
        compBinaryTable.put("D-1" , "001110");
        compBinaryTable.put("D+1" , "011111");
        compBinaryTable.put("0" , "101010");
        compBinaryTable.put("1" , "111111");
        compBinaryTable.put("-A" , "110011");
        compBinaryTable.put("-D" , "001111");
        compBinaryTable.put("A-D" , "000111");
        compBinaryTable.put("D-A" , "010011");
        compBinaryTable.put("D+A" , "000010");
        compBinaryTable.put("D&A" , "000000");
    }


    static void populateJumpBinaryInstruction(Map<String, String> jumpBinaryTable){
        jumpBinaryTable.put("JGT" , "001");
        jumpBinaryTable.put("JEQ", "010");
        jumpBinaryTable.put("JGE", "011");
        jumpBinaryTable.put("JLT", "100");
        jumpBinaryTable.put("JNE", "101");
        jumpBinaryTable.put("JLE", "110");
        jumpBinaryTable.put("JMP", "111");
    }

}
