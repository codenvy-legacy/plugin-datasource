package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
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
    @AlternateMessage({"one", "{0} row."})
    String updateCountMessage(@PluralCount int count);

    @DefaultMessage("Export as CSV file")
    String exportCsvLabel();
}
