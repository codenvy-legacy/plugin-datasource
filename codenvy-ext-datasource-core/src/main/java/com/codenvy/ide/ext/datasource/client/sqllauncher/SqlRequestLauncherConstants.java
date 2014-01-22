package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.i18n.client.Constants;

public interface SqlRequestLauncherConstants extends Constants {

    @DefaultStringValue("Open SQL editor")
    String menuEntryOpenSqlEditor();

    @DefaultStringValue("SQL editor")
    String sqlEditorWindowTitle();

    @DefaultStringValue("Select datasource")
    String selectDatasourceLabel();

    @DefaultStringValue("Result limit")
    String resultLimitLabel();
}
