/*
 * EnumParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class EnumParameter<E extends Enum<E>> implements Parameter {

    private final E par;

    private final EnumClassNameTranslator enumClassTranslator;

    public EnumParameter(E par, EnumClassNameTranslator enumClassNameTranslator) {
        this.par = par;
        this.enumClassTranslator = enumClassNameTranslator;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        if (par == null) {
            ps.setNull(index, Types.OTHER); // database type is inferred from the context
        } else {
            ps.setString(index, par.toString());
        }
    }

    /**
     * Returns a question mark with a cast to the corresponding database type. For a null value the
     * enum class is unknown and then a plain question mark is returned.
     */
    @Override
    public String getQuestionMark() {
        if (par == null) {
            return "?";
        } else {
            return "?::" + enumClassTranslator.translate(par.getDeclaringClass().getSimpleName());
        }
    }
}
