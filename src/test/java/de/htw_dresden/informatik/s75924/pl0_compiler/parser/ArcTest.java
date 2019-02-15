package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArcTest {
    @Test
    public void symbolConstructorInitialisesCorrectly() {
        Arc sut = new Arc(',', null, 3, 7);
        assertEquals(ArcType.SYMBOL, sut.getArcType());
        assertEquals(',',sut.getSymbolValue());
        assertNull(sut.getGraph());
        assertNull(sut.getTokenType());
        assertNull(sut.getSemanticRoutine());
        assertEquals(3, sut.getNext());
        assertEquals(7, sut.getAlternative());
    }

    @Test
    public void graphConstructorInitialisesCorrectly() {
        Arc sut = new Arc(Graph.EXPRESSION, null, 3, Arc.NO_ALTERNATIVE);
        assertEquals(ArcType.GRAPH, sut.getArcType());
        assertEquals(0,sut.getSymbolValue());
        assertEquals(Graph.EXPRESSION, sut.getGraph());
        assertNull(sut.getTokenType());
        assertNull(sut.getSemanticRoutine());
        assertEquals(3, sut.getNext());
        assertEquals(Arc.NO_ALTERNATIVE, sut.getAlternative());
    }

    @Test
    public void tokenTypeConstructorInitialisesCorrectly() {
        Arc sut = new Arc(TokenType.NUMERAL, null, 1, Arc.NO_ALTERNATIVE);
        assertEquals(ArcType.IDENTIFIER_OR_NUMERAL_OR_STRING, sut.getArcType());
        assertEquals(0,sut.getSymbolValue());
        assertNull(sut.getGraph());
        assertEquals(TokenType.NUMERAL, sut.getTokenType());
        assertNull(sut.getSemanticRoutine());
        assertEquals(1, sut.getNext());
        assertEquals(Arc.NO_ALTERNATIVE, sut.getAlternative());
    }

    @Test
    public void nilConstructorInitialisesCorrectly() {
        Arc sut = new Arc(5);
        assertEquals(ArcType.NIL, sut.getArcType());
        assertEquals(0,sut.getSymbolValue());
        assertNull(sut.getGraph());
        assertNull(sut.getTokenType());
        assertNull(sut.getSemanticRoutine());
        assertEquals(5, sut.getNext());
        assertEquals(Arc.NO_ALTERNATIVE, sut.getAlternative());
    }

    @Test
    public void endArcIsCorrect() {
        Arc sut = Arc.END_ARC;
        assertEquals(ArcType.END, sut.getArcType());
        assertEquals(0,sut.getSymbolValue());
        assertNull(sut.getGraph());
        assertNull(sut.getTokenType());
        assertNull(sut.getSemanticRoutine());
        assertEquals(Arc.NO_ALTERNATIVE, sut.getNext());
        assertEquals(Arc.NO_ALTERNATIVE, sut.getAlternative());
    }
}