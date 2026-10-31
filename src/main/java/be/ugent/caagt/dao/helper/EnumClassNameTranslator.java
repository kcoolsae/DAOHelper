/*
 * EnumClassNameTranslator.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

/**
 * @deprecated Enumerated database types are now inferred from the context, so the names of Java classes no
 * longer need to be translated
 */
@Deprecated
public interface EnumClassNameTranslator {

    /**
     * Translates a Java class name to a database type name
     */
    String translate (String className);

    EnumClassNameTranslator NO_OP = cn -> cn;

    EnumClassNameTranslator SNAKE_CASE = new SnakeCase ();
}
