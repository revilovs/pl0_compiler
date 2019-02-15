package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.NameList;

import java.io.IOException;

/**
 * The centerpiece of the compiler. The Parser checks the grammar and calls the semantic routines for the NameList and CodeGenerator
 */
public class Parser {
    private Lexer lexer;
    private NameList nameList = new NameList();
    private CodeGenerator codeGenerator;

    /**
     * Constructor
     * @param lexer the lexer from which to get the tokens
     * @param codeGenerator the code generator with which to generate the code
     */
    public Parser(Lexer lexer, CodeGenerator codeGenerator) {
        this.lexer = lexer;
        this.codeGenerator = codeGenerator;
    }

    /**
     * Parses the complete program
     * @throws FatalSemanticRoutineException if the source code contains an error
     * @throws IOException if an I/O error occurs
     */
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
                case IDENTIFIER_OR_NUMERAL_OR_STRING:
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
                        || currentArc.getArcType() == ArcType.IDENTIFIER_OR_NUMERAL_OR_STRING){
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

    Lexer getLexer() {
        return lexer;
    }

    NameList getNameList() {
        return nameList;
    }

    CodeGenerator getCodeGenerator() {
        return codeGenerator;
    }
}
