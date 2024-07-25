/*
 * LimitedSQLStatement.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import be.ugent.caagt.dao.Page;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a statement with offset and limit.
 */
public class LimitedSQLStatement extends AbstractQueryStatement {

    private final int limit;

    LimitedSQLStatement(OrderedSQLStatement src, int limit, int offset) {
        copyAux(src);
        this.limit = limit;
        stat += " OFFSET " + offset + " ROWS";
        if (limit >= 0) {
            stat += " FETCH FIRST " + limit + " ROWS ONLY";
        }
    }

    // Adds the total count to the query result of a compound statement
    private String createPageStatementCompound(String cntId) {
        int lastPos = stat.lastIndexOf("OFFSET");
         /* Note: there will always be an OFFSET because the statement can
           (currently) only be constructed from a limited statement. */
        return "SELECT *, COUNT(*) OVER () AS" + cntId +
                "FROM (" + stat.substring(0, lastPos) + ") AS " + getUniqueId() + " " +
                stat.substring(lastPos);
    }

    // Adds the total count to the query result using a materialized
    // CTE for reasons of speed (making a considerable difference!)
    private String createPageStatementWith(String cntId) {
        int lastPos = stat.lastIndexOf("OFFSET");
        // TODO avoid splitting strings by a better class structure
        String tableId = getUniqueId();
        return "WITH " + tableId + " AS MATERIALIZED(" + stat.substring(0, lastPos) +
                ") SELECT *, COUNT(*) OVER () AS " + cntId + " FROM " + tableId + " " +
                stat.substring(lastPos);
    }

    public <T> Page<T> getPage(ResultSetConverter<T> rsc) {
        String cntId = getUniqueId();
        String newStatement = isCompoundQuery() ? createPageStatementCompound(cntId) : createPageStatementWith(cntId);
        try (PreparedStatement ps = context.getConnection().prepareStatement(newStatement)) {
            initParameters(ps);
            try (ResultSet rs = ps.executeQuery()) {
                List<T> list = new ArrayList<>();
                int fullSize = 0;
                if (rs.next()) {
                    list.add(rsc.convert(rs));
                    fullSize = rs.getInt(cntId);
                    while (rs.next()) {
                        list.add(rsc.convert(rs));
                    }
                }
                return new Page<>(list, limit, fullSize);
            }
        } catch (SQLException ex) {
            throw BaseDAO.convert(ex);
        }
    }
}
