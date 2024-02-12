package SymbolTable;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    public static void addPredefinedValues(Map<String, Integer> symbolTableMap){
        symbolTableMap.put("SCREEN", 16384);
        symbolTableMap.put("KBD", 24576);
        symbolTableMap.put("SP", 0);
        symbolTableMap.put("LCL", 1);
        symbolTableMap.put("ARG", 2);
        symbolTableMap.put("THIS", 3);
        symbolTableMap.put("THAT", 4);

        for(int i = 0; i < 16; i++){
            symbolTableMap.put( 'R' + Integer.toString(i), i);
        }
    }

    public static Map<String, Integer> createSymbolTable(List<String> lines){
        Map<String, Integer> symbolTableDictionary = new HashMap<>();

        // add static constant in symbol table
        addPredefinedValues(symbolTableDictionary);

        // populate for label symbol
        populateLabelSymbols(symbolTableDictionary, lines);

        // populate for symbol variable
        populateVariableSymbols(symbolTableDictionary, lines);

        return symbolTableDictionary;
    }

    public  static boolean isContain(Integer num, Map<String, Integer> symbolTableMap){
        return symbolTableMap.containsValue(num);

    }

    public static Integer  findNextNumber(Map<String, Integer> symbolTableMap){
        Integer ptr = 16;
        while (isContain(ptr, symbolTableMap)){
            ptr++;
        }
        return ptr;
    }

    public static int tryParse(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return i;
        }
    }

    private static void populateVariableSymbols(Map<String, Integer> symbolTableDictionary, List<String> lines) {
        Integer ptr = 16;
        for(String line : lines) {
            if (line.charAt(0) == '@') {
                String variable = line.replace('@', ' ').trim();
                if(tryParse(variable,-1) != -1){
                    symbolTableDictionary.put(variable, tryParse(variable,-1));
                    continue;
                }
                Integer symbolValue= -1;
                if(symbolTableDictionary.containsKey(variable)){
                    symbolValue = symbolTableDictionary.get(variable);
                }
                else{
                    symbolValue = ptr++;
                }
                symbolTableDictionary.put(variable, symbolValue);
            }
        }
    }

    private static void populateLabelSymbols(Map<String, Integer> symbolTableDictionary,
                                             List<String> lines)
    {
        int currentLineNum = 0;
        for(String line : lines) {

            if (line.charAt(0) == '(') {
                symbolTableDictionary.put(line.replace('(', ' ')
                        .replace(')',' ').trim(), currentLineNum);
            } else {
                currentLineNum++;
            }
        }
    }
}
