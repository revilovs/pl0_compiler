package de.htw_dresden.informatik.s75924.pl0_compiler.lexer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SpecialCharacterTest {
    @Test
    public void allCharValuesAreDistinct() {
        for(SpecialCharacter oneChar : SpecialCharacter.values()){
            for (SpecialCharacter otherChar : SpecialCharacter.values()){
                if (! oneChar.equals(otherChar))
                    assertNotEquals(oneChar.value, otherChar.value);
            }
        }
    }

    @Test
    public void charValuesAreNotLetterOrDigit() {
        for (SpecialCharacter character : SpecialCharacter.values()){
            assertFalse(Character.isLetterOrDigit(character.value));
        }
    }
}