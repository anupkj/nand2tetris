import BinaryConverter.BinaryConverter;
import FileUtils.FileUtils;
import Parser.Parser;
import SymbolTable.SymbolTable;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filepath = "/Users/anupkumarjaiswal/Desktop/nand2tetris/projects/06/pong/Pong.asm";
        String output =   "/Users/anupkumarjaiswal/Desktop/nand2tetris/projects/06/pong/Pong.hack";
        List<String> inputInstructions = FileUtils.readLinesFromFile(filepath);
        List<String> parsedLines = Parser.parse(inputInstructions);
        Map<String, Integer> symbolTableMap = SymbolTable.createSymbolTable(parsedLines);
        List<String> binaryInstructions = BinaryConverter.getBinaryLines(parsedLines,symbolTableMap);
        FileUtils.writeLinesFromFile(output,binaryInstructions);
    }
}