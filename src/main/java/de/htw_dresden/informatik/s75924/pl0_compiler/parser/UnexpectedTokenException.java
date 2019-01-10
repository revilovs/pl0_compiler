package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

class UnexpectedTokenException extends FatalSemanticRoutineException {
    UnexpectedTokenException(Token token) {
        super(token, "Unexpected Token");
    }
}
