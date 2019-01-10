package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

public abstract class FatalSemanticRoutineException extends SemanticRoutineException{
    public FatalSemanticRoutineException(Token token, String message) {
        super(token, message);
    }
}
