package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

public class VariableEntry implements NameListEntry {
    private String name;
    private int procedureIndex;
    private int relativeAddress;

    VariableEntry(String name, int procedureIndex, int relativeAddress) {
        this.name = name;
        this.procedureIndex = procedureIndex;
        this.relativeAddress = relativeAddress;
    }

    @Override
    public int getProcedureIndex() {
        return procedureIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getRelativeAddress() {
        return relativeAddress;
    }
}
