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

package com.codenvy.ide.ext.datasource.itests;

import com.codenvy.test.framework.AbstractIntegrationTest;
import com.codenvy.test.framework.selenium.pages.IDEMainPage;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.fail;

/**
 * Test datasource creation. See specs reports in src/main/resources/com/codenvy/ide/ext/datasource/itests/NewDatasourceWizard.html
 */
public class NewDatasourceWizard extends AbstractIntegrationTest {


    protected IDEMainPage             mainPage;
    protected NewDatasourceWizardPage newDatasourceWizard;

    public String access(String url) {
        driver.get(url);
        mainPage = PageFactory.initElements(driver, IDEMainPage.class);
        return "access";
    }


    public String displayDatasourceMenu() {
        return mainPage.getMainMenuItem("DatasourceMainMenu").getText();
    }

    public String displayDatasourceNewDatasourceAction() {
        mainPage.getMainMenuItem("DatasourceMainMenu").click();
        return mainPage.getMainMenuAction("Datasource/New Datasource Connection").getText();
    }

    public String clickOnNewDatasourceAction() {
        mainPage.getMainMenuAction("Datasource/New Datasource Connection").click();
        newDatasourceWizard = new NewDatasourceWizardPage(driver);
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 15), newDatasourceWizard);
        return "clicks";
    }


    public String displayNewDatasourceWizard() {
        return newDatasourceWizard.getWizardTitle();
    }

    public String postgresDsTypeIsAvailable() {
        if (newDatasourceWizard.isDatasourceTypeAvailable("postgres")) {
            return "is enabled";
        }
        return "is not enabled";
    }


    public String mySqlDsTypeIsAvailable() {
        if (newDatasourceWizard.isDatasourceTypeAvailable("mysql")) {
            return "is enabled";
        }
        return "is not enabled";
    }

    public String msSQLServerDsTypeIsAvailable() {
        if (newDatasourceWizard.isDatasourceTypeAvailable("sqlserver")) {
            return "is enabled";
        }
        return "is not enabled";
    }

    public String oracleDsTypeIsNotAvailable() {
        try {
            new WebDriverWait(driver, 3).until(gwtToogleButtonToBeEnable(
                                                                  By.cssSelector("#gwt-debug-datasource-wizard-ds-type-oracle")
                                                                  ));
            fail("Oracle shouldn't be enabled by default");
        } catch (Exception e) {
            // success
        }
        return "is disabled";
    }

}
