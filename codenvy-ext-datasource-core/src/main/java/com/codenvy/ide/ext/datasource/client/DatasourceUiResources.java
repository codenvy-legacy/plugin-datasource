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

    @Source("refresh.png")
    ImageResource getRefreshIcon();

    @Source({"datasource-ui.css", "com/codenvy/ide/api/ui/style.css"})
    DatasourceUiStyle datasourceUiCSS();

    public interface DatasourceUiStyle extends CssResource {

        @ClassName("explorer-datasourceList")
        String explorerDatasourceList();

        @ClassName("explorer-topPanel")
        String explorerTopPanel();

        @ClassName("explorer-refreshButton")
        String explorerRefreshButton();

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
    }
}
