/*
 * BoxUtils.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao.helper;

public final class BoxUtils {

    // There is no standard Java implementation of the methods below and we did not want to pull in the entire Apache Commons Lang for this

    public static Integer[] boxArray(int[] array) {
        if (array == null) {
            return null;
        }
        Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static int[] unboxArray(Integer[] array) {
        if (array == null) {
            return null;
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static Double[] boxArray(double[] array) {
        if (array == null) {
            return null;
        }
        Double[] result = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static double[] unboxArray(Double[] array) {
        if (array == null) {
            return null;
        }
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static Integer[][] boxArray(int[][] array) {
        if (array == null) {
            return null;
        }
        Integer[][] result = new Integer[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = boxArray(array[i]);
        }
        return result;
    }

    public static int[][] unboxArray(Integer[][] array) {
        if (array == null) {
            return null;
        }
        int[][] result = new int[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = unboxArray(array[i]);
        }
        return result;
    }

    public static Double[][] boxArray(double[][] array) {
        if (array == null) {
            return null;
        }
        Double[][] result = new Double[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = boxArray(array[i]);
        }
        return result;
    }

    public static double[][] unboxArray(Double[][] array) {
        if (array == null) {
            return null;
        }
        double[][] result = new double[array.length][];
        for (int i = 0; i < array.length; i++) {
            result[i] = unboxArray(array[i]);
        }
        return result;
    }

}
