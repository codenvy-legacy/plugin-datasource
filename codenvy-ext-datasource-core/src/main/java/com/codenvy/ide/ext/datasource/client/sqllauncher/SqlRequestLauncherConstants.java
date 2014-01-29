package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.i18n.client.Messages;

public interface SqlRequestLauncherConstants extends Messages {

    @DefaultMessage("Open SQL editor")
    String menuEntryOpenSqlEditor();

    @DefaultMessage("SQL editor")
    String sqlEditorWindowTitle();

    @DefaultMessage("Select datasource")
    String selectDatasourceLabel();

    @DefaultMessage("Result limit")
    String resultLimitLabel();

    @DefaultMessage("Execute")
    String executeButtonLabel();

    @DefaultMessage("{0} rows.")
    String updateCountMessage(int count);
}
