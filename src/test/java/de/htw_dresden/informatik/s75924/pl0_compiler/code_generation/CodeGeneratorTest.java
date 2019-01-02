package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.Parser;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.SemanticRoutineException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class CodeGeneratorTest {
    private void compileFile(String inFile, String outFile) throws IOException, SemanticRoutineException {
        Lexer lexer = new Lexer(new FileReader(getClass().getResource(inFile).getFile()));

        CodeGenerator codeGenerator = new CodeGenerator(outFile);

        Parser parser = new Parser(lexer, codeGenerator);

        parser.parse();
    }

    @Test
    public void generateT7correctly() {
        try {
            compileFile("/t7.pl0", "out.cl0");

            byte[] outBytes = Files.readAllBytes(Paths.get("out.cl0"));
            byte[] correctBytes = Files.readAllBytes(Paths.get(getClass().getResource("/t7.cl0").toURI()));

            Assert.assertArrayEquals(correctBytes, outBytes);

        } catch (IOException | SemanticRoutineException | URISyntaxException e) {
            e.printStackTrace();
            fail();
        }
    }
}