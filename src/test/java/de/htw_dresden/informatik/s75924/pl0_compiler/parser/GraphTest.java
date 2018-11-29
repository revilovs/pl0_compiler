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

    @Test
    public void noInvalidIndices() {
        for (Graph g : Graph.values()) {
            int len = g.getArcs().length;

            for (Arc arc: g.getArcs()) {
                assertTrue(arc.getNext() < len);
                assertTrue(arc.getAlternative() < len);
            }
        }
    }
}