package de.htw_dresden.informatik.s75924.pl0_compiler.namelist;

/**
 * An interface for all name list entries (variable, constant and procedure)
 */
public interface NameListEntry {
    /**
     * @return the index of the procedure in which the entry is declared
     */
    int getProcedureIndex();

    /**
     * @return the name of the entry
     */
    String getName();
}
