package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

import java.util.Objects;

public abstract class SemanticRoutineException extends Exception {
    protected Token token;
    private String message;

    SemanticRoutineException(Token token, String message) {
        this.token = token;
        this.message = message;
    }

    @Override
    public final String toString() {
        return message + " at or near Token " + token;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticRoutineException)) return false;
        SemanticRoutineException that = (SemanticRoutineException) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(message, that.message);
    }
}
