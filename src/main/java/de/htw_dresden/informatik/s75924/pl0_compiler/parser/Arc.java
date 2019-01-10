package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.TokenType;

class Arc {
    private ArcType type;

    private char symbolValue;
    private Graph graph = null;
    private TokenType tokenType = null;

    private SemanticRoutine semanticRoutine = null;

    private int next;
    private int alternative;

    static final int NO_ALTERNATIVE = -1;

    /**
     * Constructs a Symbol bow for keywords or operators
     * @param symbolValue The keyword's or operator's symbol value
     * @param semanticRoutine The arc's semantic routine
     * @param next index of the next arc
     * @param alternative index of alternative arc
     */
    Arc(char symbolValue, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ArcType.SYMBOL;
        this.symbolValue = symbolValue;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Constructs a new Graph Arc
     * @param graph The arc's graph
     * @param semanticRoutine The arc's semantic routine
     * @param next index of the next arc
     * @param alternative index of alternative arc
     */
    Arc(Graph graph, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ArcType.GRAPH;
        this.graph = graph;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Constructs a new Identifier or numeral arc
     * @param tokenType The Token type (identifier or numeral)
     * @param semanticRoutine The arc's semantic routine
     * @param next index of the next arc
     * @param alternative index of alternative arc
     */
    Arc(TokenType tokenType, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ArcType.IDENTIFIER_OR_NUMERAL;
        this.tokenType = tokenType;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Constructs a NIL Arc without a semantic routine
     * @param next the index of the next arc
     */
    Arc(int next) {
        this.type = ArcType.NIL;
        this.alternative = NO_ALTERNATIVE;
        this.next = next;
    }

    /**
     * Constructs a NIL Arc with a semantic routine
     * @param next the index of the next arc
     * @param semanticRoutine The arc's semantic routine
     */
    Arc(int next, SemanticRoutine semanticRoutine) {
        this.type = ArcType.NIL;
        this.alternative = NO_ALTERNATIVE;
        this.next = next;
        this.semanticRoutine = semanticRoutine;
    }

    /**
     * Constructs an END Arc
     */
    private Arc() {
        this.type = ArcType.END;
        this.next = -1;
        this.alternative = NO_ALTERNATIVE;
    }

    static final Arc END_ARC = new Arc();

    ArcType getArcType() {
        return type;
    }

    char getSymbolValue() {
        return symbolValue;
    }

    Graph getGraph() {
        return graph;
    }

    TokenType getTokenType() {
        return tokenType;
    }

    SemanticRoutine getSemanticRoutine() {
        return semanticRoutine;
    }

    int getNext() {
        return next;
    }

    int getAlternative() {
        return alternative;
    }
}
