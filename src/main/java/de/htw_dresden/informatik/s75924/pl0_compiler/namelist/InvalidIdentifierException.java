package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.FatalSemanticRoutineException;

public class InvalidIdentifierException extends FatalSemanticRoutineException {
    public InvalidIdentifierException(Token token, String message) {
        super(token, message);
    }
}
