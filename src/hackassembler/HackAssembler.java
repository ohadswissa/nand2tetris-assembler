package hackassembler;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class HackAssembler {
    /**
     * Converts an integer to a 16-bit binary string for fitting to the wanted hack file.
     *
     * @param value the integer to convert.
     * @return a 16-bit binary string
     */
    public static String toBinary(int value) {
        return String.format("%16s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    /**
     * Checks if the input String is a numeric value.
     *
     * @param s is the string to check.
     * @return true if the input is numeric or false otherwise.
     */
    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void main(String[] args) {
        ///////////////
        //   part 1  //
        ///////////////
        //First we will initialize a symbol table with the relevant basic values. also, we will check the input is as needed.
        if (args.length != 1) {
            System.out.println("Please provide exactly one .asm file to assemble");
            return;
        }
        SymbolTable symbolTable = new SymbolTable();
        for (int i = 0; i <= 15; i++) {
            symbolTable.addEntry("R" + i, i);//R1 to R15 initialize.
        }
        //We'll add the predefined symbols.
        symbolTable.addEntry("SCREEN", 16384);
        symbolTable.addEntry("KBD", 24576);
        symbolTable.addEntry("SP", 0);
        symbolTable.addEntry("LCL", 1);
        symbolTable.addEntry("ARG", 2);
        symbolTable.addEntry("THIS", 3);
        symbolTable.addEntry("THAT", 4);
        ///////////////
        //   part 2  //
        ///////////////
        //We'll use the args[0] input as the input file for parsing.
        String inputFileName = args[0];
        //the output we want is a hack file.
        String outputFileName = inputFileName.replace(".asm", ".hack");
        //We'll initialize Parser and Code using the constructors we built.
        Parser parser = new Parser(inputFileName);
        Code code = new Code();
        //Add labels using the Parser
        int instruction = 0;
        while (parser.hasMoreLines()) {
            parser.advance();
            if (parser.instructionType() == Parser.INSTRUCTION_TYPE.L_INSTRUCTION) {
                String instructionlabel = parser.symbol();
                //Checks if the label is already at the symbol table.
                if (!symbolTable.contains(instructionlabel)) {
                    symbolTable.addEntry(instructionlabel, instruction);
                }
            } else if (parser.instructionType() == Parser.INSTRUCTION_TYPE.A_INSTRUCTION ||
                    parser.instructionType() == Parser.INSTRUCTION_TYPE.C_INSTRUCTION) {
                // Increment only for executable instructions.
                instruction++;
            }
        }
            ///////////////
            //   part 3  //
            ///////////////
            //We'll go over the file again to handle A and C instructions.
            parser = new Parser(inputFileName);
            int availablememory = 16; //Places after the initialize table.
            //Start filiing the output file using write methods
            try (PrintWriter writer = new PrintWriter(outputFileName)) {
                while (parser.hasMoreLines()) {
                    parser.advance();
                    //Checks for A instructions.
                    if (parser.instructionType() == Parser.INSTRUCTION_TYPE.A_INSTRUCTION) {
                        String instructionsymbol = parser.symbol();
                        int place;
                        //Checks for the place we "should go".
                        if (isNumber(instructionsymbol)) {
                            place = Integer.parseInt(instructionsymbol);
                        } else {
                            if (!symbolTable.contains(instructionsymbol)) {
                                symbolTable.addEntry(instructionsymbol, availablememory); //Add to the symbol table if it still doesn't there.
                                availablememory++;
                            }
                            place = symbolTable.getAddress(instructionsymbol); //As we know, A-Instruction mainly uses numbers
                            // so we are considering it as "simple numbers" and we just switch them to binary with the method we built.
                        }
                        writer.println(toBinary(place));
                        //Last we handle the C-instruction abd use the Parser methods.
                    } else if (parser.instructionType() == Parser.INSTRUCTION_TYPE.C_INSTRUCTION) {
                        String destination = parser.dest();
                        String computer = parser.comp();
                        String jump = parser.jump();
                        String combineinstruction = "111" + code.comp(computer) + code.dest(destination) + code.jump(jump);
                        writer.println(combineinstruction);//Adds the C instruction as a 16-bit.
                    }

                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found");
            }
        }

}



