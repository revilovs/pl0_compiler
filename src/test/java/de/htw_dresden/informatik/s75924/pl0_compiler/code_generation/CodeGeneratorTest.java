package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.Parser;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.SemanticRoutineException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class CodeGeneratorTest {
    private static final String OUTFILE = "out.cl0";

    private void compileFile(String inFile) throws IOException, SemanticRoutineException {
        Lexer lexer = new Lexer(new FileReader(getClass().getResource(inFile).getFile()));

        CodeGenerator codeGenerator = new CodeGenerator(OUTFILE);

        Parser parser = new Parser(lexer, codeGenerator);

        parser.parse();
    }

    private void compareFiles(String expected) throws URISyntaxException, IOException {
        byte[] expectedBytes = Files.readAllBytes(Paths.get(getClass().getResource(expected).toURI()));
        byte[] actualBytes = Files.readAllBytes(Paths.get(OUTFILE));

        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    private void compileFileCorrectly(String sourceFile, String correctlyCompiledFile){
        try{
            compileFile(sourceFile);

            compareFiles(correctlyCompiledFile);
        } catch (SemanticRoutineException | IOException | URISyntaxException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void generateT7correctly() {
        compileFileCorrectly("/t7.pl0", "/t7.cl0");
    }

    @Test
    public void generateT8correctly() {
        compileFileCorrectly("/t8.pl0", "/t8.cl0");
    }

    @Test
    public void generateT2correctly() {
        compileFileCorrectly("/t2.pl0", "/t2.cl0");
    }

    @Test
    public void generateT3correctly() {
        compileFileCorrectly("/t3.pl0", "/t3.cl0");
    }

    @Test
    public void generateFakultaetCorrectly() {
        compileFileCorrectly("/fakultaet.pl0", "/fakultaet.cl0");
    }

    @After
    public void tearDown() {
        (new File(OUTFILE)).delete();
    }
}