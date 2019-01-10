package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;

public class UnexpectedTokenException extends FatalSemanticRoutineException {
    private Token token;

    public UnexpectedTokenException(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "UnexpectedTokenException{" +
                "at Token=" + token +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnexpectedTokenException that = (UnexpectedTokenException) o;
        return token.equals(that.token);
    }
}
