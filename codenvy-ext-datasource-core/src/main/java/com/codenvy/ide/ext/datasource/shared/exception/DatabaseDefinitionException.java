/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.shared.exception;

/**
 * Exception used when a database definition is incorrect.
 * 
 * @author "Mickaël Leduque"
 */
public class DatabaseDefinitionException extends Exception {

    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    public DatabaseDefinitionException() {
    }


    public DatabaseDefinitionException(final String message) {
        super(message);
    }

    public DatabaseDefinitionException(final Throwable cause) {
        super(cause);
    }

    public DatabaseDefinitionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DatabaseDefinitionException(final String message,
                                       final Throwable cause,
                                       final boolean enableSuppression,
                                       final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
