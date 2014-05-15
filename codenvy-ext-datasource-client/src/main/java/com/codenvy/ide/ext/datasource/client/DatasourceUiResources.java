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
package com.codenvy.ide.ext.datasource.client;

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

/**
 * Client bundle interface for datasource plugin resources.
 */
public interface DatasourceUiResources extends ClientBundle {

    /**
     * The PostgreSQL logo.
     * 
     * @return an image resource for the PostGreSQL logo.
     */
    @Source("postgresql.png")
    ImageResource getPostgreSqlLogo();

    @Source("mysql.png")
    ImageResource getMySqlLogo();

    @Source("sqlserver.png")
    ImageResource getSqlServerLogo();

    @Source("oracle.png")
    ImageResource getOracleLogo();

    @Source("nuodb.png")
    ImageResource getNuoDBLogo();

    @Source("google.png")
    ImageResource getGoogleCloudSQLLogo();

    @Source("aws-postgresql.png")
    ImageResource getAwsPostgresLogo();

    @Source("aws-mysql.png")
    ImageResource getAwsMysqlLogo();

    @Source("aws-oracle.png")
    ImageResource getAwsOracleLogo();

    @Source("aws-sqlserver.png")
    ImageResource getAwsSqlServerLogo();

    @Source("refresh.svg")
    SVGResource getRefreshIcon();

    @Source("NewDatasource.svg")
    SVGResource newDatasourceMenuIcon();

    @Source("ManageDatasource.svg")
    SVGResource manageDatasourceMenuIcon();

    @Source({"datasource-ui.css", "com/codenvy/ide/api/ui/style.css"})
    DatasourceUiStyle datasourceUiCSS();

    public interface DatasourceUiStyle extends CssResource {

        @ClassName("formField-Label")
        String formFieldLabel();

        @ClassName("explorer-datasourceList")
        String explorerDatasourceList();

        @ClassName("explorer-topPanel")
        String explorerTopPanel();

        @ClassName("explorer-refreshButton")
        String explorerRefreshButton();

        @ClassName("explorer-refreshButton-image")
        String explorerRefreshButtonImage();

        @ClassName("explorer-refreshButton-up")
        String explorerRefreshButtonUp();

        @ClassName("explorer-refreshButton-up-hover")
        String explorerRefreshButtonUpHover();

        @ClassName("explorer-refreshButton-down")
        String explorerRefreshButtonDown();

        @ClassName("explorer-refreshButton-down-hover")
        String explorerRefreshButtonDownHover();

        @ClassName("requestLauncher-editorBar")
        String requestLauncherEditorBar();

        @ClassName("requestLauncher-listBox")
        String requestLauncherListBox();

        @ClassName("resultHeader-clear-button")
        String resultHeaderClearButton();

        @ClassName("requestLauncher-executionModeListBox")
        String requestLauncherExecutionModeListBox();

        @ClassName("requestLauncher-datasourceListBox")
        String requestLauncherDatasourceListBox();

        @ClassName("requestLauncher-textBox")
        String requestLauncherTextBox();

        @ClassName("requestLauncher-resultLimitInput")
        String requestLauncherResultLimitInp();

        @ClassName("requestLauncher-executeButton")
        String requestLauncherExecuteButton();

        @ClassName("requestLauncher-label")
        String requestLauncherLabel();

        @ClassName("requestLauncher-executionModeLabel")
        String requestLauncherExecutionModeLabel();

        @ClassName("requestLauncher-resultLimitLabel")
        String requestLauncherResultLimitLabel();

        @ClassName("requestLauncher-selectDatasourceLabel")
        String requestLauncherSelectDatasourceLabel();

        @ClassName("resultItemHeader-queryReminder")
        String resultItemQueryReminder();

        @ClassName("resultItemHeader-csvButton")
        String resultItemCsvButton();

        @ClassName("resultItemHeader-csvLink")
        String resultItemCsvLink();

        @ClassName("resultZoneHeader-text")
        String resultZoneHeaderText();

        @ClassName("resultZoneHeader-bar")
        String resultZoneHeaderBar();

        @ClassName("resultZoneOutput")
        String resultZoneOutput();

        @ClassName("resultItemHeader-bar")
        String resultItemHeaderBar();

        @ClassName("resultItem")
        String resultItem();

        @ClassName("propertiesTable-firstColumn")
        String propertiesTableFirstColumn();

        @ClassName("propertiesPanel-background")
        String propertiesPanelBackground();

        @ClassName("editDatasourceList-datasourceTypeStyle")
        String datasourceTypeStyle();

        @ClassName("editDatasourceList-datasourceIdStyle")
        String datasourceIdStyle();

        @ClassName("editDatasourceList-datasourceIdCellStyle")
        String datasourceIdCellStyle();

        @ClassName("editDatasourceList-datasourceTypeCellStyle")
        String datasourceTypeCellStyle();

        @ClassName("datasourceWizard-testConnection")
        String datasourceWizardTestConnection();

        @ClassName("datasourceWizard-testConnection-ok")
        String datasourceWizardTestConnectionOK();

        @ClassName("datasourceWizard-testConnection-ko")
        String datasourceWizardTestConnectionKO();
    }
}
