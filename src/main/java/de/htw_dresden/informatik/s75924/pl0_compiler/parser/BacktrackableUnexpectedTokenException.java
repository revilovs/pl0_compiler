package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

/**
 * An Exception thrown if an unexpected token occurs while parsing a program, but no token of the current graph has been
 * lexed yet, so that backtracking is possible in the calling syntax graph
 */
class BacktrackableUnexpectedTokenException extends SemanticRoutineException {
    /**
     * Constructor
     * @param token the unexpected token
     */
    BacktrackableUnexpectedTokenException(Token token) {
        super(token, "Unexpected token");
    }
}
