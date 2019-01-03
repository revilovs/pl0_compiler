package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.SpecialCharacter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator {
    private ByteBuffer codeBuffer;
    private RandomAccessFile outputFile;

    private char comparisonOperator;

    private Stack<Integer> jumpAddressStack;

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

    public void generateProcedureEntry(int procedureIndex, int variableLength) {
        //TODO: make size dynamic
        codeBuffer = ByteBuffer.allocate(4096);

        codeBuffer.put(OperationCode.ENTRY_PROCEDURE.code);
        codeBuffer.put(shortToBytes((short) 0));
        codeBuffer.put(shortToBytes((short) procedureIndex));
        codeBuffer.put(shortToBytes((short) variableLength));
    }

    public void generateProcedureReturn() throws IOException {
        codeBuffer.put(OperationCode.RETURN_PROCEDURE.code);

        byte[] codeBytes = new byte[codeBuffer.position()];

        System.arraycopy(codeBuffer.array(),0, codeBytes, 0, codeBuffer.position());

        byte[] codeLengthBytes = shortToBytes((short) codeBytes.length);

        System.arraycopy(codeLengthBytes, 0, codeBytes, 1, codeLengthBytes.length);

        outputFile.write(codeBytes);
    }

    public void generatePushVariableValue(int displacement, int procedureIndex, boolean isLocal, boolean isMain) {
        if (isMain)
            codeBuffer.put(OperationCode.PUSH_MAIN_VARIABLE_VALUE.code);
        else if (isLocal)
            codeBuffer.put(OperationCode.PUSH_LOCAL_VARIABLE_VALUE.code);
        else
            codeBuffer.put(OperationCode.PUSH_GLOBAL_VARIABLE_VALUE.code);

        codeBuffer.put(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            codeBuffer.put(shortToBytes((short) procedureIndex));
    }

    public void generatePushVariableAddress(int displacement, int procedureIndex, boolean isLocal, boolean isMain) {
        if (isMain)
            codeBuffer.put(OperationCode.PUSH_MAIN_VARIABLE_ADDRESS.code);
        else if (isLocal)
            codeBuffer.put(OperationCode.PUSH_LOCAL_VARIABLE_ADDRESS.code);
        else
            codeBuffer.put(OperationCode.PUSH_GLOBAL_VARIABLE_ADDRESS.code);

        codeBuffer.put(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            codeBuffer.put(shortToBytes((short) procedureIndex));
    }

    public void generatePushConstant(int constantIndex) {
        codeBuffer.put(OperationCode.PUSH_CONST.code);

        codeBuffer.put(shortToBytes((short) constantIndex));
    }

    public void generateStoreValue() {
        codeBuffer.put(OperationCode.STORE_VALUE.code);
    }

    public void generatePutValue() {
        codeBuffer.put(OperationCode.PUT_VALUE.code);
    }

    public void generateGetValue() {
        codeBuffer.put(OperationCode.GET_VALUE.code);
    }

    public void generateNegativeSign() {
        codeBuffer.put(OperationCode.NEGATIVE_SIGN.code);
    }

    public void generateAddOperator(){
        codeBuffer.put(OperationCode.OPERATOR_ADD.code);
    }

    public void generateSubtractOperator(){
        codeBuffer.put(OperationCode.OPERATOR_SUBTRACT.code);
    }

    public void generateMultiplyOperator(){
        codeBuffer.put(OperationCode.OPERATOR_MULTIPLY.code);
    }

    public void generateDivideOperator() {
        codeBuffer.put(OperationCode.OPERATOR_DIVIDE.code);
    }

    public void generateOdd() {
        codeBuffer.put(OperationCode.ODD.code);
    }

    public void saveCurrentAddress() {
        jumpAddressStack.push(codeBuffer.position());
    }

    public void generatePreliminaryJNOT() {
        codeBuffer.put(OperationCode.JUMP_NOT.code);

        codeBuffer.put(shortToBytes((short) 0));
    }

    public void completeIFJNOT() {
        int savedAddress = jumpAddressStack.pop();

        int currentAddress = codeBuffer.position();

        int relativeAddress = currentAddress - savedAddress - 3;

        codeBuffer.position(savedAddress + 1);

        codeBuffer.put(shortToBytes((short) relativeAddress));

        codeBuffer.position(currentAddress);
    }

    public void completeWHILE() {
        int jNotAddress = jumpAddressStack.pop();
        int conditionAddress = jumpAddressStack.pop();

        int currentAddress = codeBuffer.position();

        int loopStartJumpDistance = conditionAddress - currentAddress - 3;
        int loopExitJumpDistance = currentAddress - jNotAddress;

        codeBuffer.put(OperationCode.JUMP.code);
        codeBuffer.put(shortToBytes((short) loopStartJumpDistance));

        currentAddress = codeBuffer.position();

        codeBuffer.position(jNotAddress + 1);

        codeBuffer.put(shortToBytes((short) loopExitJumpDistance));

        codeBuffer.position(currentAddress);
    }

    public void generateProcedureCall(int procedureIndex){
        codeBuffer.put(OperationCode.CALL.code);

        codeBuffer.put(shortToBytes((short) procedureIndex));
    }

    public void setComparisonOperator(char comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public void generateComparisonOperator() {
        codeBuffer.put(comparisonOperatorMap.get(comparisonOperator).code);
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
