package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

/**
 * An Exception thrown if an error occurs while parsing that can't be fixed by trying alternative arcs of the syntax graph
 */
public abstract class FatalSemanticRoutineException extends SemanticRoutineException{
    public FatalSemanticRoutineException(Token token, String message) {
        super(token, message);
    }
}
