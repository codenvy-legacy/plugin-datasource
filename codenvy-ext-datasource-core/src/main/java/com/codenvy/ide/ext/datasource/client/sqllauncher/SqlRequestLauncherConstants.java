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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface SqlRequestLauncherConstants extends Messages {

    @DefaultMessage("Open SQL editor")
    String menuEntryOpenSqlEditor();

    @DefaultMessage("SQL editor")
    String sqlEditorWindowTitle();

    @DefaultMessage("Datasource Target:")
    String selectDatasourceLabel();

    @DefaultMessage("Result limit:")
    String resultLimitLabel();

    @DefaultMessage("Execute Query")
    String executeButtonLabel();

    @DefaultMessage("{0} rows.")
    @AlternateMessage({"one", "{0} row."})
    String updateCountMessage(@PluralCount int count);

    @DefaultMessage("Export")
    String exportCsvLabel();

    @DefaultMessage("Execution mode:")
    String executionModeLabel();

    @DefaultMessage("Execute all - ignore and report errors")
    String executeAllModeItem();

    @DefaultMessage("First error - stop on first error")
    String stopOnErrorModeitem();

    @DefaultMessage("Transaction - rollback on first error")
    String transactionModeItem();

    @DefaultMessage("Query Results")
    String queryResultsTitle();

    @DefaultMessage("Query Error")
    String queryErrorTitle();
}
