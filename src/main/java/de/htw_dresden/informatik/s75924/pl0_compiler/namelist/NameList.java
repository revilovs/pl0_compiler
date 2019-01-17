package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

import java.util.ArrayList;

/**
 * A class for managing the name list
 */
public class NameList {
    private ProcedureEntry mainProcedure = new ProcedureEntry( "", 0, null);
    private ArrayList<ProcedureEntry> procedures = new ArrayList<>();
    private ArrayList<Long> constants = new ArrayList<>();

    private String nextConstantName = null;

    private ProcedureEntry currentProcedure = mainProcedure;

    /**
     * Constructor
     */
    public NameList() {
        procedures.add(mainProcedure);
    }

    /**
     * Adds a constant to the constant block and a ConstantEntry if a name was specified previously
     * @param value the constant value
     */
    public void addConstant(long value){
        if (nextConstantName == null){
            if (!constants.contains(value))
                constants.add(value);

            return;
        }

        if (!constants.contains(value))
            constants.add(value);

        int index = constants.indexOf(value);

        ConstantEntry entry = new ConstantEntry(currentProcedure.getProcedureIndex(), value, index, nextConstantName);

        currentProcedure.addConstantEntry(entry);

        nextConstantName = null;
    }

    /**
     * Adds a VariableEntry to the current procedure
     * @param token the token containing the variable identifier
     * @throws InvalidIdentifierException if the identifier already exists in the current scope
     */
    public void addVariable(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        currentProcedure.addVariableEntry(name);
    }

    /**
     * Adds a ProcedureEntry to the name list
     * @param token the token containing the procedure identifier
     * @throws InvalidIdentifierException if the identifier already exists in the current scope
     */
    public void addProcedure(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        ProcedureEntry entry = new ProcedureEntry(name, procedures.size(), currentProcedure);

        currentProcedure.addProcedureEntry(entry);

        procedures.add(entry);

        currentProcedure = entry;
    }

    /**
     * Sets the current procedure's parent to the new current procedure
     */
    public void endProcedure() {
        if (currentProcedure.equals(mainProcedure))
            return;

        currentProcedure = currentProcedure.getParent();
    }

    private boolean findIdentifierLocal(String identifier){
        return currentProcedure.getIdentifiers().stream().anyMatch((entry -> entry.getName().equals(identifier)));
    }

    public void setConstantName(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        nextConstantName = name;
    }

    /**
     * @return the constant block
     */
    public long[] getConstantBlock(){
        return constants.stream().mapToLong(value -> value).toArray();
    }

    /**
     * @return the number of procedures
     */
    public int getNumberOfProcedures() {
        return procedures.size();
    }

    /**
     * @return the index of the current procedure
     */
    public int getCurrentProcedureIndex(){
        return currentProcedure.getProcedureIndex();
    }

    /**
     * @return the length of the current procedure's variable block
     */
    public int getVariableLength(){
        return currentProcedure.getVariableLength();
    }

    /**
     * Looks for a NameListEntry starting in the current procedure going through the parent procedures up to the main procedure
     * @param name the name of the entry to be found
     * @return a NameList entry with the name as specified or null if no identifier has that name.
     * If multiple such entries exist, it returns the one at the lowest level.
     */
    public NameListEntry findIdentifier(String name){
        for (ProcedureEntry searchProcedure = currentProcedure;
             searchProcedure != null;
             searchProcedure = searchProcedure.getParent()){

            NameListEntry entry = searchProcedure.getIdentifiers().stream()
                    .filter(nameListEntry -> nameListEntry.getName().equals(name)).findFirst().orElse(null);

            if (entry != null)
                return entry;

        }

        return null;
    }

    /**
     * Checks if the entry is a local identifier in the current procedure
     * @param entry the entry to be checked
     * @return true if and only if the entry belongs to the current procedure
     */
    public boolean entryIsLocal(NameListEntry entry){
        return entry.getProcedureIndex() == procedures.indexOf(currentProcedure);
    }

    /**
     * Checks if the entry is an identifier from the main procedure
     * @param entry the entry to be checked
     * @return true if and only if the entry belongs to the main procedure
     */
    public boolean entryIsInMain(NameListEntry entry){
        return entry.getProcedureIndex() == procedures.indexOf(mainProcedure);
    }

    /**
     * Gets the index of a constant value from the constant block
     * @param constantValue the value of the constant
     * @return the index of the constant in the constant block
     */
    public int getIndexOfConstant(long constantValue){
        return constants.indexOf(constantValue);
    }

    /**
     * Gets the index of a procedure
     * @param procedure the procedure whose index is wanted
     * @return the procedure's index
     */
    public int getIndexOfProcedure(ProcedureEntry procedure){
        return procedures.indexOf(procedure);
    }
}
