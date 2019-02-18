package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.Parser;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.FatalSemanticRoutineException;
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

    private void compileFile(String inFile) throws IOException, FatalSemanticRoutineException {
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
        } catch (FatalSemanticRoutineException | IOException | URISyntaxException e) {
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
    public void generateT4correctly() {
        compileFileCorrectly("/t4.pl0", "/t4.cl0");
    }

    @Test
    public void generateT9correctly() {
        compileFileCorrectly("/t9.pl0", "/t9.cl0");
    }

    @Test
    public void generateT10correctly() {
        compileFileCorrectly("/t10.pl0", "/t10.cl0");
    }

    @Test
    public void generateT11correctly() {
        compileFileCorrectly("/t11.pl0", "/t11.cl0");
    }

    @Test
    public void generateFakultaetCorrectly() {
        compileFileCorrectly("/fakultaet.pl0", "/fakultaet.cl0");
    }

    @Test
    public void generateGCDCorrectly() {
        compileFileCorrectly("/gcd.pl0", "/gcd.cl0");
    }

    @Test
    public void generateWhileWhileCorrectly() {
        compileFileCorrectly("/whilewhile.pl0", "/whilewhile.cl0");
    }

    @After
    public void tearDown() {
        (new File(OUTFILE)).delete();
    }
}