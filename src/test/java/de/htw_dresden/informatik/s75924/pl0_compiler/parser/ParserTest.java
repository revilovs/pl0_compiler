package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.InvalidIdentifierException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.fail;

public class ParserTest {
    private void parseFile(String name) throws FileNotFoundException, SemanticRoutineException {
        Lexer lexer = new Lexer(new FileReader(getClass().getResource(name).getFile()));

        Parser parser = new Parser(lexer);

        parser.parse();
    }

    private void parseFileWithoutErrors(String file){
        try {
            parseFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SemanticRoutineException e) {
            fail();
        }

    }

    @Test
    public void parseT2WithoutErrors() {
        parseFileWithoutErrors("/t2.pl0");
    }

    @Test
    public void parseT3WithoutErrors() {
        parseFileWithoutErrors("/t3.pl0");
    }

    @Test
    public void parseT7WithoutErros() {
        parseFileWithoutErrors("/t7.pl0");
    }

    @Test
    public void parseFakultaetWithoutErrors() {
        parseFileWithoutErrors("/fakultaet.pl0");
    }

    @Test(expected = UnexpectedTokenException.class)
    public void parseT2InvalidThrowsException() throws SemanticRoutineException {
        try {
            parseFile("/t2invalid.pl0");
        }
        catch (InvalidIdentifierException | FileNotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = InvalidIdentifierException.class)
    public void parseFakultaetInvalidVarThrowsException() throws SemanticRoutineException {
        try {
            parseFile("/fakultaet-invalid_var.pl0");
        }
        catch (FileNotFoundException |  UnexpectedTokenException e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test(expected = InvalidIdentifierException.class)
    public void parseT3InvalidProcedureThrowsException() throws SemanticRoutineException {
        try {
            parseFile("/t3-invalid_proc.pl0");
        }
        catch (FileNotFoundException |  UnexpectedTokenException e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test(expected = InvalidIdentifierException.class)
    public void parseT7InvalidConstThrowsException() throws SemanticRoutineException {
        try {
            parseFile("/t7-invalid_const.pl0");
        }
        catch (FileNotFoundException |  UnexpectedTokenException e) {
            e.printStackTrace();
            fail();
        }
    }
}