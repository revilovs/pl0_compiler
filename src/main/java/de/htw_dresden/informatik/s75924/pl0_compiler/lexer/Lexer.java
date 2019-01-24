package de.htw_dresden.informatik.s75924.pl0_compiler.lexer;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class for splitting the input file into tokens
 */
public class Lexer {
    /**
     * Enum containing the states of the Lexer's FSM.
     * Z_? states are normal states, E?? are end/final states.
     * EIK can be an identifier or a keyword, EId, must be an Identifier
     */
    private enum State {
        Z_0, Z_1, Z_2, Z_3, Z_4, Z_5, Z_6, Z_7, Z_8, Z_9, Z10, Z11, Z12, ESy, EIK, EId, ENu, EAs, ECo, ELE, ELT, EGE, EGT
    }

    private enum CharacterType {
        SYMBOL, DIGIT, ALPHA, COLON, EQUALS, LESS, GREATER, OTHER, ALPHA_KEY_START, SLASH, STAR
    }

    private enum Action {
        WRE, W_R, END, REA, CLR
    }

    private static final HashMap<Character, CharacterType> characterTypeMap = new HashMap<>();

    static {
        characterTypeMap.put(':', CharacterType.COLON);
        characterTypeMap.put('=', CharacterType.EQUALS);
        characterTypeMap.put('<', CharacterType.LESS);
        characterTypeMap.put('>', CharacterType.GREATER);

        characterTypeMap.put('+', CharacterType.SYMBOL);
        characterTypeMap.put('-', CharacterType.SYMBOL);
        characterTypeMap.put(',', CharacterType.SYMBOL);
        characterTypeMap.put('.', CharacterType.SYMBOL);
        characterTypeMap.put(';', CharacterType.SYMBOL);
        characterTypeMap.put('(', CharacterType.SYMBOL);
        characterTypeMap.put(')', CharacterType.SYMBOL);
        characterTypeMap.put('?', CharacterType.SYMBOL);
        characterTypeMap.put('!', CharacterType.SYMBOL);
        characterTypeMap.put('#', CharacterType.SYMBOL);

        characterTypeMap.put('*', CharacterType.STAR);
        characterTypeMap.put('/', CharacterType.SLASH);

        for (char c = '0'; c <= '9'; c++)
            characterTypeMap.put(c, CharacterType.DIGIT);

        for (char c = 'a'; c <= 'z'; c++)
            characterTypeMap.put(c, CharacterType.ALPHA);

        for (char c = 'A'; c <= 'Z'; c++)
            characterTypeMap.put(c, CharacterType.ALPHA);

        characterTypeMap.put('b', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('c', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('d', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('e', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('i', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('o', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('p', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('t', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('v', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('w', CharacterType.ALPHA_KEY_START);

        characterTypeMap.put('B', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('C', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('D', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('E', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('I', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('O', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('P', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('T', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('V', CharacterType.ALPHA_KEY_START);
        characterTypeMap.put('W', CharacterType.ALPHA_KEY_START);
    }

    private static final State[][] stateTable = {
            /*       SYMBOL      DIGIT      ALPHA      COLON      EQUALS     LESS       GREATER    OTHER      ALPHA_KS   SLASH      STAR */
            /*Z_0*/ { State.ESy, State.Z_2, State.Z_1, State.Z_3, State.ESy, State.Z_4, State.Z_5, State.Z_0, State.Z_9, State.Z10, State.ESy },
            /*Z_1*/ { State.EId, State.Z_1, State.Z_1, State.EId, State.EId, State.EId, State.EId, State.EId, State.Z_1, State.EId, State.EId },
            /*Z_2*/ { State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu, State.ENu },
            /*Z_3*/ { State.ECo, State.ECo, State.ECo, State.ECo, State.Z_6, State.ECo, State.ECo, State.ECo, State.ECo, State.ECo, State.ECo },
            /*Z_4*/ { State.ELT, State.ELT, State.ELT, State.ELT, State.Z_7, State.ELT, State.ELT, State.ELT, State.ELT, State.ELT, State.ELT },
            /*Z_5*/ { State.EGT, State.EGT, State.EGT, State.EGT, State.Z_8, State.EGT, State.EGT, State.EGT, State.EGT, State.EGT, State.EGT },
            /*Z_6*/ { State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs, State.EAs },
            /*Z_7*/ { State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE, State.ELE },
            /*Z_8*/ { State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE, State.EGE },
            /*Z_9*/ { State.EIK, State.Z_1, State.Z_9, State.EIK, State.EIK, State.EIK, State.EIK, State.EIK, State.Z_9, State.EIK, State.EIK },
            /*Z10*/ { State.ESy, State.ESy, State.ESy, State.ESy, State.ESy, State.ESy, State.ESy, State.ESy, State.ESy, State.ESy, State.Z11 },
            /*Z11*/ { State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z12 },
            /*Z12*/ { State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z11, State.Z_0, State.Z11 },
    };

    private static final Action[][] actionTable = {
            /*       SYMBOL       DIGIT       ALPHA       COLON       EQUALS      LESS        GREATER     OTHER       ALPHA_KS    SLASH       STAR */
            /*Z_0*/ { Action.WRE, Action.W_R, Action.W_R, Action.W_R, Action.WRE, Action.W_R, Action.W_R, Action.REA, Action.W_R, Action.W_R, Action.WRE },
            /*Z_1*/ { Action.END, Action.W_R, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.W_R, Action.END, Action.END },
            /*Z_2*/ { Action.END, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_3*/ { Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_4*/ { Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_5*/ { Action.END, Action.END, Action.END, Action.END, Action.W_R, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_6*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_7*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_8*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.END },
            /*Z_9*/ { Action.END, Action.W_R, Action.W_R, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.W_R, Action.END, Action.END },
            /*Z10*/ { Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.END, Action.REA, Action.END, Action.END, Action.REA },
            /*Z11*/ { Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.CLR },
            /*Z12*/ { Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.REA, Action.CLR, Action.REA },
    };

    private FileReader reader;

    private int readRow = 1;
    private int readColumn = 0;
    private int tokenRow;
    private int tokenColumn;


    private Token current;
    private Token next;

    private State currentState;

    private boolean eof = false;

    private char currentChar;
    private String currentString;

    /**
     * Constructor
     * @param reader the reader from which to read
     */
    public Lexer(FileReader reader){
        this.reader = reader;
        read();
        lex();
    }

    /**
     * Lexes the next token
     */
    public void lex(){
        current = next;

        if(eof) {
            next = Token.EOF_TOKEN;
            return;
        }

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
                case CLR:
                    clear();
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

    Token getCurrentToken(){
        return current;
    }

    /**
     * Returns the next token
     * @return the next token
     */
    public Token getNextToken(){
        return next;
    }

    private void read(){
        try {
            int readValue = reader.read();
            readColumn++;
            if(readValue == -1)
                eof = true;
            else {
                currentChar = (char) readValue;
                if(currentChar == '\n'){
                    readRow++;
                    readColumn = 0;
                }
                if(currentChar == '\r')
                    readColumn = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void write(){
        char upper = Character.isAlphabetic(currentChar) ? Character.toUpperCase(currentChar) : currentChar;

        if(currentString.equals("")){
            tokenRow = readRow;
            tokenColumn = readColumn;
        }

        currentString = currentString + upper;
    }

    private void end(){
        try {
            switch (currentState) {
                case EIK:
                    if (isKeyword(currentString)) {
                        next = new Token(TokenType.KEYWORD, SpecialCharacter.stringCharacterMap.get(currentString), tokenRow, tokenColumn);
                        break;
                    }
                case EId:
                    next = new Token(TokenType.IDENTIFIER, currentString, tokenRow, tokenColumn);
                    break;
                case ENu:
                    next = new Token(TokenType.NUMERAL, Long.parseLong(currentString), tokenRow, tokenColumn);
                    break;
                case ESy:
                case EAs:
                case ECo:
                case ELE:
                case ELT:
                case EGE:
                case EGT:
                    next = new Token(TokenType.SYMBOL, getOperatorChar(currentString), tokenRow, tokenColumn);
                    break;
            }
        }
        catch (InvalidTokenTypeException e){
            e.printStackTrace();
            System.err.println("Encountered InvalidTokenTypeException at " + tokenRow + ":" + tokenColumn);
            System.exit(-1);
        }
    }

    private void clear() {
        currentString = "";
    }

    private static CharacterType getCharacterType(char character){
        if (characterTypeMap.containsKey(character))
            return characterTypeMap.get(character);

        return CharacterType.OTHER;
    }

    private static char getOperatorChar(String string){
        if (string.equals("<="))
            return SpecialCharacter.LESS_OR_EQUAL.value;
        if (string.equals(":="))
            return SpecialCharacter.ASSIGN.value;
        if (string.equals(">="))
            return SpecialCharacter.GREATER_OR_EQUAL.value;

        return string.charAt(0);
    }

    private static boolean isKeyword(String string){
        return SpecialCharacter.stringCharacterMap.get(string) != null;
    }
}
