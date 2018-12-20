package de.htw_dresden.informatik.s75924.pl0_compiler;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.Parser;
import de.htw_dresden.informatik.s75924.pl0_compiler.parser.SemanticRoutineException;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class PL0Compiler {
    public static void main(String args[]) {
        try {
            FileReader reader = new FileReader(args[0]);
            Lexer lexer = new Lexer(reader);

            Parser parser = new Parser(lexer);

            parser.parse();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SemanticRoutineException e) {
            System.err.println(e.toString());
            System.exit(2);
        }
    }
}
