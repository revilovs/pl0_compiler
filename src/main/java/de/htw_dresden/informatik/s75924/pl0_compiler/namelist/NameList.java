package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public void addVariable(String name) throws InvalidIdentifierException {
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException();

        currentProcedure.addVariableEntry(name);
    }

    public void addProcedure(String name) throws InvalidIdentifierException {
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException();

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

    public ConstantEntry findConstant(long value){
        throw new NotImplementedException();
    }

    public NameListEntry findIdentifier(String name){
        throw new NotImplementedException();
    }

    public void setConstantName(String name) throws InvalidIdentifierException {
        if (findIdentifierLocal(name))
            throw new InvalidIdentifierException();

        nextConstantName = name;
    }
}