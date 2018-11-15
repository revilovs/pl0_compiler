package de.htw_dresden.informatik.s75924.pl0_compiler;

public class Token {
    public enum TokenType {
        KEYWORD, NUMERAL, SYMBOL, IDENTIFIER, EOF
    }

    public static final char ASSIGN = 128;
    public static final char LESS_OR_EQUAL = 129;
    public static final char GREATER_OR_EQUAL = 130;
    public static final char KW_BEGIN = 131;
    public static final char KW_CALL = 132;
    public static final char KW_CONST = 133;
    public static final char KW_DO = 134;
    public static final char KW_END = 135;
    public static final char KW_IF = 136;
    public static final char KW_ODD = 137;
    public static final char KW_PROCEDURE = 138;
    public static final char KW_THEN = 139;
    public static final char KW_VAR = 140;
    public static final char KW_WHILE = 141;

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

    public Token(TokenType type, long numberValue, int row, int column) throws InvalidTokenTypeException {
        this(row, column);
        if (type != TokenType.NUMERAL){
            throw new InvalidTokenTypeException();
        }
        this.type = type;
        this.numberValue = numberValue;
    }

    public Token(TokenType type, char charValue, int row, int column){
        this(row, column);
        this.type = type;
        this.charValue = charValue;
    }

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

    public TokenType getType() {
        return type;
    }

    public String getStringValue() {
        return stringValue;
    }

    public long getNumberValue() {
        return numberValue;
    }

    public char getCharValue() {
        return charValue;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        String value;
        switch (type){
            case KEYWORD:
            case SYMBOL:
            case IDENTIFIER:
                value = getStringValue();
                break;
            case NUMERAL:
                value = "" + getNumberValue();
                break;
            case EOF:
                value = "End of file";
                break;
            default:
                value = "undefined";

        }
        return type.toString() + " " + value + " at " + row + ":" + column;
    }

    @Override
    public boolean equals(Object other){
        if(! (other instanceof Token))
            return false;
        Token otherToken = (Token) other;

        return type == otherToken.type
                && (stringValue == null ? otherToken.stringValue == null : stringValue.equals(otherToken.stringValue))
                && charValue == otherToken.charValue
                && numberValue == ((Token) other).numberValue;
    }
}
