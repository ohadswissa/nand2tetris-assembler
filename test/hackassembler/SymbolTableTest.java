package hackassembler;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SymbolTableTest {
    @Test
    public void testInitialization() {
        SymbolTable table = new SymbolTable();
        assertFalse(table.contains("R1"), "Symbol table should be empty initially.");
    }

    @Test
    public void testAddEntry() {
        SymbolTable table = new SymbolTable();
        table.addEntry("R1", 1);
        assertTrue(table.contains("R1"), "Symbol table should contain 'R1'.");
        assertEquals(1, table.getAddress("R1"), "Address for 'R1' should be 1.");
    }

    @Test
    public void testGetAddress() {
        SymbolTable table = new SymbolTable();
        table.addEntry("LOOP", 4);
        assertEquals(4, table.getAddress("LOOP"), "Address for 'LOOP' should be 4.");
    }

    @Test
    public void testContains() {
        SymbolTable table = new SymbolTable();
        table.addEntry("END", 10);
        assertTrue(table.contains("END"), "Symbol table should contain 'END'.");
        assertFalse(table.contains("START"), "Symbol table should not contain 'START'.");
    }

    @Test
    public void testOverwriteEntry() {
        SymbolTable table = new SymbolTable();
        table.addEntry("R1", 1);
        table.addEntry("R1", 5); // Overwrite
        assertEquals(5, table.getAddress("R1"), "Address for 'R1' should be updated to 5.");
    }
    @Test
    public void testPrintTable() {
        SymbolTable table = new SymbolTable();
        // Adding 8 symbols
        table.addEntry("R1", 1);
        table.addEntry("R2", 2);
        table.addEntry("LOOP", 10);
        table.addEntry("END", 20);
        table.addEntry("TEMP", 5);
        table.addEntry("SCREEN", 16384);
        table.addEntry("KBD", 24576);
        table.addEntry("START", 0);
        // Printing the symbol table
        System.out.println("\n--- Symbol Table Test ---");
        table.printTable();
    }
}

