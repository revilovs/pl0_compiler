package de.htw_dresden.informatik.s75924.pl0_compiler;

public class Token {
    enum TokenType {
        KEYWORD, NUMERAL, OPERATOR, IDENTIFIER, EOF
    }
    private TokenType type;
    private String stringValue;
    private long numberValue;

    private int row;
    private int column;

    public static final Token EOF_TOKEN = new Token();

    public Token(TokenType type, long numberValue, int row, int column){
        if (type != TokenType.NUMERAL){
            //TODO: fix
            return;
        }
        this.type = type;
        this.numberValue = numberValue;
        this.row = row;
        this.column = column;
    }

    public Token(TokenType type, String stringValue, int row, int column){
        if (type == TokenType.NUMERAL){
            //TODO: fix
            return;
        }
        this.type = type;
        this.stringValue = stringValue;
        this.row = row;
        this.column = column;
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
            case OPERATOR:
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
        return type.toString() + " " + value + " at " + row + "," + column;
    }
}
