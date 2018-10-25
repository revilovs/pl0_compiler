import java.io.FileReader;

public class Lexer {
    private enum State {
        Z0, Z1, Z2, Z3, Z4, Z5, Z6, Z7, Z8, ZE
    }

    private enum CharacterType {
        SYMBOL, DIGIT, ALPHA, COLON, EQUALS, LESS, GREATER, OTHER
    }

    private enum Action {
        WRE, W_R, END
    }

    private static final State[][] stateTable = {
            /*      SYMBOL    DIGIT     ALPHA     COLON     EQUALS    LESS      GREATER   OTHER   */
            /*Z0*/{ State.ZE, State.Z2, State.Z1, State.Z3, State.Z0, State.Z4, State.Z5, State.Z0 },
            /*Z1*/{ State.ZE, State.Z1, State.Z1, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE },
            /*Z2*/{ State.ZE, State.Z2, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE },
            /*Z3*/{ State.ZE, State.ZE, State.ZE, State.ZE, State.Z6, State.ZE, State.ZE, State.ZE },
            /*Z4*/{ State.ZE, State.ZE, State.ZE, State.ZE, State.Z7, State.ZE, State.ZE, State.ZE },
            /*Z5*/{ State.ZE, State.ZE, State.ZE, State.ZE, State.Z8, State.ZE, State.ZE, State.ZE },
            /*Z6*/{ State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE },
            /*Z7*/{ State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE },
            /*Z8*/{ State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE, State.ZE }
    };

    private static final Action[][] actionTable = {
            /*      SYMBOL      DIGIT       ALPHA       COLON       EQUALS      LESS        GREATER     OTHER   */
            /*Z0*/{ Action.WRE, Action.W_R, Action.W_R, Action.W_R, Action.WRE, Action.W_R, Action.W_R, Action.END },
            /*Z1*/{ Action.END, Action.W_R, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.END },
            /*Z2*/{ Action.END, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END },
            /*Z3*/{ Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.END },
            /*Z4*/{ Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.END },
            /*Z5*/{ Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.END },
            /*Z6*/{ Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END },
            /*Z7*/{ Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END },
            /*Z8*/{ Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END }
    };

    private FileReader reader;

    private Token current;
    private Token next;

    private State currentState;

    private char currentChar;

    public Lexer(FileReader reader){
        this.reader = reader;
        lex();
    }

    public void lex(){
        current = next;

        currentState = State.Z0;

        while (currentState != State.ZE){
            State nextState = stateTable[currentState.ordinal()][getCharacterType(currentChar).ordinal()];
            switch (actionTable[currentState.ordinal()][getCharacterType(currentChar).ordinal()]){
                case WRE:
                    write();
                    read();
                    end();
                    break;
                case W_R:
                    write();
                    read();
                case END:
                    end();
            }
            currentState = nextState;
        }
    }

    public Token getCurrentToken(){
        return current;
    }

    public Token getNextToken(){
        return next;
    }

    private void read(){

    }

    private void write(){

    }

    private void end(){

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
}
