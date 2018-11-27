package de.htw_dresden.informatik.s75924.pl0_compiler.lexer;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.TestCase.fail;

public class TokenTest {
    @Test
    public void constructNumeralToken() {
        try {
            Token sut = new Token(TokenType.NUMERAL, 42, 1, 1);

            Assert.assertEquals(TokenType.NUMERAL, sut.getType());
            Assert.assertEquals(42, sut.getNumberValue());
            Assert.assertEquals(1, sut.getRow());
            Assert.assertEquals(1, sut.getColumn());
            Assert.assertEquals(0, sut.getCharValue());
            Assert.assertNull(sut.getStringValue());

        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = InvalidTokenTypeException.class)
    public void constructNumeralTokenWrongType() throws InvalidTokenTypeException {
        Token sut = new Token(TokenType.NUMERAL, 'A', 1, 1);
    }

    @Test
    public void constructSymbolToken() {
        try {
            Token sut = new Token(TokenType.SYMBOL, '+', 1, 1);

            Assert.assertEquals(TokenType.SYMBOL, sut.getType());
            Assert.assertEquals(0, sut.getNumberValue());
            Assert.assertEquals(1, sut.getRow());
            Assert.assertEquals(1, sut.getColumn());
            Assert.assertEquals('+', sut.getCharValue());
            Assert.assertNull(sut.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = InvalidTokenTypeException.class)
    public void constructSymbolTokenWrongType() throws InvalidTokenTypeException {
        Token sut = new Token(TokenType.SYMBOL, "+", 1, 1);
    }

    @Test
    public void constructKeywordToken() {
        Token sut = null;
        try {
            sut = new Token(TokenType.KEYWORD, SpecialCharacter.VAR.value, 1, 1);

            Assert.assertEquals(TokenType.KEYWORD, sut.getType());
            Assert.assertEquals(0, sut.getNumberValue());
            Assert.assertEquals(1, sut.getRow());
            Assert.assertEquals(1, sut.getColumn());
            Assert.assertEquals(SpecialCharacter.VAR.value, sut.getCharValue());
            Assert.assertNull(sut.getStringValue());
        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = InvalidTokenTypeException.class)
    public void constructKeywordTokenWrongType() throws InvalidTokenTypeException {
        Token sut = new Token(TokenType.KEYWORD, "VAR", 1 , 1);
    }

    @Test
    public void constructIdentifierToken() {
        try {
            Token sut = new Token(TokenType.IDENTIFIER, "X", 1, 1);

            Assert.assertEquals(TokenType.IDENTIFIER, sut.getType());
            Assert.assertEquals(0, sut.getNumberValue());
            Assert.assertEquals(1, sut.getRow());
            Assert.assertEquals(1, sut.getColumn());
            Assert.assertEquals(0, sut.getCharValue());
            Assert.assertEquals("X", sut.getStringValue());
        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = InvalidTokenTypeException.class)
    public void constructIdentifierTokenWrongType() throws InvalidTokenTypeException {
        Token sut = new Token(TokenType.IDENTIFIER, 'x', 1, 1);
    }

    @Test
    public void checkEOFToken() {
        Token sut = Token.EOF_TOKEN;

        Assert.assertEquals(TokenType.EOF, sut.getType());
        Assert.assertEquals(0, sut.getNumberValue());
        Assert.assertEquals(0, sut.getRow());
        Assert.assertEquals(0, sut.getColumn());
        Assert.assertEquals(0, sut.getCharValue());
        Assert.assertNull(sut.getStringValue());
    }

    @Test
    public void symbolTokenToString() {
        try {
            Token sut = new Token(TokenType.SYMBOL, '+', 1,1);

            Assert.assertEquals("SYMBOL + at 1:1", sut.toString());
        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void keywordTokenToString() {
        try {
            Token sut = new Token(TokenType.KEYWORD, SpecialCharacter.VAR.value, 1,1);

            Assert.assertEquals("KEYWORD VAR at 1:1", sut.toString());
        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void numeralTokenToString() {
        try {
            Token sut = new Token(TokenType.NUMERAL, -42, 1,1);

            Assert.assertEquals("NUMERAL -42 at 1:1", sut.toString());
        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void identifierTokenToString() {
        try {
            Token sut = new Token(TokenType.IDENTIFIER, "MAX", 1,1);

            Assert.assertEquals("IDENTIFIER MAX at 1:1", sut.toString());
        } catch (InvalidTokenTypeException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void eofTokenToString() {
        Token sut = Token.EOF_TOKEN;

        Assert.assertEquals("EOF", sut.toString());
    }
}