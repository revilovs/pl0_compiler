package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import java.io.IOException;

/**
 * Interface for semantic routines
 */
public interface SemanticRoutine {
    /**
     * The actual semantic routine
     * @param parser the parser parsing the source code
     * @throws FatalSemanticRoutineException if an error in the source code is detected during the semantic routine
     * @throws IOException if an I/O error occurs
     */
    void apply(Parser parser) throws FatalSemanticRoutineException, IOException;
}
