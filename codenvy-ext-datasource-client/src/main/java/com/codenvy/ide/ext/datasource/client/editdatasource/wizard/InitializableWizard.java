/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.ide.ext.datasource.client.editdatasource.wizard;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.wizard.DefaultWizard;
import com.codenvy.ide.api.ui.wizard.WizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;

/**
 * Version of the wizard that can be configured with values.
 * 
 * @author "Mickaël Leduque"
 * @param <T> the data type
 */
public class InitializableWizard<T> extends DefaultWizard {

    /**
     * The initial data
     */
    private T configuration;

    public InitializableWizard(final NotificationManager notificationManager, final String title) {
        super(notificationManager, title);
        Log.debug(InitializableWizard.class, "New InitializableWizard instance created.");
    }

    /**
     * Sets the initial data of the wizard.
     * 
     * @param configuration the data
     */
    public void initData(final T configuration) {
        this.configuration = configuration;
    }

    @Override
    public WizardPage flipToFirst() {
        final WizardPage page = super.flipToFirst();
        if (page == null) {
            Log.error(InitializableWizard.class, "flipToFirst returned a null page !");
            throw new NullPointerException();
        }
        if (page instanceof InitializableWizardPage) {
            scheduleInit(page);

        } else {
            Log.warn(InitializableWizard.class, "Not an initializable wizard page : " + page.getClass());
        }
        return page;
    }

    @Override
    public WizardPage flipToNext() {
        WizardPage page = super.flipToNext();
        if (page instanceof InitializableWizardPage) {
            scheduleInit(page);
        } else {
            Log.warn(InitializableWizard.class, "Not an initializable wizard page : " + page.getClass());
        }
        return page;
    }

    @Override
    public WizardPage flipToPrevious() {
        WizardPage page = super.flipToPrevious();
        if (page instanceof InitializableWizardPage) {
            scheduleInit(page);
        } else {
            Log.warn(InitializableWizard.class, "Not an initializable wizard page : " + page.getClass());
        }
        return page;
    }

    /**
     * Delay the initialization of the page so that the page is properly configured before trying to configure it.
     * 
     * @param page the page to initialize
     */
    private void scheduleInit(final WizardPage page) {
        Command command = new Command() {

            @Override
            public void execute() {
                ((InitializableWizardPage)page).initPage(configuration);
            }
        };
        Scheduler.get().scheduleDeferred(command);
    }
}
