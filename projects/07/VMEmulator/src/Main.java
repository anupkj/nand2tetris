import AssemblyCodeWriter.AssemblyCodeWriter;
import FileUtils.FileUtils;
import Parser.Parser;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filepath = "/Users/anupkumarjaiswal/Desktop/nand2tetris/projects/07/StackArithmetic/StackTest/StackTest.vm";
        String output =   "/Users/anupkumarjaiswal/Desktop/nand2tetris/projects/07/StackArithmetic/StackTest/StackTest.asm";
        List<String> inputInstructions = FileUtils.readLinesFromFile(filepath);
        List<String> parsedLines = Parser.parse(inputInstructions);
        List<String> assemblyInstructions = AssemblyCodeWriter.convertToAssembly(parsedLines);
        FileUtils.writeLinesFromFile(output,assemblyInstructions);
        System.out.println("Hello world!");
    }
}