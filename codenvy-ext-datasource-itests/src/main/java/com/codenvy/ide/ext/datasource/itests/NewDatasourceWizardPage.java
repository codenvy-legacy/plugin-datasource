/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
package com.codenvy.ide.ext.datasource.itests;

import com.codenvy.test.framework.AbstractIntegrationTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * A webdriver page object for Datasource plugin creation/edit wizard box
 */
public class NewDatasourceWizardPage {

    protected WebDriver driver;

    @FindBy(id = "gwt-debug-wizardDialog-headerLabel")
    WebElement          title;

    public NewDatasourceWizardPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getWizardTitle() {
        return title.getText();
    }

    public boolean isDatasourceTypeAvailable(String dbType) {
        new WebDriverWait(driver, 10).until(AbstractIntegrationTest.gwtToogleButtonToBeEnable(
                By.id("gwt-debug-datasource-wizard-ds-type-" + dbType)
                                                                                             ));
        return true;
    }

}
