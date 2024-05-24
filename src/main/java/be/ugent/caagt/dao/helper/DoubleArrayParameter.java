/*
 * DoubleArrayParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

public class DoubleArrayParameter extends SqlArrayParameter {

    public DoubleArrayParameter(double[] par) {
        super(BoxUtils.boxArray(par), "float8");
    }
}
