/*
 * EnumParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EnumParameter<E extends Enum<E>> implements Parameter {

    private final E par;

    private final EnumClassNameTranslator enumClassTranslator;

    public EnumParameter(E par, EnumClassNameTranslator enumClassNameTranslator) {
        this.par = par;
        this.enumClassTranslator = enumClassNameTranslator;
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        ps.setString(index, par.toString());
    }

    @Override
    public String getQuestionMark() {
        return "?::" + enumClassTranslator.translate(par.getClass().getSimpleName());
    }
}
