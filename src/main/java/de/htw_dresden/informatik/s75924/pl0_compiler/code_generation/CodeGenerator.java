package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

public class CodeGenerator {
    private ByteArrayOutputStream codeBuffer;
    private RandomAccessFile outputFile;

    public CodeGenerator(String outFile) throws IOException {
        outputFile = new RandomAccessFile(outFile, "rw");
        outputFile.setLength(0);
        outputFile.seek(4);
    }

    public void generateProcedureEntry(int procedureIndex, int variableLength) throws IOException {
        codeBuffer = new ByteArrayOutputStream();

        codeBuffer.write(OperationCode.ENTRY_PROCEDURE.code);
        codeBuffer.write(shortToBytes((short) 0));
        codeBuffer.write(shortToBytes((short) procedureIndex));
        codeBuffer.write(shortToBytes((short) variableLength));
    }

    public void generateProcedureReturn() throws IOException {
        codeBuffer.write(OperationCode.RETURN_PROCEDURE.code);

        byte[] codeBytes = codeBuffer.toByteArray();

        byte[] codeLengthBytes = shortToBytes((short) codeBytes.length);

        System.arraycopy(codeLengthBytes, 0, codeBytes, 1, codeLengthBytes.length);

        outputFile.write(codeBytes);
    }

    public void generatePushVariableValue(int displacement, int procedureIndex, boolean isLocal, boolean isMain) throws IOException {
        if (isLocal)
            codeBuffer.write(OperationCode.PUSH_LOCAL_VARIABLE_VALUE.code);
        else if (isMain)
            codeBuffer.write(OperationCode.PUSH_MAIN_VARIABLE_VALUE.code);
        else
            codeBuffer.write(OperationCode.PUSH_GLOBAL_VARIABLE_VALUE.code);

        codeBuffer.write(shortToBytes((short) displacement));

        if (!isLocal && !isMain)
            codeBuffer.write(shortToBytes((short) procedureIndex));


    }

    public void generatePushConstant(int constantIndex) throws IOException {
        codeBuffer.write(OperationCode.PUSH_CONST.code);

        codeBuffer.write(shortToBytes((short) constantIndex));
    }

    public void generatePutValue() {
        codeBuffer.write(OperationCode.PUT_VALUE.code);
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
