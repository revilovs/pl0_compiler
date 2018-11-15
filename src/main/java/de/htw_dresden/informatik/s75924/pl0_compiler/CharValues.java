package de.htw_dresden.informatik.s75924.pl0_compiler;

import java.util.HashMap;
import java.util.Map;

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

    static Map<String, Character> stringCharacterMap;
    static Map<Character, String> characterStringMap;

    static {
        stringCharacterMap = new HashMap<>();
        characterStringMap = new HashMap<>();

        for (CharValues item : CharValues.values()) {
            stringCharacterMap.put(item.toString(), item.value);
            characterStringMap.put(item.value, item.toString());
        }
    }
}
