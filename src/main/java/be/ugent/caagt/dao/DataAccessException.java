/*
 * DataAccessException.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

/**
 * Indicates that an error has happened accessing the data
 */
public class DataAccessException extends RuntimeException {

    private final Exception innerException;

    public DataAccessException(String desc, Exception exception){
        super(desc);
        this.innerException = exception;
    }

    public DataAccessException(String desc){
        this(desc, null);
    }

    public DataAccessException(Exception ex) { this(null, ex); }

    public Exception getInnerException() {
        return innerException;
    }

    @Override
    public String getMessage(){
        String message = super.getMessage();
        String innerMessage = innerException != null ? innerException.getMessage() : "";
        if (message == null) {
            return innerMessage;
        } else {
            return message + " -- " + innerMessage;
        }
    }
}
