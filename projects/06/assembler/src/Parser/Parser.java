package Parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {


    private static List<String> RemoveWhiteSpace(List<String> lines){
        List<String> formattedLines = new ArrayList<>();
        for(String line : lines){
            if(line.trim().length() == 0 || line.trim().charAt(0)=='/'){
                continue;
            }
            StringBuilder stringBuilder = new StringBuilder();
            line = line.trim();
            for(char c : line.toString().toCharArray()){
                if(c == '/'){
                    break;
                }
                else{
                    stringBuilder.append(c);
                }
            }
            if(stringBuilder.length() > 0) formattedLines.add(stringBuilder.toString());
        }
        return formattedLines;
    }
    public static List<String> parse(List<String> lines){
        return RemoveWhiteSpace(lines);
    }
}
