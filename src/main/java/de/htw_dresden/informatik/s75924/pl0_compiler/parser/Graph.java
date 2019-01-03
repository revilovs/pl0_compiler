package de.htw_dresden.informatik.s75924.pl0_compiler.parser;

import de.htw_dresden.informatik.s75924.pl0_compiler.code_generation.CodeGenerator;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.SpecialCharacter;
import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.TokenType;
import de.htw_dresden.informatik.s75924.pl0_compiler.namelist.*;

import java.io.IOException;

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
                /* 1 */ new Arc('.',
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws IOException {
                        CodeGenerator codeGenerator = parser.getCodeGenerator();
                        NameList nameList = parser.getNameList();

                        codeGenerator.writeConstantBlock(nameList.getConstantBlock());
                        codeGenerator.writeNumberOfProcedures(nameList.getNumberOfProcedures());
                        codeGenerator.close();
                    }
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
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        CodeGenerator codeGenerator = parser.getCodeGenerator();
                        NameList nameList = parser.getNameList();

                        int procedureIndex = nameList.getCurrentProcedureIndex();
                        int variableLength = nameList.getVariableLength();

                        codeGenerator.generateProcedureEntry(procedureIndex, variableLength);
                    }
                }),
                /*  9 */ new Arc(Graph.STATEMENT,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws IOException {
                        parser.getCodeGenerator().generateProcedureReturn();

                        parser.getNameList().endProcedure();
                    }
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
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException {
                        NameList nameList = parser.getNameList();
                        String procedureName = parser.getLexer().getNextToken().getStringValue();

                        nameList.addProcedure(procedureName);
                    }
                },
                2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(';', null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(BLOCK, null, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(';', null, 5, Arc.NO_ALTERNATIVE),
                /* 5 */ Arc.END_ARC
        };
        
        CONST_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException {
                        NameList nameList = parser.getNameList();
                        String identifier = parser.getLexer().getNextToken().getStringValue();

                        nameList.setConstantName(identifier);
                    }
                },
                1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc('=', null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(TokenType.NUMERAL,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        NameList nameList = parser.getNameList();
                        long value = parser.getLexer().getNextToken().getNumberValue();

                        nameList.addConstant(value);
                    }
                },
                3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };
        
        VAR_DECLARATION.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.IDENTIFIER,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException {
                        NameList nameList = parser.getNameList();
                        String identifier = parser.getLexer().getNextToken().getStringValue();

                        nameList.addVariable(identifier);
                    }
                },
                1, Arc.NO_ALTERNATIVE),
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
                /* 0 */ new Arc(TokenType.IDENTIFIER,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException {
                        NameList nameList = parser.getNameList();

                        String identifier = parser.getLexer().getNextToken().getStringValue();

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
                                throw new InvalidIdentifierException();
                        }
                        else
                            throw new InvalidIdentifierException();
                    }
                }, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(SpecialCharacter.ASSIGN.value, null, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(EXPRESSION,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateStoreValue();
                    }
                }, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ Arc.END_ARC
        };

        CONDITIONAL_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.IF.value, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONDITION,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        CodeGenerator codeGenerator = parser.getCodeGenerator();

                        codeGenerator.saveCurrentAddress();
                        codeGenerator.generatePreliminaryJNOT();
                    }
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(SpecialCharacter.THEN.value, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(STATEMENT,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().completeIFJNOT();
                    }
                }, 4, Arc.NO_ALTERNATIVE),
                /* 4 */ Arc.END_ARC
        };

        LOOP_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc(SpecialCharacter.WHILE.value,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().saveCurrentAddress();
                    }
                }, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(CONDITION,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        CodeGenerator codeGenerator = parser.getCodeGenerator();

                        codeGenerator.saveCurrentAddress();
                        codeGenerator.generatePreliminaryJNOT();
                    }
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(SpecialCharacter.DO.value, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(STATEMENT,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().completeWHILE();
                    }
                }, 4, Arc.NO_ALTERNATIVE),
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
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException {
                        String identifier = parser.getLexer().getNextToken().getStringValue();

                        NameList nameList = parser.getNameList();
                        CodeGenerator codeGenerator = parser.getCodeGenerator();

                        NameListEntry entry = nameList.findIdentifier(identifier);

                        if (entry instanceof ProcedureEntry) {
                            ProcedureEntry procedureEntry = (ProcedureEntry) entry;

                            int index = nameList.getIndexOfProcedure(procedureEntry);

                            codeGenerator.generateProcedureCall(index);

                        }
                        else
                            throw new InvalidIdentifierException();
                    }
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        INPUT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc('?', null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(TokenType.IDENTIFIER,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException, IOException {
                        ASSIGNMENT_STATEMENT.arcs[0].getSemanticRoutine().apply(parser);

                        parser.getCodeGenerator().generateGetValue();
                    }
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        OUTPUT_STATEMENT.arcs = new Arc[] {
                /* 0 */ new Arc('!', null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(EXPRESSION,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generatePutValue();
                    }
                }, 2, Arc.NO_ALTERNATIVE),
                /* 2 */ Arc.END_ARC
        };

        EXPRESSION.arcs = new Arc[] {
                /* 0 */ new Arc('-', null, 2, 1),
                /* 1 */ new Arc(TERM, null, 3, Arc.NO_ALTERNATIVE),
                /* 2 */ new Arc(TERM,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateNegativeSign();
                    }
                }, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(4),
                /* 4 */ new Arc('+', null, 5, 6),
                /* 5 */ new Arc(TERM,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateAddOperator();
                    }
                }, 3, Arc.NO_ALTERNATIVE),
                /* 6 */ new Arc('-', null, 7, 8),
                /* 7 */ new Arc(TERM,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateSubtractOperator();
                    }
                }, 3, Arc.NO_ALTERNATIVE),
                /* 8 */ Arc.END_ARC
        };

        TERM.arcs = new Arc[] {
                /* 0 */ new Arc(FACTOR, null, 1, Arc.NO_ALTERNATIVE),
                /* 1 */ new Arc(2),
                /* 2 */ new Arc('*', null, 3, 4),
                /* 3 */ new Arc(FACTOR,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateMultiplyOperator();
                    }
                }, 1, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc('/', null, 5, 6),
                /* 5 */ new Arc(FACTOR,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateDivideOperator();
                    }
                }, 1, Arc.NO_ALTERNATIVE),
                /* 6 */ Arc.END_ARC
        };

        FACTOR.arcs = new Arc[] {
                /* 0 */ new Arc(TokenType.NUMERAL,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        NameList nameList = parser.getNameList();
                        CodeGenerator codeGenerator = parser.getCodeGenerator();
                        long constantValue = parser.getLexer().getNextToken().getNumberValue();

                        nameList.addConstant(constantValue);
                        int constantIndex = nameList.getIndexOfConstant(constantValue);

                        codeGenerator.generatePushConstant(constantIndex);

                    }
                }, 5, 1),
                /* 1 */ new Arc('(', null, 2, 4),
                /* 2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /* 3 */ new Arc(')', null, 5, Arc.NO_ALTERNATIVE),
                /* 4 */ new Arc(TokenType.IDENTIFIER,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) throws SemanticRoutineException {
                        NameList nameList = parser.getNameList();
                        CodeGenerator codeGenerator = parser.getCodeGenerator();
                        String identifier = parser.getLexer().getNextToken().getStringValue();

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
                                throw new InvalidIdentifierException();
                        }
                        else
                            throw new InvalidIdentifierException();
                    }
                }, 5, Arc.NO_ALTERNATIVE),
                /* 5 */ Arc.END_ARC
        };

        CONDITION.arcs = new Arc[] {
                /*  0 */ new Arc(SpecialCharacter.ODD.value, null, 1, 2),
                /*  1 */ new Arc(EXPRESSION,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateOdd();
                    }
                }, 10, Arc.NO_ALTERNATIVE),
                /*  2 */ new Arc(EXPRESSION, null, 3, Arc.NO_ALTERNATIVE),
                /*  3 */ new Arc('=',
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue());
                    }
                }, 9, 4),
                /*  4 */ new Arc('#',
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue());
                    }
                }, 9, 5),
                /*  5 */ new Arc('<',
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue());
                    }
                }, 9, 6),
                /*  6 */ new Arc(SpecialCharacter.LESS_OR_EQUAL.value,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue());
                    }
                }, 9, 7),
                /*  7 */ new Arc('>',
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue());
                    }
                },  9, 8),
                /*  8 */ new Arc(SpecialCharacter.GREATER_OR_EQUAL.value,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().setComparisonOperator(parser.getLexer().getNextToken().getCharValue());
                    }
                }, 9, Arc.NO_ALTERNATIVE),
                /*  9 */ new Arc(EXPRESSION,
                new SemanticRoutine() {
                    @Override
                    public void apply(Parser parser) {
                        parser.getCodeGenerator().generateComparisonOperator();
                    }
                }, 10, Arc.NO_ALTERNATIVE),
                /* 10 */ Arc.END_ARC
        };
    }

    private Arc[] arcs;

    public Arc[] getArcs() {
        return arcs;
    }
}
