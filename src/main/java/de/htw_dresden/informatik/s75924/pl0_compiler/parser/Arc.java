package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.TokenType;

public class Arc {
    private ArcType type;

    private char symbolValue;
    private Graph graph = null;
    private TokenType tokenType = null;

    private SemanticRoutine semanticRoutine = null;

    private int next;
    private int alternative;

    public static final int NO_ALTERNATIVE = -1;

    /**
     * Constructs a Symbol bow for keywords or operators
     * @param symbolValue The keyword's or operator's symbol value
     * @param semanticRoutine The arc's semantic routine
     * @param next index of the next arc
     * @param alternative index of alternative arc
     */
    public Arc(char symbolValue, SemanticRoutine semanticRoutine, int next, int alternative) {
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
    public Arc(Graph graph, SemanticRoutine semanticRoutine, int next, int alternative) {
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
    public Arc(TokenType tokenType, SemanticRoutine semanticRoutine, int next, int alternative) {
        this.type = ArcType.IDENTIFIER_OR_NUMERAL;
        this.tokenType = tokenType;
        this.semanticRoutine = semanticRoutine;
        this.next = next;
        this.alternative = alternative;
    }

    /**
     * Constructs a NIL Arc
     * @param next the index of the next arc
     */
    public Arc(int next) {
        this.type = ArcType.NIL;
        this.alternative = NO_ALTERNATIVE;
        this.next = next;
    }

    /**
     * Constructs an END Arc
     */
    private Arc() {
        this.type = ArcType.END;
        this.next = -1;
        this.alternative = NO_ALTERNATIVE;
    }

    public static final Arc END_ARC = new Arc();

    public ArcType getArcType() {
        return type;
    }

    public char getSymbolValue() {
        return symbolValue;
    }

    public Graph getGraph() {
        return graph;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public SemanticRoutine getSemanticRoutine() {
        return semanticRoutine;
    }

    public int getNext() {
        return next;
    }

    public int getAlternative() {
        return alternative;
    }
}
