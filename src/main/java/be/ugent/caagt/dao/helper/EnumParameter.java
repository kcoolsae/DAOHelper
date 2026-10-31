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

/**
 * Parameter of an enumerated type. The name of the enum constant is sent without a type, so that the
 * database infers the enumerated type from the context, for example from the column it is compared with
 * or assigned to. A plain question mark is used, both for null and for non-null values.
 */
public class EnumParameter<E extends Enum<E>> implements Parameter {

    private final E par;

    public EnumParameter(E par) {
        this.par = par;
    }

    /**
     * @deprecated The database type is now inferred from the context and the translator is no longer used.
     * Use {@link #EnumParameter(Enum)} instead
     */
    @Deprecated
    public EnumParameter(E par, EnumClassNameTranslator enumClassNameTranslator) {
        this(par);
    }

    @Override
    public void setParameter(PreparedStatement ps, int index) throws SQLException {
        if (par == null) {
            ps.setNull(index, Types.OTHER);
        } else {
            ps.setObject(index, par.name(), Types.OTHER);
        }
    }
}
