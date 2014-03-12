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
