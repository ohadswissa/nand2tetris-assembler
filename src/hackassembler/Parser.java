package hackassembler;
import java.util.HashMap;
import java.io.*;
import java.lang.*;
import java.util.Scanner;

public class Parser {
    /**
     * for later use in the instructionType() method.
     */
    public enum INSTRUCTION_TYPE {
        A_INSTRUCTION,
        C_INSTRUCTION,
        L_INSTRUCTION;
    }
    public HashMap<Integer, String> map;
    int LineCounter = -1;
    /**
     * Constructor/initializer : creates a Parser and opens the source text file.
     *
     * @param filename is the input file.
     */
    public Parser(String filename) {
        this.map = new HashMap<>();
        int i = 0;//For mapping purposes.
        try {
            File readFile = new File(filename);
            Scanner scan = new Scanner(readFile);
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                //Ensures consistent formatting, which can be helpful when parsing the line further.
                if (!line.isEmpty() && !line.startsWith("//")) {
                    String result = line.replaceAll("\\s+", " ").trim();
                    //Handle a case there is comment inside the row structure.
                    int CommentIndex = result.indexOf("//");
                    if (CommentIndex != -1) {
                        result = result.substring(0, CommentIndex).trim(); //Takes only the part without the comment.
                    }
                    //If the line is as we need, we'll add it to the map.
                    if (!result.isEmpty()) {
                        map.put(i, result);
                        i++;
                    }
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    /**
     * Checks if there is more work to do.
     *
     * @return true if the map size is bigger the number of lines.
     */
    public boolean hasMoreLines() {
        return LineCounter + 1 < map.size();
    }

    /**
     * Called only if  hasMoreLines get back true, then it gets the next instruction and makes it the current instruction (string)
     */
    public void advance() {
        if (!hasMoreLines()) {
            return;
        }
        do {
            LineCounter++;
        } while (hasMoreLines() && map.get(LineCounter).trim().isEmpty());
    }
    /**
     * Returns the type of the current instruction as a constant.
     * A_INSTRUCTION for @xxx
     * C_INSTRUCTION for dest=comp;jump
     * L_INSTRUCTION for (label)
     */
    public INSTRUCTION_TYPE instructionType() {
        if (LineCounter < 0 || LineCounter >= map.size()) {
            throw new IllegalStateException("Invalid LineCounter state.");
        }
        String Command = map.get(LineCounter).trim();
        if (Command.startsWith("@")) {
            return INSTRUCTION_TYPE.A_INSTRUCTION;
        } else if (Command.startsWith("(") && Command.endsWith(")")) {
            return INSTRUCTION_TYPE.L_INSTRUCTION;
        }
        else return INSTRUCTION_TYPE.C_INSTRUCTION;
    }

    /**
     * Works on the correct instruction.
     *
     * @return the instruction's symbol (String) in a clean structure.
     */
    public String symbol() {
        INSTRUCTION_TYPE instruction = instructionType();
        String Command = map.get(LineCounter);//For manipulating purposes to return a clean command.
        if (instruction == INSTRUCTION_TYPE.A_INSTRUCTION) {
            return Command.replace("@", "");//'A' instruction without '@'.
        } else if (instruction == INSTRUCTION_TYPE.L_INSTRUCTION) {
            return Command.replace("(", "").replace(")", "");//'L' instruction without '(',')'.
        }
        return "";
    }

    /**
     * example : from "D=D+1;JLE" the method brings back 'D'.
     *
     * @return the instruction dest field (string).
     */
    public String dest() {
        String[] Destination = map.get(LineCounter).split("=");//Split's the string into an array divided by '='.
        if (Destination.length == 1) {
            return ""; //Which means there is no '=' sign or no destination value.
        }
        return Destination[0];
    }

    /**
     * example : from "D=D+1;JLE" the method brings back 'D+1'.
     *
     * @return the instruction comp field (string).
     */
    public String comp() {
        String Computer = map.get(LineCounter);
        int EqualIndex = Computer.indexOf("=");
        int SemicolonIndex = Computer.indexOf(";");
        String Partial;
        if (EqualIndex != -1) {
             Partial = Computer.substring(EqualIndex + 1);//Takes the String after the '=' sign.
        } else {
            Partial = Computer;//When there isn't a '=' sign.
        }
        if (SemicolonIndex != -1) {
            Partial = Partial.substring(0, SemicolonIndex);//Takes the part until the ';' sign.
        }
        return Partial.trim();
    }
    /**
     * example : from "D=D+1;JLE" the method brings back 'JLE'.
     * @return the instruction jump field (string).
     */
    public String jump(){
        String Jump = map.get(LineCounter);
        int SemicolonIndex = Jump.indexOf(";");
        if (SemicolonIndex == -1) {
            return "";
        }
        return Jump.substring(SemicolonIndex + 1).trim();//Takes the String after the ';' sign.
    }
}






