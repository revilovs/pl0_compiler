package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

class BacktrackableUnexpectedTokenException extends SemanticRoutineException {
    BacktrackableUnexpectedTokenException(Token token) {
        super(token, "Unexpected token");
    }
}
