package de.htw_dresden.informatik.s75924.pl0_compiler.lexer;

import java.util.Objects;

/**
 * Class for a Token generated by the Lexer
 */
public class Token {

    private TokenType type;
    private String stringValue = null;
    private long numberValue = 0;
    private char charValue = 0;

    private int row;
    private int column;

    public static final Token EOF_TOKEN = new Token();

    private Token(int row, int column){
        this.row = row;
        this.column = column;
    }

    /**
     * Constructs a NUMERAL Token
     * @param type should be NUMERAL
     * @param numberValue the value of the numeral
     * @param row the row of the token
     * @param column the column of the token
     * @throws InvalidTokenTypeException if the wrong TokenType is specified
     */
    public Token(TokenType type, long numberValue, int row, int column) throws InvalidTokenTypeException {
        this(row, column);
        if (type != TokenType.NUMERAL){
            throw new InvalidTokenTypeException();
        }
        this.type = type;
        this.numberValue = numberValue;
    }

    /**
     * Constructs a KEYWORD or SYMBOL token
     * @param type should be KEYWORD or symbol
     * @param charValue the character value of the keyword or symbol
     * @param row the row of the token
     * @param column the column of the token
     * @throws InvalidTokenTypeException if the wrong TokenType is specified
     */
    public Token(TokenType type, char charValue, int row, int column) throws InvalidTokenTypeException {
        this(row, column);
        if (type != TokenType.KEYWORD && type != TokenType.SYMBOL) {
            throw new InvalidTokenTypeException();
        }
        this.type = type;
        this.charValue = charValue;
    }

    /**
     * Constructs an IDENTIFIER Token
     * @param type should be IDENTIFIER
     * @param stringValue the identifier name
     * @param row the row of the token
     * @param column the column of the token
     * @throws InvalidTokenTypeException if the wrong TokenType is specified
     */
    public Token(TokenType type, String stringValue, int row, int column) throws InvalidTokenTypeException {
        this(row, column);
        if (type != TokenType.IDENTIFIER)
            throw new InvalidTokenTypeException();

        this.type = type;
        this.stringValue = stringValue;
    }

    private Token(){
        type = TokenType.EOF;
    }

    /**
     * Returns the token's type
     * @return the token's type
     */
    public TokenType getType() {
        return type;
    }

    /**
     * Returns the token's string value
     * @return the token's string value
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * Returns the token's number value
     * @return the token's number value
     */
    public long getNumberValue() {
        return numberValue;
    }

    /**
     * Returns the token's character value
     * @return the token's character value
     */
    public char getCharValue() {
        return charValue;
    }

    int getRow() {
        return row;
    }

    int getColumn() {
        return column;
    }

    /**
     * Converts the token to a string
     * @return a string containing the token's type, value, row and column
     */
    @Override
    public String toString() {
        String value;
        switch (type){
            case KEYWORD:
                value = SpecialCharacter.characterStringMap.get(charValue);
                break;
            case SYMBOL:
                value = "" + getCharValue();
                break;
            case IDENTIFIER:
                value = getStringValue();
                break;
            case NUMERAL:
                value = "" + getNumberValue();
                break;
            case EOF:
                return "EOF";
            default:
                value = "undefined";
        }
        return type.toString() + " " + value + " at " + row + ":" + column;
    }

    /**
     * Checks if other's type, value, row and column are equal
     * @param other the object to be compared
     * @return true if and only if type, value, row and column correspond
     */
    @Override
    public boolean equals(Object other){
        if(! (other instanceof Token))
            return false;
        Token otherToken = (Token) other;

        return type == otherToken.type
                && (Objects.equals(stringValue, otherToken.stringValue))
                && charValue == otherToken.charValue
                && numberValue == ((Token) other).numberValue
                && row == otherToken.row
                && column == otherToken.column ;
    }
}
