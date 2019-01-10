package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.NameList;

import java.io.IOException;

public class Parser {
    private Lexer lexer;
    private NameList nameList = new NameList();
    private CodeGenerator codeGenerator;

    public Parser(Lexer lexer, CodeGenerator codeGenerator) {
        this.lexer = lexer;
        this.codeGenerator = codeGenerator;
    }

    public void parse() throws FatalSemanticRoutineException, IOException {
        try {
            parse(Graph.PROGRAM);
        } catch (BacktrackableUnexpectedTokenException e) {
            throw new UnexpectedTokenException(lexer.getNextToken());
        }
    }

    private void parse(Graph graph) throws FatalSemanticRoutineException, IOException, BacktrackableUnexpectedTokenException {
        boolean success = false;
        boolean backTrackPossible = true;

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
                    catch (BacktrackableUnexpectedTokenException e) {
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
                else if (backTrackPossible)
                    throw new BacktrackableUnexpectedTokenException(lexer.getNextToken());
                else
                    throw new UnexpectedTokenException(lexer.getNextToken());
            }

            else {
                if (currentArc.getArcType() == ArcType.SYMBOL
                        || currentArc.getArcType() == ArcType.IDENTIFIER_OR_NUMERAL){
                    lexer.lex();
                    backTrackPossible = false;
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

    protected CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }
}
