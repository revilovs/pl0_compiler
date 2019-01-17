package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

/**
 * An Exception thrown when an unexpected token is encountered and no alternatives in the syntax graph are left
 */
class UnexpectedTokenException extends FatalSemanticRoutineException {
    UnexpectedTokenException(Token token) {
        super(token, "Unexpected Token");
    }
}
