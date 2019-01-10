package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

import java.util.ArrayList;

public class NameList {
    private ProcedureEntry mainProcedure = new ProcedureEntry( "", 0, null);
    private ArrayList<ProcedureEntry> procedures = new ArrayList<>();
    private ArrayList<Long> constants = new ArrayList<>();

    private String nextConstantName = null;

    private ProcedureEntry currentProcedure = mainProcedure;

    public NameList() {
        procedures.add(mainProcedure);
    }

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

    public void addVariable(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        currentProcedure.addVariableEntry(name);
    }

    public void addProcedure(Token token) throws InvalidIdentifierException {
        String name = token.getStringValue();
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException(token, "Identifier already exists, cannot be declared again");

        ProcedureEntry entry = new ProcedureEntry(name, procedures.size(), currentProcedure);

        currentProcedure.addProcedureEntry(entry);

        procedures.add(entry);

        currentProcedure = entry;
    }

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

    public long[] getConstantBlock(){
        return constants.stream().mapToLong(value -> value).toArray();
    }

    public int getNumberOfProcedures() {
        return procedures.size();
    }

    public int getCurrentProcedureIndex(){
        return currentProcedure.getProcedureIndex();
    }

    public int getVariableLength(){
        return currentProcedure.getVariableLength();
    }

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

    public boolean entryIsLocal(NameListEntry entry){
        return entry.getProcedureIndex() == procedures.indexOf(currentProcedure);
    }

    public boolean entryIsInMain(NameListEntry entry){
        return entry.getProcedureIndex() == procedures.indexOf(mainProcedure);
    }

    public int getIndexOfConstant(long constantValue){
        return constants.indexOf(constantValue);
    }

    public int getIndexOfProcedure(ProcedureEntry procedure){
        return procedures.indexOf(procedure);
    }
}
