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
    }
}
