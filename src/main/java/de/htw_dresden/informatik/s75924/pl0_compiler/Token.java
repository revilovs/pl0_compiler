package de.htw_dresden.informatik.s75924.pl0_compiler;

public class Token {
    public enum TokenType {
        KEYWORD, NUMERAL, SYMBOL, IDENTIFIER, EOF
    }

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

    public Token(TokenType type, char charValue, int row, int column) throws InvalidTokenTypeException {
        this(row, column);
        if (type != TokenType.KEYWORD && type != TokenType.SYMBOL) {
            throw new InvalidTokenTypeException();
        }
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
