/*
 * NamedParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Combination of name and parameter
 */
public class NamedParameter {

    private final String name;
    private final Parameter parameter;

    public NamedParameter(String name, Parameter parameter) {
        if (name.indexOf('?') >= 0) {
            throw new IllegalArgumentException("Named parameter should not contain question mark in name");
        }
        this.name = name;
        this.parameter = parameter;
    }

    public String getName() {
        return name;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getQuestionMark() {
        return parameter.getQuestionMark();
    }
}
