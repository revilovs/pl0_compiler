package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.FatalSemanticRoutineException;

/**
 * An Exception thrown if an undeclared identifier is encountered
 */
public class InvalidIdentifierException extends FatalSemanticRoutineException {
    /**
     * Constructor
     * @param token the offending token
     * @param message the reason for throwing the Exception
     */
    public InvalidIdentifierException(Token token, String message) {
        super(token, message);
    }
}
