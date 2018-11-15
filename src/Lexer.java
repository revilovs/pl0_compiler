import java.io.FileReader;
import java.io.IOException;

public class Lexer {
    private enum State {
        Z_0, Z_1, Z_2, Z_3, Z_4, Z_5, Z_6, Z_7, Z_8, ESy, EDe, ENu, EAs, ECo, ELE, ELT, EGE, EGT, EOF
    }

    private enum CharacterType {
        SYMBOL, DIGIT, ALPHA, COLON, EQUALS, LESS, GREATER, OTHER
    }

    private enum Action {
        WRE, W_R, END, REA
    }

    private static final State[][] stateTable = {
            /*      SYMBOL      DIGIT      ALPHA      COLON      EQUALS     LESS       GREATER    OTHER   */
            /*Z_0*/ { State.ESy, State.Z_2, State.Z_1, State.Z_3, State.Z_0, State.Z_4, State.Z_5, State.Z_0 },
            /*Z_1*/ { State.EDe, State.Z_1, State.Z_1, State.EDe, State.EDe, State.EDe, State.EDe, State.EDe },
            /*Z_2*/ { State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu },
            /*Z_3*/ { State.ECo, State.ECo, State.ECo, State.ECo, State.Z_6, State.ECo, State.ECo, State.ECo },
            /*Z_4*/ { State.ELT, State.ELT, State.ELT, State.ELT, State.Z_7, State.ELT, State.ELT, State.ELT },
            /*Z_5*/ { State.EGT, State.EGT, State.EGT, State.EGT, State.Z_8, State.EGT, State.EGT, State.EGT },
            /*Z_6*/ { State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs },
            /*Z_7*/ { State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE },
            /*Z_8*/ { State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE }
    };

    private static final Action[][] actionTable = {
            /*       SYMBOL       DIGIT       ALPHA       COLON       EQUALS      LESS        GREATER     OTHER   */
            /*Z_0*/ { Action.WRE, Action.W_R, Action.W_R, Action.W_R, Action.WRE, Action.W_R, Action.W_R, Action.REA },
            /*Z_1*/ { Action.END, Action.W_R, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.REA },
            /*Z_2*/ { Action.END, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA },
            /*Z_3*/ { Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.REA },
            /*Z_4*/ { Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.REA },
            /*Z_5*/ { Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.REA },
            /*Z_6*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA },
            /*Z_7*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA },
            /*Z_8*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA }
    };

    private FileReader reader;

    private Token current;
    private Token next;

    private State currentState;

    private boolean eof = false;

    private char currentChar;
    private String currentString;

    public Lexer(FileReader reader){
        this.reader = reader;
        read();
        lex();
    }

    public void lex(){
        current = next;

        boolean end = false;

        currentState = State.Z_0;
        currentString = "";

        while (currentState.toString().charAt(0)=='Z' && !end && !eof){
            State nextState = stateTable[currentState.ordinal()][getCharacterType(currentChar).ordinal()];
            switch (actionTable[currentState.ordinal()][getCharacterType(currentChar).ordinal()]){
                case WRE:
                    write();
                    read();
                    end = true;
                    break;
                case W_R:
                    write();
                    read();
                    break;
                case REA:
                    read();
                    break;
                case END:
                    end = true;
                    break;
            }
            currentState = nextState;
        }
        end();
    }

    public Token getCurrentToken(){
        return current;
    }

    public Token getNextToken(){
        return next;
    }

    private void read(){
        try {
            int readValue = reader.read();
            if(readValue == -1)
                eof = true;
            else
                currentChar = (char) readValue;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void write(){
        char upper = Character.isAlphabetic(currentChar) ? Character.toUpperCase(currentChar) : currentChar;

        currentString = currentString + upper;
    }

    private void end(){
        switch (currentState){
            case EDe:
                if(isKeyword(currentString))
                    next = new Token(Token.TokenType.KEYWORD, currentString);
                else
                    next = new Token(Token.TokenType.IDENTIFIER, currentString);
                break;
            case ENu:
                next = new Token(Token.TokenType.NUMERAL, Long.parseLong(currentString));
                break;
            case ESy:
            case EAs:
            case ECo:
            case ELE:
            case ELT:
            case EGE:
            case EGT:
                next = new Token(Token.TokenType.OPERATOR, currentString);
                break;
        }

        if(eof)
            next = Token.EOF_TOKEN;
    }

    private static CharacterType getCharacterType(char character){
        switch (character){
            case ':': return CharacterType.COLON;
            case '=': return CharacterType.EQUALS;
            case '<': return CharacterType.LESS;
            case '>': return CharacterType.GREATER;
            case '+':
            case '-':
            case '*':
            case '/':
            case ',':
            case '.':
            case ';':
            case '(':
            case ')':
            case '?':
            case '!':
            case '#':
                return CharacterType.SYMBOL;
        }

        if(Character.isAlphabetic(character))
            return CharacterType.ALPHA;

        if(Character.isDigit(character))
            return CharacterType.DIGIT;

        return CharacterType.OTHER;
    }

    private static boolean isKeyword(String string){
        final String[] keywords = {
                "BEGIN", "CALL", "CONST", "DO", "END", "IF", "ODD", "PROCEDURE", "THEN", "VAR", "WHILE"
        };

        for (String kewyword : keywords) {
            if(string.equals(kewyword))
                return true;
        }

        return false;
    }
}
