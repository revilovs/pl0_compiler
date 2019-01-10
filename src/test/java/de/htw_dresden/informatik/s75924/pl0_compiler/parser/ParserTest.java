package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.*;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.InvalidIdentifierException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.fail;

public class ParserTest {
    private void parseFile(String name) throws IOException, FatalSemanticRoutineException {
        Lexer lexer = new Lexer(new FileReader(getClass().getResource(name).getFile()));

        CodeGenerator generator = new CodeGenerator("/dev/null");

        Parser parser = new Parser(lexer, generator);

        parser.parse();
    }

    private void parseFileWithoutErrors(String file) throws IOException, FatalSemanticRoutineException {
        parseFile(file);
    }

    @Test
    public void parseT2WithoutErrors() throws IOException, FatalSemanticRoutineException {
        parseFileWithoutErrors("/t2.pl0");
    }

    @Test
    public void parseT3WithoutErrors() throws IOException, FatalSemanticRoutineException {
        parseFileWithoutErrors("/t3.pl0");
    }

    @Test
    public void parseT7WithoutErrors() throws IOException, FatalSemanticRoutineException {
        parseFileWithoutErrors("/t7.pl0");
    }

    @Test
    public void parseFakultaetWithoutErrors() throws IOException, FatalSemanticRoutineException {
        parseFileWithoutErrors("/fakultaet.pl0");
    }

    @Test
    public void parseT2InvalidThrowsException() throws FatalSemanticRoutineException, InvalidTokenTypeException, IOException {
        try {
            parseFile("/t2invalid.pl0");
        }
        catch (UnexpectedTokenException e){
            Token expected = new Token(TokenType.KEYWORD, SpecialCharacter.BEGIN.value, 3, 1);
            Assert.assertEquals(new UnexpectedTokenException(expected), e);
        }
    }

    @Test
    public void parseFakultaetInvalidVarThrowsException() throws FatalSemanticRoutineException, InvalidTokenTypeException, IOException {
        try {
            parseFile("/fakultaet-invalid_var.pl0");
        }
        catch (InvalidIdentifierException e){
            Token expectedToken = new Token(TokenType.IDENTIFIER, "B", 4, 8);
            Assert.assertEquals(
                    new InvalidIdentifierException(expectedToken, "Identifier already exists, cannot be declared again"),
                    e);
        }
    }


    @Test
    public void parseT3InvalidProcedureThrowsException() throws FatalSemanticRoutineException, IOException, InvalidTokenTypeException {
        try {
            parseFile("/t3-invalid_proc.pl0");
        }
        catch (InvalidIdentifierException e){
            Token expectedToken = new Token(TokenType.IDENTIFIER, "P1", 8, 11);
            Assert.assertEquals(
                    new InvalidIdentifierException(expectedToken, "Identifier already exists, cannot be declared again"),
                    e);
        }
    }


    @Test
    public void parseT7InvalidConstThrowsException() throws FatalSemanticRoutineException, IOException, InvalidTokenTypeException {
        try {
            parseFile("/t7-invalid_const.pl0");
        }
        catch (InvalidIdentifierException e){
            Token expectedToken = new Token(TokenType.IDENTIFIER, "A", 1, 12);
            Assert.assertEquals(
                    new InvalidIdentifierException(expectedToken, "Identifier already exists, cannot be declared again"),
                    e);
        }
    }
}