package de.htw_dresden.informatik.s75924.pl0_compiler;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.Parser;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.FatalSemanticRoutineException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PL0Compiler {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: PL0Compiler");
        }

        String inputFileName = args[0];

        FileReader reader = null;
        try {
            reader = new FileReader(inputFileName);
        } catch (FileNotFoundException e) {
            System.err.println("Could not find source file " + inputFileName);
            System.exit(1);
        }
        Lexer lexer = new Lexer(reader);

        String outFileName =
                inputFileName.endsWith(".pl0") ?
                        inputFileName.substring(0, inputFileName.length() - 3) + "cl0" :
                        inputFileName + ".cl0";

        CodeGenerator codeGenerator = null;
        try {
            codeGenerator = new CodeGenerator(outFileName);
        } catch (IOException e) {
            System.err.println("Could not open or create output file. Do you have the necessary permissions?");
            System.exit(2);
        }

        Parser parser = new Parser(lexer, codeGenerator);

        try {
            parser.parse();
        } catch (FatalSemanticRoutineException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("I/O error while parsing.");
            System.exit(3);
        }
    }
}
