public class Token {
    enum TokenType {
        KEYWORD, NUMERAL, OPERATOR, IDENTIFIER, EOF
    }
    private TokenType type;
    private String stringValue;
    private long numberValue;

    public static final Token EOF_TOKEN = new Token();

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
            default:
                value = "undefined";

        }
        return type.toString() + " " + value;
    }
}
