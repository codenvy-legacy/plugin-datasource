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
    @Source("PostgreSQL.png")
    ImageResource getPostgreSqlLogo();

    @Source("mySQL.png")
    ImageResource getMySqlLogo();

    @Source("sqlServer.png")
    ImageResource getSqlServerLogo();

    @Source("oracle.png")
    ImageResource getOracleLogo();

    @Source("NuoDB.png")
    ImageResource getNuoDBLogo();

    @Source("GoogleCloudSQL.png")
    ImageResource getGoogleCloudSQLLogo();

    @Source("aws_postgres.png")
    ImageResource getAwsPostgresLogo();

    @Source("aws_mysql.png")
    ImageResource getAwsMysqlLogo();

    @Source("aws_oracle.png")
    ImageResource getAwsOracleLogo();

    @Source("aws_sqlserver.png")
    ImageResource getAwsSqlServerLogo();

    @Source("refresh.png")
    ImageResource getRefreshIcon();

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

        @ClassName("requestLauncher-editorBar")
        String requestLauncherEditorBar();

        @ClassName("requestLauncher-listBox")
        String requestLauncherListBox();

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

        @ClassName("resultHeader-infoHeader")
        String infoHeader();

        @ClassName("resultHeader-queryReminder")
        String queryReminder();

        @ClassName("resultHeader-csvButton")
        String csvButton();

        @ClassName("resultHeader-csvLink")
        String csvLink();

        @ClassName("resultHeader-infoHeaderTitle")
        String infoHeaderTitle();

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
    }
}
