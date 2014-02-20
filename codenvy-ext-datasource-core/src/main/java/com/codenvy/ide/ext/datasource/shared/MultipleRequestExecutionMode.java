package com.codenvy.ide.ext.datasource.shared;

public enum MultipleRequestExecutionMode {

    /** All SQL requests are executed, continue after error. */
    ONE_BY_ONE,
    /** All SQL requests are executed, but stop at first error. */
    STOP_AT_FIRST_ERROR,
    /** All requests are executed or none. If one fails, stop and rollback. */
    TRANSACTIONAL
}
