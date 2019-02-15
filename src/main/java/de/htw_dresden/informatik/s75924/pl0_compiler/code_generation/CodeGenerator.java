package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import de.htw_dresden.informatik.s75924.pl0_compiler.lexer.SpecialCharacter;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Stack;

/**
 * Class for generating the code to the output file
 */
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

    /**
     * Constructor
     * @param outFile the file to which the code should be output
     * @throws IOException if the outFile can't be opened for writing
     */
    public CodeGenerator(String outFile) throws IOException {
        outputFile = new RandomAccessFile(outFile, "rw");
        outputFile.setLength(0);
        outputFile.seek(4);

        jumpAddressStack = new Stack<>();
    }

    /**
     * Generates the procedure entry code
     * @param procedureIndex the procedure's index
     * @param variableLength the length of the procedure's variable block
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateProcedureEntry(int procedureIndex, int variableLength) throws IOException {
        currentProcedureStartAddress = outputFile.getFilePointer();

        outputFile.write(OperationCode.ENTRY_PROCEDURE.code);
        outputFile.write(shortToBytes((short) 0));
        outputFile.write(shortToBytes((short) procedureIndex));
        outputFile.write(shortToBytes((short) variableLength));
    }

    /**
     * generates the procedure return code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateProcedureReturn() throws IOException {
        outputFile.write(OperationCode.RETURN_PROCEDURE.code);

        long currentPosition = outputFile.getFilePointer();

        long procedureCodeLength = currentPosition - currentProcedureStartAddress;
        byte[] codeLengthBytes = shortToBytes((short) procedureCodeLength);

        outputFile.seek(currentProcedureStartAddress + 1);
        outputFile.write(codeLengthBytes);
        
        outputFile.seek(currentPosition);
    }

    /**
     * Generates the code for pushing a variable's value
     * @param displacement the variable's displacement in its variable block
     * @param procedureIndex the index of the procedure where it is located
     * @param isLocal indicates if the variable belongs to the current procedure
     * @param isMain indicates if the variable is in the main procedure
     * @throws IOException if an I/O error occurs while writing
     */
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

    /**
     * Generates the code for pushing a variable's address to the stack
     * @param displacement the variable's displacement in its variable block
     * @param procedureIndex the index of the procedure where it is located
     * @param isLocal indicates if the variable belongs to the current procedure
     * @param isMain indicates if the variable is in the main procedure
     * @throws IOException if an I/O error occurs while writing
     */
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

    /**
     * Generates the code for pushing a constant to the stack
     * @param constantIndex the index of the constant
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePushConstant(int constantIndex) throws IOException {
        outputFile.write(OperationCode.PUSH_CONST.code);

        outputFile.write(shortToBytes((short) constantIndex));
    }

    /**
     * Generates the store value code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateStoreValue() throws IOException {
        outputFile.write(OperationCode.STORE_VALUE.code);
    }

    /**
     * Generates the store put code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePutValue() throws IOException {
        outputFile.write(OperationCode.PUT_VALUE.code);
    }

    /**
     * Generates the get value code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateGetValue() throws IOException {
        outputFile.write(OperationCode.GET_VALUE.code);
    }

    /**
     * Generates the negative sign code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateNegativeSign() throws IOException {
        outputFile.write(OperationCode.NEGATIVE_SIGN.code);
    }

    /**
     * Generates the add operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateAddOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_ADD.code);
    }

    /**
     * Generates the subtraction operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateSubtractOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_SUBTRACT.code);
    }

    /**
     * Generates the multiplication operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateMultiplyOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_MULTIPLY.code);
    }

    /**
     * Generates the division operator code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateDivideOperator() throws IOException {
        outputFile.write(OperationCode.OPERATOR_DIVIDE.code);
    }

    /**
     * Generates the odd code
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateOdd() throws IOException {
        outputFile.write(OperationCode.ODD.code);
    }

    /**
     * Pushes the current code generation position to the stack for generating jump addresses later
     * @throws IOException if an I/O error occurs while writing
     */
    public void saveCurrentAddress() throws IOException {
        jumpAddressStack.push(outputFile.getFilePointer());
    }

    /**
     * Generates the JNOT code with the jump distance set to 0
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePreliminaryJNOT() throws IOException {
        outputFile.write(OperationCode.JUMP_NOT.code);

        outputFile.write(shortToBytes((short) 0));
    }

    /**
     * Generates the JUMP code with the jump distance set to 0
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePreliminaryELSEJUMP() throws IOException {
        outputFile.write(OperationCode.JUMP.code);

        outputFile.write(shortToBytes((short) 0));
    }

    /**
     * Completes the JNOT code for the conditional statement
     * @param elsePresent should be true if and only if the conditional statement has an ELSE branch
     * @throws IOException if an I/O error occurs while writing
     */
    public void completeIFJNOT(boolean elsePresent) throws IOException {
        long savedAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long relativeAddress = currentAddress - savedAddress - 3
                + (elsePresent ? 3 : 0);

        outputFile.seek(savedAddress + 1);
        outputFile.write(shortToBytes((short) relativeAddress));
        outputFile.seek(currentAddress);
    }

    /**
     * Completes the JUMP code for the ELSE branch of a conditional statement
     */
    public void completeELSEJUMP() throws IOException {
        long savedAddress = jumpAddressStack.pop();

        long currentAddress = outputFile.getFilePointer();

        long relativeAddress = currentAddress - savedAddress - 3;

        outputFile.seek(savedAddress + 1);
        outputFile.write(shortToBytes((short) relativeAddress));
        outputFile.seek(currentAddress);
    }

    /**
     * Completes the jump codes for a loop statement
     * @throws IOException if an I/O error occurs while writing
     */
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

    /**
     * Generates the procedure call code
     * @param procedureIndex the index of the procedure
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateProcedureCall(int procedureIndex) throws IOException {
        outputFile.write(OperationCode.CALL.code);

        outputFile.write(shortToBytes((short) procedureIndex));
    }

    /**
     * Sets the comparison operator for a condition
     * @param comparisonOperator the comparison operator
     */
    public void setComparisonOperator(char comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Generates the comparison operator code for the previously saved operator
     * @throws IOException if an I/O error occurs while writing
     */
    public void generateComparisonOperator() throws IOException {
        outputFile.write(comparisonOperatorMap.get(comparisonOperator).code);
    }

    /**
     * Generates the put string code
     * @param string the string to be output
     * @throws IOException if an I/O error occurs while writing
     */
    public void generatePutString(String string) throws IOException {
        outputFile.write(OperationCode.PUT_STRING.code);

        int arrayLength = string.length() + 1;
        byte[] bytes = new byte[arrayLength];
        bytes[arrayLength -1] = 0;
        System.arraycopy(string.getBytes(), 0, bytes, 0 , arrayLength - 1);

        outputFile.write(bytes);
    }

    /**
     * Writes the constant block to the output file
     * @param constantBlock the constant block
     * @throws IOException if an I/O error occurs while writing
     */
    public void writeConstantBlock(long[] constantBlock) throws IOException {
        for (long constant : constantBlock)
            outputFile.write(longToBytes(constant));
    }
    /**
     * Writes the number of procedures to the start of the code file
     * @param numberOfProcedures the number of procedures
     * @throws IOException if an I/O error occurs while writing
     */
    public void writeNumberOfProcedures(int numberOfProcedures) throws IOException {
        outputFile.seek(0);
        outputFile.write(longToBytes((long) numberOfProcedures));
    }

    /**
     * Closes the output file
     * @throws IOException if an I/O error occurs while closing
     */
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
