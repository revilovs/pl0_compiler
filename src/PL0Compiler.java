import java.io.FileNotFoundException;
import java.io.FileReader;

public class PL0Compiler {
    public static void main(String args[]) {
        try {
            FileReader reader = new FileReader(args[0]);
            Lexer lexer = new Lexer(reader);

            lexer.lex();

            Token token = lexer.getCurrentToken();

            while (token.getType() != Token.TokenType.EOF) {
                System.out.println(token.toString());
                lexer.lex();
                token = lexer.getCurrentToken();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
