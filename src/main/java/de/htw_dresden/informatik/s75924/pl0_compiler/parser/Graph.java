package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Lexer;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.SpecialCharacter;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.Token;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.TokenType;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.*;

/**
 * An enumeration for all syntax graphs of the PL/0 language, made up of Arcs
 */
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
    CONDITION,
    ARRAY_INDEX;

    /*
      This is necessary because of the circular reference between the graphs
      e.g. EXPRESSION -> TERM -> FACTOR -> EXPRESSION
      Therefore the arcs must be assigned after declaration
     */
    static {
        PROGRAM.arcs = new Arc[] {
                /* 0 */ new Arc(BLOCK, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc('.',
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    NameList nameList = parser.getNameList();

                    codeGenerator.writeConstantBlock(nameList.getConstantBlock());
                    codeGenerator.writeNumberOfProcedures(nameList.getNumberOfProcedures());
                    codeGenerator.close();
                },
                2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };
        
        BLOCK.arcs = new Arc[] {
                /*  0 */ new Arc(Graph.CONST_DECLARATION_LIST, null, 1, 2),
                /*  1 */ new Arc(3),
                /*  2 */ new Arc(3),
                /*  3 */ new Arc(Graph.VAR_DECLARATION_LIST, null, 4, 5),
                /*  4 */ new Arc(6),
                /*  5 */ new Arc(6),
                /*  6 */ new Arc(Graph.PROCEDURE_DECLARATION, null, 7, 8),
                /*  7 */ new Arc(6),
                /*  8 */ new Arc(9,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    NameList nameList = parser.getNameList();

                    int procedureIndex = nameList.getCurrentProcedureIndex();
                    int variableLength = nameList.getVariableLength();

                    codeGenerator.generateProcedureEntry(procedureIndex, variableLength);
                }),
                /*  9 */ new Arc(Graph.STATEMENT,
                parser -> {
                    parser.getCodeGenerator().generateProcedureReturn();

                    parser.getNameList().endProcedure();
                },
                10, Arc.NO_ALTERNATIVE),
                /* 10 */ Arc.END_ARC
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
                /* 1 */ new Arc(TokenType.IDENTIFIER,
                parser -> {
                    NameList nameList = parser.getNameList();
                    Token token = parser.getLexer().getNextToken();

                    nameList.addProcedure(token);
                },
                2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(';', null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(BLOCK, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(';', null, 5, Arc.NO_ALTERNATIVE),
                /* 5 */ Arc.END_ARC
        };
        
        CONST_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER,
                parser -> {
                    NameList nameList = parser.getNameList();
                    Token token = parser.getLexer().getNextToken();

                    nameList.setConstantName(token);
                },
                1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc('=', null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(TokenType.NUMERAL,
                parser -> {
                    NameList nameList = parser.getNameList();
                    long value = parser.getLexer().getNextToken().getNumberValue();

                    nameList.addConstant(value);
                },
                3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };
        
        VAR_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER,
                parser -> {
                    NameList nameList = parser.getNameList();
                    Token token = parser.getLexer().getNextToken();

                    nameList.addVariable(token);
                },
                1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc('[', null, 2, 4),
                /* 2 */ new Arc(TokenType.NUMERAL,
                parser -> {
                    long arraySize = parser.getLexer().getNextToken().getNumberValue();

                    parser.getNameList().makeVariableArray(arraySize);
                },
                3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(']', null, 5, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(5),
                /* 5 */ Arc.END_ARC
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
                /* 0 */ new Arc(TokenType.IDENTIFIER,
                parser -> parser.getNameList().setLastVariableName(parser.getLexer().getNextToken().getStringValue()),
                1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(ARRAY_INDEX, null, 3, 2),
                /* 2 */ new Arc(3,
                parser -> {
                    NameList nameList = parser.getNameList();
                    Lexer lexer = parser.getLexer();

                    String identifier = nameList.getLastVariableName();

                    NameListEntry entry = nameList.findIdentifier(identifier);
                    if (entry!= null){
                        if (entry instanceof VariableEntry){
                            VariableEntry variableEntry = (VariableEntry) entry;

                            int displacement = variableEntry.getRelativeAddress();
                            int procedureIndex = variableEntry.getProcedureIndex();
                            boolean isLocal = nameList.entryIsLocal(variableEntry);
                            boolean isMain = nameList.entryIsInMain(variableEntry);

                            parser.getCodeGenerator()
                                    .generatePushVariableAddress(displacement, procedureIndex, isLocal, isMain);
                        }
                        else
                            throw new InvalidIdentifierException(lexer.getNextToken(), "Identifier is not a variable");
                    }
                    else
                        throw new InvalidIdentifierException(lexer.getNextToken(), "Identifier not found");
                }),
                /* 3 */ new Arc(SpecialCharacter.ASSIGN.value, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(EXPRESSION,
                parser -> parser.getCodeGenerator().generateStoreValue(), 5, Arc.NO_ALTERNATIVE),
                /* 5 */ Arc.END_ARC
        };

        CONDITIONAL_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.IF.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONDITION,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.saveCurrentAddress();
                    codeGenerator.generatePreliminaryJNOT();
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(SpecialCharacter.THEN.value, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(STATEMENT, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(SpecialCharacter.ELSE.value,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.completeIFJNOT(true);
                    codeGenerator.saveCurrentAddress();
                    codeGenerator.generatePreliminaryELSEJUMP();
                }, 5, 6),
                /* 5 */ new Arc(Graph.STATEMENT,
                parser -> parser.getCodeGenerator().completeELSEJUMP(),
                7, Arc.NO_ALTERNATIVE),
                /* 6 */ new Arc(7,
                parser -> parser.getCodeGenerator().completeIFJNOT(false)),
                /* 7 */ Arc.END_ARC
        };

        LOOP_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.WHILE.value,
                parser -> parser.getCodeGenerator().saveCurrentAddress(), 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONDITION,
                parser -> {
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.saveCurrentAddress();
                    codeGenerator.generatePreliminaryJNOT();
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(SpecialCharacter.DO.value, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(STATEMENT,
                parser -> parser.getCodeGenerator().completeWHILE(), 4, Arc.NO_ALTERNATIVE),
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
                /* 1 */ new Arc(TokenType.IDENTIFIER,
                parser -> {
                    Token token = parser.getLexer().getNextToken();
                    String identifier = token.getStringValue();

                    NameList nameList = parser.getNameList();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    NameListEntry entry = nameList.findIdentifier(identifier);

                    if (entry instanceof ProcedureEntry) {
                        ProcedureEntry procedureEntry = (ProcedureEntry) entry;

                        int index = nameList.getIndexOfProcedure(procedureEntry);

                        codeGenerator.generateProcedureCall(index);

                    }
                    else
                        throw new InvalidIdentifierException(token, "Identifier is no procedure name");
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        INPUT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc('?', null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(TokenType.IDENTIFIER,
                (parser) -> parser.getNameList().setLastVariableName(parser.getLexer().getNextToken().getStringValue()),
                2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(ARRAY_INDEX,
                parser -> parser.getCodeGenerator().generateGetValue(),
                4, 3),
                /* 3 */ new Arc(4,
                parser -> {
                    ASSIGNMENT_STATEMENT.arcs[2].getSemanticRoutine().apply(parser);

                    parser.getCodeGenerator().generateGetValue();
                }),
                /* 4 */ Arc.END_ARC
        };

        OUTPUT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc('!', null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(TokenType.STRING,
                parser -> parser.getCodeGenerator().generatePutString(parser.getLexer().getNextToken().getStringValue()),
                3, 2),
                /* 2 */ new Arc(EXPRESSION,
                parser -> parser.getCodeGenerator().generatePutValue(), 3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };

        EXPRESSION.arcs = new Arc[] {
                /* 0 */ new Arc('-', null, 2, 1),
                /* 1 */ new Arc(TERM, null, 3, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(TERM,
                parser -> parser.getCodeGenerator().generateNegativeSign(), 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(4),
                /* 4 */ new Arc('+', null, 5, 6),
                /* 5 */ new Arc(TERM,
                parser -> parser.getCodeGenerator().generateAddOperator(), 3, Arc.NO_ALTERNATIVE),
                /* 6 */ new Arc('-', null, 7, 8),
                /* 7 */ new Arc(TERM,
                parser -> parser.getCodeGenerator().generateSubtractOperator(), 3, Arc.NO_ALTERNATIVE),
                /* 8 */ Arc.END_ARC
        };

        TERM.arcs = new Arc[] {
                /* 0 */ new Arc(FACTOR, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(2),
                /* 2 */ new Arc('*', null, 3, 4),
                /* 3 */ new Arc(FACTOR,
                parser -> parser.getCodeGenerator().generateMultiplyOperator(), 1, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc('/', null, 5, 6),
                /* 5 */ new Arc(FACTOR,
                parser -> parser.getCodeGenerator().generateDivideOperator(), 1, Arc.NO_ALTERNATIVE),
                /* 6 */ Arc.END_ARC
        };

        FACTOR.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.NUMERAL,
                parser -> {
                    NameList nameList = parser.getNameList();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    long constantValue = parser.getLexer().getNextToken().getNumberValue();

                    nameList.addConstant(constantValue);
                    int constantIndex = nameList.getIndexOfConstant(constantValue);

                    codeGenerator.generatePushConstant(constantIndex);

                }, 7, 1),
                /* 1 */ new Arc('(', null, 2, 4),
                /* 2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(')', null, 7, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(TokenType.IDENTIFIER,
                parser -> parser.getNameList().setLastVariableName(parser.getLexer().getNextToken().getStringValue()),
                5, Arc.NO_ALTERNATIVE),
                /* 5 */ new Arc(ARRAY_INDEX,
                parser -> parser.getCodeGenerator().generateSwap(),
                7, 6),
                /* 6 */ new Arc(7,
                parser -> {
                    NameList nameList = parser.getNameList();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    Token token = parser.getLexer().getNextToken();
                    String identifier = nameList.getLastVariableName();

                    NameListEntry entry = nameList.findIdentifier(identifier);
                    if (entry != null){
                        if (entry instanceof VariableEntry){
                            VariableEntry variableEntry = (VariableEntry) entry;

                            boolean isLocal = nameList.entryIsLocal(variableEntry);
                            boolean isMain = nameList.entryIsInMain(variableEntry);
                            int displacement = variableEntry.getRelativeAddress();
                            int procedureIndex = variableEntry.getProcedureIndex();

                            codeGenerator.generatePushVariableValue(displacement, procedureIndex, isLocal, isMain);
                        }

                        else if (entry instanceof ConstantEntry){
                            ConstantEntry constantEntry = (ConstantEntry) entry;

                            int constantIndex = constantEntry.getIndex();

                            codeGenerator.generatePushConstant(constantIndex);
                        }
                        else
                            throw new InvalidIdentifierException(token, "Identifier is not a variable or constant");
                    }
                    else
                        throw new InvalidIdentifierException(token, "Identifier not found");
                }),
                /* 7 */ Arc.END_ARC
        };

        CONDITION.arcs = new Arc[] {
                /*  0 */ new Arc(SpecialCharacter.ODD.value, null, 1, 2),
                /*  1 */ new Arc(EXPRESSION,
                parser -> parser.getCodeGenerator().generateOdd(), 10, Arc.NO_ALTERNATIVE),
                /*  2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /*  3 */ new Arc('=',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 4),
                /*  4 */ new Arc('#',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 5),
                /*  5 */ new Arc('<',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 6),
                /*  6 */ new Arc(SpecialCharacter.LESS_OR_EQUAL.value,
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, 7),
                /*  7 */ new Arc('>',
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()),  9, 8),
                /*  8 */ new Arc(SpecialCharacter.GREATER_OR_EQUAL.value,
                parser -> parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue()), 9, Arc.NO_ALTERNATIVE),
                /*  9 */ new Arc(EXPRESSION,
                parser -> parser.getCodeGenerator().generateComparisonOperator(), 10, Arc.NO_ALTERNATIVE),
                /* 10 */ Arc.END_ARC
        };

        ARRAY_INDEX.arcs = new Arc[] {
                /* 0 */ new Arc('[',
                parser -> {
                    NameList nameList = parser.getNameList();
                    CodeGenerator codeGenerator = parser.getCodeGenerator();
                    Token token = parser.getLexer().getNextToken();

                    String identifier = nameList.getLastVariableName();

                    NameListEntry entry = nameList.findIdentifier(identifier);

                    if (entry != null) {
                        if (entry instanceof VariableEntry) {
                            VariableEntry variableEntry = (VariableEntry) entry;

                            boolean isLocal = nameList.entryIsLocal(variableEntry);
                            boolean isMain = nameList.entryIsInMain(variableEntry);
                            int displacement = variableEntry.getRelativeAddress();
                            int procedureIndex = variableEntry.getProcedureIndex();

                            codeGenerator.generatePushVariableAddress(displacement, procedureIndex, isLocal, isMain);
                        }
                        else
                            throw new InvalidIdentifierException(token, "Indexing only possible on variables");
                    }
                    else
                        throw new InvalidIdentifierException(token, "Identifier not found");
                },
                1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(EXPRESSION, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(']',
                parser -> {
                    NameList nameList = parser.getNameList();
                    nameList.addConstant(4);
                    int index = nameList.getIndexOfConstant(4);

                    CodeGenerator codeGenerator = parser.getCodeGenerator();

                    codeGenerator.generatePushConstant(index);
                    codeGenerator.generateMultiplyOperator();
                    codeGenerator.generateAddOperator();
                },
                3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };
    }

    private Arc[] arcs;

    public Arc[] getArcs() {
        return arcs;
    }
}
