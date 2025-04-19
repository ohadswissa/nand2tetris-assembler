package hackassembler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class codetest {
    // Test for the comp method
    @Test
    void testCompValidInputs() {
        System.out.println("Testing valid inputs for the 'comp' method...");

        // a == 0 tests
        assertEquals("0101010", Code.comp("0"), "Expected binary for '0' is 0101010.");
        assertEquals("0111111", Code.comp("1"), "Expected binary for '1' is 0111111.");
        assertEquals("0000010", Code.comp("D+A"), "Expected binary for 'D+A' is 0000010.");
        assertEquals("0000000", Code.comp("D&A"), "Expected binary for 'D&A' is 0000000.");

        // a == 1 tests
        assertEquals("1110000", Code.comp("M"), "Expected binary for 'M' is 1110000.");
        assertEquals("1000010", Code.comp("D+M"), "Expected binary for 'D+M' is 1000010.");

        System.out.println("All valid 'comp' inputs passed.\n");
    }

    @Test
    void testCompInvalidInput() {
        System.out.println("Testing invalid input for the 'comp' method...");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Code.comp("INVALID");
        });
        assertEquals("Invalid component value found: INVALID", exception.getMessage(),
                "Expected error message for invalid comp input.");

        System.out.println("Invalid 'comp' input test passed.\n");
    }

    // Test for the dest method
    @Test
    void testDestValidInputs() {
        System.out.println("Testing valid inputs for the 'dest' method...");

        assertEquals("000", Code.dest("null"), "Expected binary for 'null' is 000.");
        assertEquals("001", Code.dest("M"), "Expected binary for 'M' is 001.");
        assertEquals("010", Code.dest("D"), "Expected binary for 'D' is 010.");
        assertEquals("111", Code.dest("ADM"), "Expected binary for 'ADM' is 111.");

        System.out.println("All valid 'dest' inputs passed.\n");
    }

    @Test
    void testDestInvalidInput() {
        System.out.println("Testing invalid input for the 'dest' method...");

        assertEquals("000", Code.dest("XYZ"), "Expected binary for invalid dest input is 000.");

        System.out.println("Invalid 'dest' input test passed.\n");
    }

    // Test for the jump method
    @Test
    void testJumpValidInputs() {
        System.out.println("Testing valid inputs for the 'jump' method...");

        assertEquals("000", Code.jump("null"), "Expected binary for 'null' is 000.");
        assertEquals("001", Code.jump("JGT"), "Expected binary for 'JGT' is 001.");
        assertEquals("111", Code.jump("JMP"), "Expected binary for 'JMP' is 111.");

        System.out.println("All valid 'jump' inputs passed.\n");
    }

    @Test
    void testJumpInvalidInput() {
        System.out.println("Testing invalid input for the 'jump' method...");

        assertEquals("000", Code.jump("INVALID"), "Expected binary for invalid jump input is 000.");

        System.out.println("Invalid 'jump' input test passed.\n");
    }
}
