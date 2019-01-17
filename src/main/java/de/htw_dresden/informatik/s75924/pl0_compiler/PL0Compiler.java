package de.htw_dresden.informatik.s75924.pl0_compiler;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.Parser;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.FatalSemanticRoutineException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PL0Compiler {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: PL0Compiler <source file> [<output file name>]");
            System.exit(1);
        }

        String inputFileName = args[0];

        FileReader reader = null;
        try {
            reader = new FileReader(inputFileName);
        } catch (FileNotFoundException e) {
            System.err.println("Could not find source file " + inputFileName);
            System.exit(2);
        }

        Lexer lexer = new Lexer(reader);

        String outFileName =
                args.length >= 2 ?
                        args[1] :
                inputFileName.endsWith(".pl0") ?
                        inputFileName.substring(0, inputFileName.length() - 3) + "cl0" :
                        inputFileName + ".cl0";

        CodeGenerator codeGenerator = null;
        try {
            codeGenerator = new CodeGenerator(outFileName);
        } catch (IOException e) {
            System.err.println("Could not open or create output file "
                    + outFileName
                    + ". Do you have the necessary permissions?");
            System.exit(3);
        }

        Parser parser = new Parser(lexer, codeGenerator);

        try {
            parser.parse();
            System.out.println("Compilation successful");
        }
        catch (FatalSemanticRoutineException e) {
            System.out.println("Error: " + e.toString());
            (new File(outFileName)).delete();
            System.exit(4);
        }
        catch (IOException e) {
            System.err.println("I/O error while parsing.");
            System.exit(5);
        }
    }
}
