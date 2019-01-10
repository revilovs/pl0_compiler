package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import java.io.IOException;

public abstract class SemanticRoutine {
    public abstract void apply(Parser parser) throws FatalSemanticRoutineException, IOException;
}
