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

    @DefaultMessage("< empty result >")
    String emptyResult();

    @DefaultMessage("No datasource selected")
    String executeNoDatasourceTitle();

    @DefaultMessage("Please select the datasource to execute the request.")
    String executeNoDatasourceMessage();

    @DefaultMessage("No execution mode selected")
    String executeNoExecutionModeTitle();

    @DefaultMessage("Please choose an execution mode for the request.")
    String executeNoExecutionModeMessage();

    @DefaultMessage("The result limit can''t be negative. It was reset to the previous value.")
    String userErrorNegativeLimit();

    @DefaultMessage("The result limit can''t be zero. It was reset to the previous value.")
    String userErrorZeroLimit();
}
