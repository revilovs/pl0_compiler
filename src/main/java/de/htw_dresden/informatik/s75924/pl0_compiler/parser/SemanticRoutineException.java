package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

import java.util.Objects;

/**
 * Abstract parent class for all errors that can occur during parsing
 */
public abstract class SemanticRoutineException extends Exception {
    protected Token token;
    private String message;

    SemanticRoutineException(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    /**
     * @return A string containing the message and the token where the error occured.
     */
    @Override
    public final String toString() {
        return message + " at or near Token " + token;
    }

    /**
     * Checks if o has the same message and token as this
     * @param o the object to be compared
     * @return true if and only if message and token of this and o are equal
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticRoutineException)) return false;
        SemanticRoutineException that = (SemanticRoutineException) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(message, that.message);
    }
}
