package hackassembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import hackassembler.Parser.INSTRUCTION_TYPE;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
public class ParserTest {
    @Test
    void testParserConstructor() {
        // Replace with the path to your test.asm file
        String testFilePath = "test.asm";
        // Create the Parser object
        Parser parser = new Parser("src/test/resources/simple.asm");
        // Expected parsed map
        HashMap<Integer, String> expectedMap = new HashMap<>();
        expectedMap.put(0, "@2");
        expectedMap.put(1, "D=A");
        expectedMap.put(2, "@3");
        expectedMap.put(3, "D=D+A");
        expectedMap.put(4, "@0");
        expectedMap.put(5, "M=D");

        // Assert that the map in the Parser matches the expected map
        assertEquals(expectedMap, parser.map, "Parsed map should match the expected map.");
    }

    @Test
    public void testAdvance(){
    Parser parser = new Parser("src/test/resources/test.asm");

    // Call advance to move to the first line
    parser.advance();
    assertEquals("@2", parser.map.get(parser.LineCounter), "Initial line should be '@2'");
    assertEquals(Parser.INSTRUCTION_TYPE.A_INSTRUCTION, parser.instructionType(), "First instruction should be A_INSTRUCTION");

    // Advance to line 1
    parser.advance();
    assertEquals("D=A", parser.map.get(parser.LineCounter), "After advancing, the next line should be 'D=A'");
    assertEquals(Parser.INSTRUCTION_TYPE.C_INSTRUCTION, parser.instructionType(), "Instruction type should be C_INSTRUCTION");

    // Advance to line 2
    parser.advance();
    assertEquals("@3", parser.map.get(parser.LineCounter), "Next line should be '@3'");
    assertEquals(Parser.INSTRUCTION_TYPE.A_INSTRUCTION, parser.instructionType(), "Instruction type should be A_INSTRUCTION");

    // Advance to line 3
    parser.advance();
    assertEquals("(LOOP)", parser.map.get(parser.LineCounter), "Next line should be '(LOOP)'");
    assertEquals(Parser.INSTRUCTION_TYPE.L_INSTRUCTION, parser.instructionType(), "Instruction type should be L_INSTRUCTION");

    // Advance to line 4
    parser.advance();
    assertEquals("0;JMP", parser.map.get(parser.LineCounter), "Next line should be '0;JMP'");
    assertEquals(Parser.INSTRUCTION_TYPE.C_INSTRUCTION, parser.instructionType(), "Instruction type should be C_INSTRUCTION");

    // Check that `hasMoreLines()` is false after the last line
    assertFalse(parser.hasMoreLines(), "No more lines should remain after the last instruction");
}

    @Test
    public void testInstructionType() {
        Parser parser = new Parser("src/test/resources/test.asm");
        // Arrange: Initialize parser with the test file
        // Act and Assert for each line
        parser.advance();
        assertEquals(Parser.INSTRUCTION_TYPE.A_INSTRUCTION, parser.instructionType(), "Expected A_INSTRUCTION for @2");

        parser.advance();
        assertEquals(Parser.INSTRUCTION_TYPE.C_INSTRUCTION, parser.instructionType(), "Expected C_INSTRUCTION for D=A");

        parser.advance();
        assertEquals(Parser.INSTRUCTION_TYPE.A_INSTRUCTION, parser.instructionType(), "Expected A_INSTRUCTION for @3");

        parser.advance();
        assertEquals(Parser.INSTRUCTION_TYPE.L_INSTRUCTION, parser.instructionType(), "Expected L_INSTRUCTION for (LOOP)");

        parser.advance();
        assertEquals(Parser.INSTRUCTION_TYPE.C_INSTRUCTION, parser.instructionType(), "Expected C_INSTRUCTION for 0;JMP");
    }

    @Test
    void testConstructorAdvanceAndInstructionType() {
        Parser parser = new Parser("src/test/resources/test.asm");
        System.out.println("Testing file: src/test/resources/test.asm");

        while (parser.hasMoreLines()) {
            parser.advance();
            String command = parser.map.get(parser.LineCounter);
            System.out.println("Line " + parser.LineCounter + ": " + command);

            INSTRUCTION_TYPE type = parser.instructionType();
            System.out.println("Instruction Type: " + type);
        }

        System.out.println("Test completed successfully.");
    }

