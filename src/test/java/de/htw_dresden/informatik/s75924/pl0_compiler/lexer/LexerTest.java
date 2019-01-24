package de.htw_dresden.informatik.s75924.pl0_compiler.lexer;

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
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.VAR.value, 1, 1));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 1, 5));
            expectedTokens.add(new Token(TokenType.SYMBOL, ',', 1, 6));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "B", 1, 7));
            expectedTokens.add(new Token(TokenType.SYMBOL, ',', 1, 8));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "MAX", 1, 9));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 1, 12));

            // procedure p1;
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.PROCEDURE.value, 2, 1));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "P1", 2, 11));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 2, 13));

            // begin
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.BEGIN.value, 7, 1));

            //   if a>=b then Max:=a;
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.IF.value, 8, 3));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 8, 6));
            expectedTokens.add(new Token(TokenType.SYMBOL, SpecialCharacter.GREATER_OR_EQUAL.value, 8, 7));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "B", 8, 9));
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.THEN.value, 8, 11));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "MAX", 8, 16));
            expectedTokens.add(new Token(TokenType.SYMBOL, SpecialCharacter.ASSIGN.value, 8, 19));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 8, 21));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 8, 22));

            //   if a< b then Max:=b
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.IF.value, 9, 3));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 9, 6));
            expectedTokens.add(new Token(TokenType.SYMBOL, '<', 9, 7));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "B", 9, 9));
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.THEN.value, 9, 11));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "MAX", 9, 16));
            expectedTokens.add(new Token(TokenType.SYMBOL, SpecialCharacter.ASSIGN.value, 9, 19));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "B", 9, 21));

            // end;
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.END.value, 10, 1));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 10, 4));

            // begin
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.BEGIN.value, 11, 1));

            //   ?a;?b;
            expectedTokens.add(new Token(TokenType.SYMBOL, '?', 12, 3));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 12, 4));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 12, 5));
            expectedTokens.add(new Token(TokenType.SYMBOL, '?', 12, 6));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "B", 12, 7));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 12, 8));

            //   call p1;
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.CALL.value, 13, 3));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "P1", 13, 8));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 13, 10));

            //   !Max
            expectedTokens.add(new Token(TokenType.SYMBOL, '!', 14, 3));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "MAX", 14, 4));

            // end.
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.END.value, 15, 1));
            expectedTokens.add(new Token(TokenType.SYMBOL, '.', 15, 4));

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

    @Test
    public void lexT7Correctly() {
        try {
            Lexer sut = new Lexer(new FileReader(getClass().getResource("/t7.pl0").getFile()));

            ArrayList<Token> expectedTokens = new ArrayList<>();

            //const a=5, b=7;
            expectedTokens.add(new Token(TokenType.KEYWORD, SpecialCharacter.CONST.value, 1, 1));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 1, 7));
            expectedTokens.add(new Token(TokenType.SYMBOL, '=', 1, 8));
            expectedTokens.add(new Token(TokenType.NUMERAL, 5, 1, 9));
            expectedTokens.add(new Token(TokenType.SYMBOL, ',', 1, 10));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "B", 1, 51));
            expectedTokens.add(new Token(TokenType.SYMBOL, '=', 1, 52));
            expectedTokens.add(new Token(TokenType.NUMERAL, 7, 1, 53));
            expectedTokens.add(new Token(TokenType.SYMBOL, ';', 1, 54));

            //!a.
            expectedTokens.add(new Token(TokenType.SYMBOL, '!', 2, 1));
            expectedTokens.add(new Token(TokenType.IDENTIFIER, "A", 2,2));
            expectedTokens.add(new Token(TokenType.SYMBOL, '.', 2,3));

            expectedTokens.add(Token.EOF_TOKEN);



            for (Token expectedToken : expectedTokens) {
                sut.lex();
                Assert.assertEquals(expectedToken, sut.getCurrentToken());
            }

        }
        catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }
}