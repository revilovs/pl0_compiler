package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.NameList;

public class Parser {
    private Lexer lexer;
    private NameList nameList = new NameList();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public void parse() throws SemanticRoutineException {
        parse(Graph.PROGRAM);
    }

    private void parse(Graph graph) throws SemanticRoutineException {
        boolean success = false;
        Arc currentArc = graph.getArcs()[0];

        Token nextToken;

        while (! (nextToken = lexer.getNextToken()).equals(Token.EOF_TOKEN)){
            switch (currentArc.getArcType()){
                case NIL:
                    success = true;
                    break;
                case SYMBOL:
                    success = (nextToken.getCharValue() == currentArc.getSymbolValue());
                    break;
                case IDENTIFIER_OR_NUMERAL:
                    success = (nextToken.getType().equals(currentArc.getTokenType()));
                    break;
                case GRAPH:
                    try {
                        parse(currentArc.getGraph());
                        success = true;
                    }
                    catch (UnexpectedTokenException e) {
                        success = false;
                    }
                    break;
                case END:
                    return;
            }

            if (success && currentArc.getSemanticRoutine() != null){
                currentArc.getSemanticRoutine().apply(this);
            }

            if (!success){
                if (currentArc.getAlternative() != Arc.NO_ALTERNATIVE)
                    currentArc = graph.getArcs()[currentArc.getAlternative()];
                else
                    throw new UnexpectedTokenException(lexer.getNextToken());
            }

            else {
                if (currentArc.getArcType() == ArcType.SYMBOL
                        || currentArc.getArcType() == ArcType.IDENTIFIER_OR_NUMERAL){
                    lexer.lex();
                }

                currentArc = graph.getArcs()[currentArc.getNext()];
            }
        }

        //EOF encountered
        if(!currentArc.getArcType().equals(ArcType.END))
            throw new UnexpectedTokenException(nextToken);
    }

    protected Lexer getLexer() {
        return lexer;
    }

    protected NameList getNameList() {
        return nameList;
    }
}