    @Test
    public void testSymbol() {
        Parser parser = new Parser("src/test/resources/test.asm");
        parser.advance(); // Line 0: @2
        assertEquals("2", parser.symbol(), "Expected symbol '2' for A_INSTRUCTION");

        parser.advance(); // Line 1: D=A (C_INSTRUCTION)
        assertEquals("", parser.symbol(), "Expected empty symbol for C_INSTRUCTION");

        parser.advance(); // Line 2: @3
        assertEquals("3", parser.symbol(), "Expected symbol '3' for A_INSTRUCTION");

        parser.advance(); // Line 3: (LOOP)
        assertEquals("LOOP", parser.symbol(), "Expected symbol 'LOOP' for L_INSTRUCTION");

        parser.advance(); // Line 4: 0;JMP (C_INSTRUCTION)
        assertEquals("", parser.symbol(), "Expected empty symbol for C_INSTRUCTION");
    }

    @Test
    public void testDestCompJump() {
        System.out.println("Testing file: src/test/resources/test.asm");

        // Initialize the parser
        Parser parser = new Parser("src/test/resources/test.asm");

        // Expected results for dest, comp, and jump
        String[][] expectedResults = {
                {"", "2", ""},    // @2
                {"D", "A", ""},   // D=A
                {"", "3", ""},    // @3
                {"", "", ""},     // (LOOP) - Labels are ignored here
                {"", "0", "JMP"}  // 0;JMP
        };

        // Iterate over each line in the test file and validate dest, comp, jump
        int index = 0;
        while (parser.hasMoreLines()) {
            parser.advance();
            System.out.println("Line " + parser.LineCounter + ": " + parser.map.get(parser.LineCounter));

            String expectedDest = expectedResults[index][0];
            String expectedComp = expectedResults[index][1];
            String expectedJump = expectedResults[index][2];

            if (parser.instructionType() == Parser.INSTRUCTION_TYPE.C_INSTRUCTION) {
                assertEquals(expectedDest, parser.dest(), "Expected dest: " + expectedDest);
                assertEquals(expectedComp, parser.comp(), "Expected comp: " + expectedComp);
                assertEquals(expectedJump, parser.jump(), "Expected jump: " + expectedJump);
            } else {
                System.out.println("Skipping non-C_INSTRUCTION at Line " + parser.LineCounter);
            }

            index++;
        }

        System.out.println("Test completed successfully.");
    }
    @Test
    public void testParserFullFlow() {
        // Given a test assembly file
        String asmFilePath = "src/test/resources/test.asm";

        // Initialize the parser with the file
        Parser parser = new Parser(asmFilePath);

        // Advance and test the first instruction
        parser.advance();
        assertEquals("@2", parser.map.get(parser.LineCounter), "First instruction should be @2");
        assertEquals(Parser.INSTRUCTION_TYPE.A_INSTRUCTION, parser.instructionType(), "First instruction type should be A_INSTRUCTION");

        // Test symbol extraction
        assertEquals("2", parser.symbol(), "Symbol for @2 should be '2'");

        // Advance and test the second instruction
        parser.advance();
        assertEquals("D=A", parser.map.get(parser.LineCounter), "Second instruction should be D=A");
        assertEquals(Parser.INSTRUCTION_TYPE.C_INSTRUCTION, parser.instructionType(), "Second instruction type should be C_INSTRUCTION");
        assertEquals("D", parser.dest(), "Dest for D=A should be 'D'");
        assertEquals("A", parser.comp(), "Comp for D=A should be 'A'");
        assertEquals("", parser.jump(), "Jump for D=A should be empty");

        // Advance and test the third instruction
        parser.advance();
        assertEquals("@3", parser.map.get(parser.LineCounter), "Third instruction should be @3");
        assertEquals(Parser.INSTRUCTION_TYPE.A_INSTRUCTION, parser.instructionType(), "Third instruction type should be A_INSTRUCTION");

        // Test symbol extraction again
        assertEquals("3", parser.symbol(), "Symbol for @3 should be '3'");

        // Advance and test the fourth instruction (a label)
        parser.advance();
        assertEquals("(LOOP)", parser.map.get(parser.LineCounter), "Fourth instruction should be (LOOP)");
        assertEquals(Parser.INSTRUCTION_TYPE.L_INSTRUCTION, parser.instructionType(), "Fourth instruction type should be L_INSTRUCTION");

        // Advance and test the fifth instruction
        parser.advance();
        assertEquals("0;JMP", parser.map.get(parser.LineCounter), "Fifth instruction should be 0;JMP");
        assertEquals(Parser.INSTRUCTION_TYPE.C_INSTRUCTION, parser.instructionType(), "Fifth instruction type should be C_INSTRUCTION");
        assertEquals("", parser.dest(), "Dest for 0;JMP should be empty");
        assertEquals("0", parser.comp(), "Comp for 0;JMP should be '0'");
        assertEquals("JMP", parser.jump(), "Jump for 0;JMP should be 'JMP'");

        // Confirm no more lines
        parser.advance();
        assertFalse(parser.hasMoreLines(), "There should be no more lines after the last instruction");
    }
}


