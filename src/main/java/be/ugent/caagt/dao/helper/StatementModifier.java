/*
 * StatementModifier.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * Common super class of classes that are used to modify statements.
 */
public abstract class StatementModifier {

    protected abstract void addToWhereList (AbstractFilteredStatement statement);

    protected abstract void addToHavingList (GroupedSQLStatement statement);

}
