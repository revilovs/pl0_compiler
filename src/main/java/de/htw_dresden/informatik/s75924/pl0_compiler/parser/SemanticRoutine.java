package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import java.io.IOException;

public interface SemanticRoutine {
    void apply(Parser parser) throws FatalSemanticRoutineException, IOException;
}
