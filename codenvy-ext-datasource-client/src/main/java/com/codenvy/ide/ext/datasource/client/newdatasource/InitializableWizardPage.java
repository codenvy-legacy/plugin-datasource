package com.codenvy.ide.ext.datasource.client.newdatasource;

import com.codenvy.ide.api.ui.wizard.WizardPage;

public interface InitializableWizardPage extends WizardPage {

    void initPage(Object initData);
}
