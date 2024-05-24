/*
 * NamedParameterList.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NamedParameterList extends StatementModifier {

    private List<NamedParameter> parameters;

    public NamedParameterList() {
        parameters = new ArrayList<>();
    }

    public NamedParameterList with(NamedParameter name) {
        NamedParameterList result = new NamedParameterList();
        result.parameters = new ArrayList<>(parameters);
        result.parameters.add(name);
        return result;
    }

    @Override
    protected void addToWhereList(AbstractFilteredStatement statement) {
        for (NamedParameter par : parameters) {
            statement.addAux(new WhereClause(par));
        }
    }

    void addToParameterList (List<NamedParameter> list) {
        list.addAll(parameters);
    }

    void addToSetList (UpdateSQLStatement statement) {
        for (NamedParameter par : parameters) {
            statement.addAuxSet(par.getName(), par.getParameter());
        }
    }

    @Override
    protected void addToHavingList(GroupedSQLStatement statement) {
        for (NamedParameter par : parameters) {
            statement.addAuxHaving(new WhereClause(par));
        }
    }

    public NamedParameterList with(String name, Parameter parameter) {
        return with(new NamedParameter(name, parameter));
    }

    public NamedParameterList with(String name, boolean parameter) {
        return with(name, new BooleanParameter(parameter));
    }

    public NamedParameterList with(String name, int parameter) {
        return with(name, new IntParameter(parameter));
    }

    public NamedParameterList with(String name, double parameter) {
        return with(name, new DoubleParameter(parameter));
    }

    public NamedParameterList with(String name, String parameter) {
        return with(name, new StringParameter(parameter));
    }

    public NamedParameterList with(String name, LocalDateTime parameter) {
        return with(name, new LocalDateTimeParameter(parameter));
    }

    public NamedParameterList with(String name, LocalDate parameter) {
        return with(name, new LocalDateParameter(parameter));
    }

    public NamedParameterList with(String name, LocalTime parameter) {
        return with(name, new LocalTimeParameter(parameter));
    }

    public NamedParameterList with(String name, Instant parameter) {
        return with(name, new InstantParameter(parameter));
    }

    public NamedParameterList with(String name, byte[] parameter) {
        return with(name, new BytesParameter(parameter));
    }

}
