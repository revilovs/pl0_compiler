package de.htw_dresden.informatik.s75924.pl0_compiler.code_generation;

enum OperationCode {
    PUSH_LOCAL_VARIABLE_VALUE((byte) 0x00),
    PUSH_MAIN_VARIABLE_VALUE((byte) 0x01),
    PUSH_GLOBAL_VARIABLE_VALUE((byte) 0x02),
    PUSH_LOCAL_VARIABLE_ADDRESS((byte) 0x03),
    PUSH_MAIN_VARIABLE_ADDRESS((byte) 0x04),
    PUSH_GLOBAL_VARIABLE_ADDRESS((byte) 0x05),
    PUSH_CONST((byte) 0x06),
    STORE_VALUE((byte) 0x07),
    PUT_VALUE((byte) 0x08),
    GET_VALUE((byte) 0x09),
    NEGATIVE_SIGN((byte) 0x0a),
    ODD((byte) 0x0b),
    OPERATOR_ADD((byte) 0x0c),
    OPERATOR_SUBTRACT((byte) 0x0d),
    OPERATOR_MULTIPLY((byte) 0x0e),
    OPERATROR_DIVIDE((byte) 0x0f),
    COMPARE_EQUAL((byte) 0x10),
    COMPARE_NOT_EQUAL((byte) 0x11),
    COMPARE_LESS((byte) 0x12),
    COMPARE_GREATER((byte) 0x13),
    COMPARE_LESS_OR_EQUAL((byte) 0x14),
    COMPARE_GREATER_OR_EQUAL((byte) 0x15),
    CALL((byte) 0x16),
    RETURN_PROCEDURE((byte) 0x17),
    JUMP((byte) 0x18),
    JUMP_NOT((byte) 0x19),
    ENTRY_PROCEDURE((byte) 0x1a);

    byte code;

    OperationCode(byte code){
        this.code = code;
    }

}
