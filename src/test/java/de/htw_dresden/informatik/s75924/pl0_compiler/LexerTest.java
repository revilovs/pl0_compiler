package de.htw_dresden.informatik.s75924.pl0_compiler;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.Assert.fail;

public class LexerTest {

    @Test
    public void lexT2correctly(){
        try {
            Lexer sut = new Lexer(new FileReader(getClass().getResource("/t2.pl0").getFile()));

            ArrayList<Token> expectedTokens = new ArrayList<>();

            //var a,b,Max;
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.VAR.value, 1, 0));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "A", 1, 5));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ',', 1, 6));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "B", 1, 7));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ',', 1, 8));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "MAX", 1, 9));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 1, 12));

            // procedure p1;
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.PROCEDURE.value, 2, 1));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "P1", 2, 11));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 2, 13));

            // begin
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.BEGIN.value, 3, 1));

            //   if a>=b then Max:=a;
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.IF.value, 4, 3));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "A", 4, 6));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, SpecialCharacter.GREATER_OR_EQUAL.value, 4, 7));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "B", 4, 9));
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.THEN.value, 4, 11));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "MAX", 4, 15));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, SpecialCharacter.ASSIGN.value, 4, 18));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "A", 4, 20));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 4, 21));

            //   if a< b then Max:=b
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.IF.value, 5, 3));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "A", 5, 6));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, '<', 5, 7));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "B", 5, 9));
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.THEN.value, 5, 11));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "MAX", 5, 15));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, SpecialCharacter.ASSIGN.value, 5, 18));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "B", 5, 20));

            // end;
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.END.value, 6, 1));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 6, 4));

            // begin
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.BEGIN.value, 7, 1));

            //   ?a;?b;
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, '?', 8, 3));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "A", 8, 4));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 8, 5));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, '?', 8, 6));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "B", 8, 7));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 8, 8));

            //   call p1;
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.CALL.value, 9, 3));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "P1", 9, 8));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, ';', 9, 10));

            //   !Max
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, '!', 10, 3));
            expectedTokens.add(new Token(Token.TokenType.IDENTIFIER, "MAX", 10, 4));

            // end.
            expectedTokens.add(new Token(Token.TokenType.KEYWORD, SpecialCharacter.END.value, 11, 1));
            expectedTokens.add(new Token(Token.TokenType.SYMBOL, '.', 11, 4));

            expectedTokens.add(Token.EOF_TOKEN);

            for (Token expectedToken : expectedTokens) {
                sut.lex();
                Assert.assertEquals(expectedToken, sut.getCurrentToken());
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}