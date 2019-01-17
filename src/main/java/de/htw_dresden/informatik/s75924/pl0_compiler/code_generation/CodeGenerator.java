package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.SpecialCharacter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator {
    private RandomAccessFile outputFile;

    private long currentProcedureStartAddress = 0;

    private char comparisonOperator;

    private Stack<Long> jumpAddressStack;

    private static final HashMap<Character, OperationCode> comparisonOperatorMap = new HashMap<>();

    static {
        comparisonOperatorMap.put('=', OperationCode.COMPARE_EQUAL);
        comparisonOperatorMap.put('#', OperationCode.COMPARE_NOT_EQUAL);
        comparisonOperatorMap.put('<', OperationCode.COMPARE_LESS);
        comparisonOperatorMap.put('>', OperationCode.COMPARE_GREATER);
        comparisonOperatorMap.put(SpecialCharacter.LESS_OR_EQUAL.value, OperationCode.COMPARE_LESS_OR_EQUAL);
        comparisonOperatorMap.put(SpecialCharacter.GREATER_OR_EQUAL.value, OperationCode.COMPARE_GREATER_OR_EQUAL);
    }

    public CodeGenerator(String outFile) throws IOException {
        outputFile = new RandomAccessFile(outFile, "rw");
        outputFile.setLength(0);
        outputFile.seek(4);

        jumpAddressStack = new Stack<>();
    }

    public void generateProcedureEntry(int procedureIndex, int variableLength) throws IOException {
        currentProcedureStartAddress = outputFile.getFilePointer();

        outputFile.write(OperationCode.ENTRY_PROCEDURE.code);
        outputFile.write(shortToBytes((short) 0));
        outputFile.write(shortToBytes((short) procedureIndex));
        outputFile.write(shortToBytes((short) variableLength));
    }

    public void generateProcedureReturn() throws IOException {
        outputFile.write(OperationCode.RETURN_PROCEDURE.code);

        long currentPosition = outputFile.getFilePointer();

        long procedureCodeLength = currentPosition - currentProcedureStartAddress;
        byte[] codeLengthBytes = shortToBytes((short) procedureCodeLength);

        outputFile.seek(currentProcedureStartAddress + 1);
        outputFile.write(codeLengthBytes);
        
        outputFile.seek(currentPosition);
    }

    public void generatePushVariableValue(int displacement, int procedureIndex, boolean isLocal, boolean isMain) throws IOException {
        if (isMain)
            outputFile.write(OperationCode.PUSH_MAIN_VARIABLE_VALUE.code);
        else if (isLocal)
            outputFile.write(OperationCode.PUSH_LOCAL_VARIABLE_VALUE.code);
        else
            outputFile.write(OperationCode.PUSH_GLOBAL_VARIABLE_VALUE.code);

        outputFile.write(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            outputFile.write(shortToBytes((short) procedureIndex));
    }

    public void generatePushVariableAddress(int displacement, int procedureIndex, boolean isLocal, boolean isMain) throws IOException {
        if (isMain)
            outputFile.write(OperationCode.PUSH_MAIN_VARIABLE_ADDRESS.code);
        else if (isLocal)
            outputFile.write(OperationCode.PUSH_LOCAL_VARIABLE_ADDRESS.code);
        else
            outputFile.write(OperationCode.PUSH_GLOBAL_VARIABLE_ADDRESS.code);

        outputFile.write(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            outputFile.write(shortToBytes((short) procedureIndex));
    }

    public void generatePushConstant(int constantIndex) throws IOException {
        outputFile.write(OperationCode.PUSH_CONST.code);

        outputFile.write(shortToBytes((short) constantIndex));
    }

    public void generateStoreValue() throws IOException {
        outputFile.write(OperationCode.STORE_VALUE.code);
    }

    public void generatePutValue() throws IOException {
        outputFile.write(OperationCode.PUT_VALUE.code);
    }

    public void generateGetValue() throws IOException {
        outputFile.write(OperationCode.GET_VALUE.code);
    }

    public void generateNegativeSign() throws IOException {
        outputFile.write(OperationCode.NEGATIVE_SIGN.code);
    }

    public void generateAddOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_ADD.code);
    }

    public void generateSubtractOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_SUBTRACT.code);
    }

    public void generateMultiplyOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_MULTIPLY.code);
    }

    public void generateDivideOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_DIVIDE.code);
    }

    public void generateOdd() throws IOException {
        outputFile.write(OperationCode.ODD.code);
    }

    public void saveCurrentAddress() throws IOException {
        jumpAddressStack.push(outputFile.getFilePointer());
    }

    public void generatePreliminaryJNOT() throws IOException {
        outputFile.write(OperationCode.JUMP_NOT.code);

        outputFile.write(shortToBytes((short) 0));
    }

    public void completeIFJNOT() throws IOException {
        long savedAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long relativeAddress = currentAddress - savedAddress - 3;

        outputFile.seek(savedAddress + 1);

        outputFile.write(shortToBytes((short) relativeAddress));

        outputFile.seek(currentAddress);
    }

    public void completeWHILE() throws IOException {
        long jNotAddress = jumpAddressStack.pop();
        long conditionAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long loopStartJumpDistance = conditionAddress - currentAddress - 3;
        long loopExitJumpDistance = currentAddress - jNotAddress;

        outputFile.write(OperationCode.JUMP.code);
        outputFile.write(shortToBytes((short) loopStartJumpDistance));

        currentAddress = outputFile.getFilePointer();

        outputFile.seek(jNotAddress + 1);

        outputFile.write(shortToBytes((short) loopExitJumpDistance));

        outputFile.seek(currentAddress);
    }

    public void generateProcedureCall(int procedureIndex) throws IOException {
        outputFile.write(OperationCode.CALL.code);

        outputFile.write(shortToBytes((short) procedureIndex));
    }

    public void setComparisonOperator(char comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public void generateComparisonOperator() throws IOException {
        outputFile.write(comparisonOperatorMap.get(comparisonOperator).code);
    }

    public void writeConstantBlock(long[] constantBlock) throws IOException {
        for (long constant : constantBlock)
            outputFile.write(longToBytes(constant));
    }

    public void writeNumberOfProcedures(int numberOfProcedures) throws IOException {
        outputFile.seek(0);
        outputFile.write(longToBytes((long) numberOfProcedures));
    }

    public void close() throws IOException {
        outputFile.close();
    }

    private static byte[] longToBytes(long value){
        byte[] bytes = new byte[4];

        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);

        return bytes;
    }

    private static byte[] shortToBytes(short value){
        byte[] bytes = new byte[2];

        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);

        return bytes;
    }
}
