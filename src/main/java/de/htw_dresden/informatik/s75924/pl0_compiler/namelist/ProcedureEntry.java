package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

import java.util.ArrayList;

/**
 * Class representing a procedure entry in the name list
 */
public class ProcedureEntry implements NameListEntry {
    private String name;
    private ProcedureEntry parent;
    private int procedureIndex;
    private ArrayList<NameListEntry> identifiers = new ArrayList<>();
    private int variableRelativeAddressCounter = 0;

    ProcedureEntry(String name, int procedureIndex, ProcedureEntry parent) {
        this.name = name;
        this.procedureIndex = procedureIndex;
        this.parent = parent;
    }

    ProcedureEntry getParent() {
        return parent;
    }

    ArrayList<NameListEntry> getIdentifiers() {
        return identifiers;
    }

    @Override
    public int getProcedureIndex() {
        return procedureIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    int getVariableLength(){
        return variableRelativeAddressCounter;
    }

    void addConstantEntry(ConstantEntry entry){
        identifiers.add(entry);
    }

    void addVariableEntry(String name){
        VariableEntry entry = new VariableEntry(name, procedureIndex, variableRelativeAddressCounter);

        variableRelativeAddressCounter += 4;

        identifiers.add(entry);
    }

    void makeVariableArray(int length){
        variableRelativeAddressCounter += (4 * (length - 1));
    }

    void addProcedureEntry(ProcedureEntry entry) {
        identifiers.add(entry);
    }
}
