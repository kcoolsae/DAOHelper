/*
 * SnakeCase.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

class SnakeCase implements EnumClassNameTranslator {
    @Override
    public String translate(String className) {
        StringBuilder builder = new StringBuilder();
        builder.append (Character.toLowerCase(className.charAt(0)));
        for (int i = 1; i < className.length(); i++) {
            char ch = className.charAt(i);
            if (Character.isUpperCase(ch)) {
                builder.append('_');
            }
            builder.append(Character.toLowerCase(ch));
        }
        return builder.toString();
    }
}
