public class Token {
    enum TokenType {
        EMPTY, KEYWORD, NUMERAL, OPERATOR, IDENTIFIER
    }
    private TokenType type;
    private String stringValue;
    private long numberValue;

    public Token(TokenType type, long numberValue){
        if (type != TokenType.NUMERAL){
            //TODO: fix
            return;
        }
        this.type = type;
        this.numberValue = numberValue;
    }

    public Token(TokenType type, String stringValue){
        if (type == TokenType.NUMERAL){
            //TODO: fix
            return;
        }
        this.type = type;
        this.stringValue = stringValue;
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
}
