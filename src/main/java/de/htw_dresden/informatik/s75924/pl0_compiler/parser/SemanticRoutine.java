package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

public abstract class SemanticRoutine {
    public abstract void apply(Parser parser) throws SemanticRoutineException;
}
