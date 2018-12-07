package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.fail;

public class ParserTest {
    @Test
    public void parseT2WithoutErrors() {
        try {
            Lexer lexer = new Lexer(new FileReader(getClass().getResource("/t2.pl0").getFile()));

            Parser parser = new Parser(lexer);

            parser.parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnexpectedTokenException | SemanticRoutineException e) {
            fail();
        }
    }

    @Test(expected = UnexpectedTokenException.class)
    public void parseT2InvalidThrowsException() throws UnexpectedTokenException {
        try {
            Lexer lexer = new Lexer(new FileReader(getClass().getResource("/t2invalid.pl0").getFile()));

            Parser parser = new Parser(lexer);

            parser.parse();
        }
        catch (FileNotFoundException | SemanticRoutineException e) {
            e.printStackTrace();
            fail();
        }
    }
}