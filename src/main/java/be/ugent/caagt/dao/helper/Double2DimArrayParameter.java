/*
 * Double2DimArrayParameter.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2026 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

public class Double2DimArrayParameter extends SqlArrayParameter {

    public Double2DimArrayParameter(double[][] par) {
        super(BoxUtils.boxArray(par), "float8");
    }
}
