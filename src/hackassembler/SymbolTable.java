package hackassembler;
import java.util.HashMap;
import java.io.*;
import java.lang.*;
import java.util.regex.*;
import java.util.Scanner;
public class SymbolTable {
    private HashMap<String, Integer> symbolTable;//The symbol table we are going to fill.
    /**
     * Constructor/initializer : creates and initializes a SymbolTable.
     */
    public SymbolTable() {
        this.symbolTable = new HashMap<>();
    }
    /**
     *Adds <symbol,address> to the table.
     * @param symbol for the symbol as a String.
     * @param address for the place in the map.
     */
    public void addEntry(String symbol, int address) {
        this.symbolTable.put(symbol, address);
    }

    /**
     * Checks if symbol exists in the table.
     * @param symbol for the Symbol as a String to check.
     * @return true if the symbol is in the table.
     */
    boolean contains(String symbol) {
        return this.symbolTable.containsKey(symbol);
    }
    /**
     * Returns the address associated with symbol.
     * @param symbol for the Symbol as a String.
     * @return the place in the map.
     */
    int getAddress(String symbol) {
        return this.symbolTable.get(symbol);
    }

    /**
     * Prints a table.
     */
    public void printTable() {
        System.out.println("Symbol Table:");
        this.symbolTable.forEach((symbol, address) ->
                System.out.println("Symbol: " + symbol + ", Address: " + address));
    }

}
