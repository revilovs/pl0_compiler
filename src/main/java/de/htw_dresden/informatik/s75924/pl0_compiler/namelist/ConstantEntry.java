package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

public class ConstantEntry implements NameListEntry {
    private int procedureIndex;
    private long value;
    private int index;
    private String name;

    ConstantEntry(int procedureIndex, long value, int index, String name) {
        this.procedureIndex = procedureIndex;
        this.value = value;
        this.index = index;
        this.name = name;
    }

    @Override
    public int getProcedureIndex() {
        return procedureIndex;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
}
