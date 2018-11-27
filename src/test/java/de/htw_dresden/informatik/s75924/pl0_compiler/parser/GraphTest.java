package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class GraphTest {
    @Test
    public void allGraphsHaveArcs() {
        for (Graph g: Graph.values()) {
            assertNotNull(g.getArcs());
        }
    }
}