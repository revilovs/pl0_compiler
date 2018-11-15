package de.htw_dresden.informatik.s75924.pl0_compiler;

public enum CharValues {
    ASSIGN(128),
    LESS_OR_EQUAL(129),
    GREATER_OR_EQUAL(130),
    BEGIN(131),
    CALL(132),
    CONST(133),
    DO(134),
    END(135),
    IF(136),
    ODD(137),
    PROCEDURE(138),
    THEN(139),
    VAR(140),
    WHILE(141);

    char value;

    CharValues(int value) {
        this.value = (char) value;
    }
}
