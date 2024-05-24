/*
 * SelectSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Represents the part of a select statement up to and including the from clause
 */
public class SelectSQLStatement extends AbstractOrderedStatement
        implements SupportsParameter<SelectSQLStatement>, SupportsWhere<SelectSQLStatement> {

    SelectSQLStatement(SelectHeader src, String clause) {
        copyAux(src);
        stat += " FROM " + clause;
    }

    private SelectSQLStatement() {

    }

    /**
     * Create an object of this type containing the same information plus one additional parameter.
     */
    public SelectSQLStatement parameter(Parameter par) {
        SelectSQLStatement result = new SelectSQLStatement();
        result.copyAux(this);
        result.addAux(par);
        return result;
    }

    @Override
    public SelectSQLStatement where(WhereClause clause) {
        return where((StatementModifier)clause);
    }

    public SelectSQLStatement where(StatementModifier modifier) {
        SelectSQLStatement result = new SelectSQLStatement();
        result.copyAux(this);
        modifier.addToWhereList(result);
        return result;
    }

    @Override
    public OrderedSQLStatement orderBy(OrderByClause clause) {
        return new OrderedSQLStatement(this, clause);
    }

    public GroupedSQLStatement groupBy(String fields) {
        return new GroupedSQLStatement(this, fields);
    }

    /**
     * Limits the query to maximum one row, executes it and returns whether it returned
     * a result.
     */
    public boolean isEmpty() {
        // TODO: this only works for Derby and Postgresql
        try (PreparedStatement ps = context.getConnection().prepareStatement(
                stat + " FETCH FIRST 1 ROWS ONLY"
        )) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                return !rs.next();
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }

    @Override
    public <E extends Enum<E>> SelectSQLStatement parameter(E value) {
        return parameter (new EnumParameter<>(value, context.getEnumClassTranslator()));
    }

    @Override
    public <E extends Enum<E>> SelectSQLStatement where(String clause, E parameter) {
        return where(new WhereClause(clause, parameter, context.getEnumClassTranslator()));
    }
}
