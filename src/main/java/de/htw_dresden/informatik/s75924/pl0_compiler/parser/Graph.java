package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.SpecialCharacter;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.TokenType;

public enum Graph {
    PROGRAM,
    BLOCK,
    CONST_DECLARATION_LIST,
    VAR_DECLARATION_LIST,
    PROCEDURE_DECLARATION,
    CONST_DECLARATION,
    VAR_DECLARATION,
    STATEMENT,
    ASSIGNMENT_STATEMENT,
    CONDITIONAL_STATEMENT,
    LOOP_STATEMENT,
    COMPOUND_STATEMENT,
    PROCEDURE_CALL,
    INPUT_STATEMENT,
    OUTPUT_STATEMENT,
    EXPRESSION,
    TERM,
    FACTOR,
    CONDITION;

    /*
      This is necessary because of the circular reference between the graphs
      e.g. EXPRESSION -> TERM -> FACTOR -> EXPRESSION
      Therefore the arcs must be assigned after declaration
     */
    static {
        PROGRAM.arcs = new Arc[] {
                /* 0 */ new Arc(BLOCK, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc('.', null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };
        
        BLOCK.arcs = new Arc[] {
                /* 0 */ new Arc(CONST_DECLARATION_LIST, null, 2, 1),
                /* 1 */ new Arc(2),
                /* 2 */ new Arc(VAR_DECLARATION_LIST, null, 4, 3),
                /* 3 */ new Arc(4),
                /* 4 */ new Arc(PROCEDURE_DECLARATION, null, 4, 5),
                /* 5 */ new Arc(STATEMENT, null, 6, Arc.NO_ALTERNATIVE),
                /* 6 */ Arc.END_ARC
        };
        
        CONST_DECLARATION_LIST.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.CONST.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONST_DECLARATION, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(',', null, 1, 3),
                /* 3 */ new Arc(';', null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ Arc.END_ARC
        };
        
        VAR_DECLARATION_LIST.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.VAR.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(VAR_DECLARATION, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(',', null, 1, 3),
                /* 3 */ new Arc(';', null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ Arc.END_ARC
        };
        
        PROCEDURE_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.PROCEDURE.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(TokenType.IDENTIFIER, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(';', null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(BLOCK, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(';', null, 5, Arc.NO_ALTERNATIVE),
                /* 5 */ Arc.END_ARC
        };
        
        CONST_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc('=', null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(TokenType.NUMERAL, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };
        
        VAR_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ Arc.END_ARC
        };
        
        STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(ASSIGNMENT_STATEMENT, null, 7, 1),
                /* 1 */ new Arc(CONDITIONAL_STATEMENT, null, 7, 2),
                /* 2 */ new Arc(LOOP_STATEMENT, null, 7, 3),
                /* 3 */ new Arc(COMPOUND_STATEMENT, null, 7, 4),
                /* 4 */ new Arc(PROCEDURE_CALL, null, 7, 5),
                /* 5 */ new Arc(INPUT_STATEMENT, null, 7, 6),
                /* 6 */ new Arc(OUTPUT_STATEMENT, null, 7, Arc.NO_ALTERNATIVE),
                /* 7 */ Arc.END_ARC
        };

        ASSIGNMENT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(SpecialCharacter.ASSIGN.value, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };

        CONDITIONAL_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.IF.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONDITION, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(SpecialCharacter.THEN.value, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(STATEMENT, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ Arc.END_ARC
        };

        LOOP_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.WHILE.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONDITION, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(SpecialCharacter.DO.value, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(STATEMENT, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ Arc.END_ARC
        };

        COMPOUND_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.BEGIN.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(STATEMENT, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(';', null, 1, 3),
                /* 3 */ new Arc(SpecialCharacter.END.value, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ Arc.END_ARC
        };

        PROCEDURE_CALL.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.CALL.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(TokenType.IDENTIFIER, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        INPUT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc('?', null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(TokenType.IDENTIFIER, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        OUTPUT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc('!', null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(EXPRESSION, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        EXPRESSION.arcs = new Arc[] {
                /* 0 */ new Arc('-', null, 2, 1),
                /* 1 */ new Arc(TERM, null, 3, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(TERM, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(4),
                /* 4 */ new Arc('+', null, 5, 6),
                /* 5 */ new Arc(TERM, null, 3, Arc.NO_ALTERNATIVE),
                /* 6 */ new Arc('-', null, 7, 8),
                /* 7 */ new Arc(TERM, null, 3, Arc.NO_ALTERNATIVE),
                /* 8 */ Arc.END_ARC
        };

        TERM.arcs = new Arc[] {
                /* 0 */ new Arc(FACTOR, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(2),
                /* 2 */ new Arc('*', null, 3, 4),
                /* 3 */ new Arc(FACTOR, null, 1, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc('/', null, 5, 6),
                /* 5 */ new Arc(FACTOR, null, 1, Arc.NO_ALTERNATIVE),
                /* 6 */ Arc.END_ARC
        };

        FACTOR.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.NUMERAL, null, 5, 1),
                /* 1 */ new Arc('(', null, 2, 4),
                /* 2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(')', null, 5, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(TokenType.IDENTIFIER, null, 5, Arc.NO_ALTERNATIVE),
                /* 5 */ Arc.END_ARC
        };

        CONDITION.arcs = new Arc[] {
                /*  0 */ new Arc(SpecialCharacter.ODD.value, null, 1, 2),
                /*  1 */ new Arc(EXPRESSION, null, 10, Arc.NO_ALTERNATIVE),
                /*  2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /*  3 */ new Arc('=', null, 9, 4),
                /*  4 */ new Arc('#', null, 9, 5),
                /*  5 */ new Arc('<', null, 9, 6),
                /*  6 */ new Arc(SpecialCharacter.LESS_OR_EQUAL.value, null, 9, 7),
                /*  7 */ new Arc('>', null, 9, 8),
                /*  8 */ new Arc(SpecialCharacter.GREATER_OR_EQUAL.value, null, 9, Arc.NO_ALTERNATIVE),
                /*  9 */ new Arc(EXPRESSION, null, 10, Arc.NO_ALTERNATIVE),
                /* 10 */ Arc.END_ARC
        };
    }

    private Arc[] arcs;

    public Arc[] getArcs() {
        return arcs;
    }
}