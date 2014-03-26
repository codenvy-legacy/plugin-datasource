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
package com.codenvy.ide.ext.datasource.client.newdatasource;

import com.google.gwt.i18n.client.Messages;

public interface NewDatasourceWizardMessages extends Messages {

    @DefaultMessage("New Datasource")
    String wizardTitle();

    @DefaultMessage("Establishing Database Connection...")
    String startConnectionTest();

    @DefaultMessage("Succesfully connected to database")
    String connectionTestSuccessNotification();

    @DefaultMessage("Failed to connect to database")
    String connectionTestFailureSuccessNotification();

    @DefaultMessage("Connection succeeded")
    String connectionTestSuccessMessage();

    @DefaultMessage("Connection failed")
    String connectionTestFailureSuccessMessage();
}
