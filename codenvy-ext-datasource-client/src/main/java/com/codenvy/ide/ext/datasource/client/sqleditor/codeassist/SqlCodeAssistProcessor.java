/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.sqleditor.codeassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;
import com.codenvy.ide.text.*;
import com.codenvy.ide.texteditor.api.CodeAssistCallback;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class SqlCodeAssistProcessor implements CodeAssistProcessor {

    private final SqlEditorResources        resources;
    private final DatabaseInfoOracle        databaseInfoOracle;
    private final ReadableContentTextEditor textEditor;
    private final EditorDatasourceOracle    editorDatasourceOracle;

    private final static RegExp             TABLE_REGEXP_PATTERN               =
																				 RegExp.compile(".*((from((\\s+(\\w+\\.)*\\w+\\s*,)*))|insert into|alter table|update|join)\\s+(\\w*.*\\w*)$"); 
    private final static int                TABLE_REGEXP_GROUP                 = 6;
    private final static RegExp             COLUMN_REGEXP_PATTERN              =
                                                                                 RegExp.compile(".*((from((\\s+((\\w+\\.)*\\w+)\\s*,)*))|insert into|alter table|update)"
                                                                                                    + "\\s+(\\w*\\.?(\\w+))\\s+((.+\\s+)*)"
                                                                                                    + "((where\\s+(.*\\s+)*|\\()(\\w*\\.*\\w*\\.*\\w+,?\\s+)*|set\\s+((.+\\s*)+,\\s*)*)(\\w*\\.*\\w*\\.*\\w*)"
                                                                                                    +"|((from((\\s+((\\w+\\.)*\\w+)\\s*,)*))\\s+(\\w*\\.?(\\w+))\\s+((\\w+\\s+)*)(join\\s+)(\\w*\\.?(\\w+))"
                                                                                                    +"(\\s+on\\s+(.*\\s+)*)(\\w*\\.*\\w*\\.*\\w*))$"
                                                                                                , "gm");
    private final static int                COLUMN_REGEXP_GROUP                = 17;
    private final static int                JOIN_COLUMN_REGEXP_GROUP                = 33;
    private final static int                TABLE_IN_COLUMN_REGEXP_GROUP       = 7;
    private final static int                JOIN_TABLE_IN_COLUMN_REGEXP_GROUP       = 29;
    private final static int                PREV_TABLES_IN_COLUMN_REGEXP_GROUP = 5;
    private final static int                JOIN_PREV_TABLES_IN_COLUMN_REGEXP_GROUP = 24;
    private final static int                STATEMENT_TYPE_REGEXP_GROUP 	   = 2;
    private final static int                JOIN_STATEMENT_TYPE_REGEXP_GROUP 		= 19;

    public SqlCodeAssistProcessor(ReadableContentTextEditor textEditor,
                                  SqlEditorResources resources,
                                  DatabaseInfoOracle databaseInfoOracle,
                                  EditorDatasourceOracle editorDatasourceOracle) {
        this.textEditor = textEditor;
        this.resources = resources;
        this.databaseInfoOracle = databaseInfoOracle;
        this.editorDatasourceOracle = editorDatasourceOracle;
    }

    /**
     * Interface API for computing the code completion
     * 
     * @param textEditorPartView the editor
     * @param offset an offset within the document for which completions should be computed
     * @param codeAssistCallback the callback used to provide code completion
     */
    @Override
    public void computeCompletionProposals(TextEditorPartView textEditorPartView, int offset, CodeAssistCallback codeAssistCallback) {
        // Avoid completion when text is selected
        if (textEditorPartView.getSelection().hasSelection()) {
            codeAssistCallback.proposalComputed(null);
            return;
        }

        SqlCodeQuery query = buildQuery(textEditorPartView);

        if (query.getLastQueryPrefix() == null) {
            codeAssistCallback.proposalComputed(null);
            return;
        }
        Array<SqlCodeCompletionProposal> completionProposals = findAutoCompletions(query);
        InvocationContext invocationContext = new InvocationContext(query, offset, resources, textEditorPartView);
        codeAssistCallback.proposalComputed(jsToArray(completionProposals, invocationContext));
    }

    /**
     * Build a query for the given editor. The query object will than be used to search for possible completion proposal through the trie.
     */
    protected SqlCodeQuery buildQuery(TextEditorPartView textEditorPartView) {
        Position cursorPosition = textEditorPartView.getSelection().getCursorPosition();
        Document document = textEditorPartView.getDocument();

        return buildQuery(cursorPosition, document);
    }

    protected SqlCodeQuery buildQuery(Position cursorPosition, Document document) {
        try {
            String text = getLastSQLStatementPrefix(cursorPosition, document);
            return getQuery(text);
        } catch (BadLocationException e) {
            Log.error(SqlCodeAssistProcessor.class, e);
            return null;
        }
    }

    protected String getLastSQLStatementPrefix(Position cursorPosition, Document document) throws BadLocationException {
        int line = document.getLineOfOffset(cursorPosition.getOffset());
        Region region = document.getLineInformation(line);
        int column = cursorPosition.getOffset() - region.getOffset();

        String textBefore = "";

        boolean parsingLineWithCursor = true;

        while ((line >= 0) && (!textBefore.contains(";"))) {
            String text;
            int lastStatementSeparatorIndex;

            Region information = document.getLineInformation(line);
            if (parsingLineWithCursor) {
                text = document.get(information.getOffset(), column);
                parsingLineWithCursor = false;
            } else {
                text = document.get(information.getOffset(), information.getLength()) + "\n";
            }

            lastStatementSeparatorIndex = text.lastIndexOf(';');

            if (lastStatementSeparatorIndex > -1) {
                textBefore = text.substring(lastStatementSeparatorIndex + 1).concat(textBefore);
                break;
            }
            line--;
            textBefore = text.concat(textBefore);
        }
        return textBefore.replaceAll("^\\s*", "");
    }

    protected SqlCodeQuery getQuery(String text) {
        return new SqlCodeQuery(text);
    }

    protected Array<SqlCodeCompletionProposal> findAutoCompletions(SqlCodeQuery query) {
        Array<SqlCodeCompletionProposal> completionProposals = findTableAutocompletions(query);
        completionProposals.addAll(findColumnAutocompletions(query));
        completionProposals.addAll(findTemplateAutocompletion(query));
        return completionProposals;
    }

    protected Array<SqlCodeCompletionProposal> findTableAutocompletions(SqlCodeQuery query) {
        Array<SqlCodeCompletionProposal> array = Collections.createArray();
        String linePrefix = query.getLastQueryPrefix();
        String linePrefixLowerCase = linePrefix.toLowerCase();

        MatchResult matcher = TABLE_REGEXP_PATTERN.exec(linePrefixLowerCase);
        if (matcher != null) {
            String tablePrefix = matcher.getGroup(TABLE_REGEXP_GROUP);            
            String lineReplacementPrefix = linePrefix.substring(0, linePrefix.length() - tablePrefix.length());
            String datasourceId = editorDatasourceOracle.getSelectedDatasourceId(textEditor.getEditorInput().getFile().getId());
            Collection<String> schemas = databaseInfoOracle.getSchemasFor(datasourceId);
            for (String schema : schemas) {
                Collection<String> tables = databaseInfoOracle.getTablesFor(datasourceId, schema);
                for (String table : tables) {
                    String tablePrefixLowerCase = tablePrefix.toLowerCase();
                    String tableLowerCase = table.toLowerCase();
                    String schemaLowerCase = schema.toLowerCase();
                    if (tableLowerCase.startsWith(tablePrefixLowerCase)
                        || (schemaLowerCase + "." + tableLowerCase).startsWith(tablePrefixLowerCase)) {
                        String replacementString = lineReplacementPrefix + schema + "." + table;
                        array.add(new SqlCodeCompletionProposal(schema + "." + table, replacementString, replacementString.length()));
                    }
                }
            }
        }
        return array;
    }

    protected Array<SqlCodeCompletionProposal> findColumnAutocompletions(SqlCodeQuery query) {
        Array<SqlCodeCompletionProposal> array = Collections.createArray();
        String linePrefix = query.getLastQueryPrefix();
        String linePrefixLowerCase = linePrefix.toLowerCase();
        MatchResult matcher = COLUMN_REGEXP_PATTERN.exec(linePrefixLowerCase);
        List<String> selectedTables = new ArrayList<String>();
        boolean isSelectStatement = false;

        String columnPrefix = "";
        while (matcher != null) {
            String statementTypeGroup = matcher.getGroup((linePrefixLowerCase.contains("join")) ? JOIN_STATEMENT_TYPE_REGEXP_GROUP : STATEMENT_TYPE_REGEXP_GROUP);
            isSelectStatement = (statementTypeGroup != null)
                                && !statementTypeGroup.trim().isEmpty()
                                && statementTypeGroup.startsWith("from");
            String selectedTable = matcher.getGroup((linePrefixLowerCase.contains("join")) ? JOIN_TABLE_IN_COLUMN_REGEXP_GROUP : TABLE_IN_COLUMN_REGEXP_GROUP);
            columnPrefix = matcher.getGroup((linePrefixLowerCase.contains("join")) ? JOIN_COLUMN_REGEXP_GROUP : COLUMN_REGEXP_GROUP);
            if (selectedTable != null && !selectedTable.trim().isEmpty() && !selectedTables.contains(selectedTable)) {
                selectedTables.add(selectedTable);
            }
            String prevSelectedTable = matcher.getGroup((linePrefixLowerCase.contains("join")) ? JOIN_PREV_TABLES_IN_COLUMN_REGEXP_GROUP : PREV_TABLES_IN_COLUMN_REGEXP_GROUP);
            if (prevSelectedTable != null && !prevSelectedTable.trim().isEmpty() && !selectedTables.contains(prevSelectedTable)) {
                selectedTables.add(prevSelectedTable);
            }

            matcher = COLUMN_REGEXP_PATTERN.exec(linePrefixLowerCase);
        }

        String lineReplacementPrefix = linePrefix.substring(0, linePrefix.length() - columnPrefix.length());
        String datasourceId = editorDatasourceOracle.getSelectedDatasourceId(textEditor.getEditorInput().getFile().getId());
        Collection<String> schemas = databaseInfoOracle.getSchemasFor(datasourceId);
        for (String schema : schemas) {
            Collection<String> tables = databaseInfoOracle.getTablesFor(datasourceId, schema);
            String schemaLowerCase = schema.toLowerCase();
            for (String table : tables) {
                for (String selectedTable : selectedTables) {
                    String tablePrefixLowerCase = selectedTable.toLowerCase();
                    String tableLowerCase = table.toLowerCase();
                    if (tableLowerCase.startsWith(tablePrefixLowerCase)
                        || (schemaLowerCase + "." + tableLowerCase).startsWith(tablePrefixLowerCase)) {
                        Collection<String> columns = databaseInfoOracle.getColumnsFor(datasourceId, schema, table);
                        for (String column : columns) {
                            String columnPrefixLowerCase = columnPrefix.toLowerCase();
                            String columnLowerCase = column.toLowerCase();
                            if (columnLowerCase.startsWith(columnPrefixLowerCase)
                                || (schemaLowerCase + "." + tableLowerCase + "." + columnLowerCase).startsWith(columnPrefixLowerCase)
                                || (tableLowerCase + "." + columnLowerCase).startsWith(columnPrefixLowerCase)) {
                                String columnReplacementString = isSelectStatement ? (schema + "." + table + "." + column) : column;
                                String replacementString = lineReplacementPrefix + columnReplacementString;
                                array.add(new SqlCodeCompletionProposal(schema + "." + table + "." + column, replacementString,
                                                                        replacementString.length()));
                            }
                        }
                    }
                }
            }
        }
        return array;
    }

    protected Array<SqlCodeCompletionProposal> findTemplateAutocompletion(SqlCodeQuery query) {
        Array<SqlCodeCompletionProposal> findAndFilterAutocompletions = SqlCodeTemplateTrie.findAndFilterAutocompletions(query);
        for (SqlCodeCompletionProposal proposal : findAndFilterAutocompletions.asIterable()) {
            int nextTablePosition = proposal.getReplacementString().indexOf("aTable");
            if (nextTablePosition > 0) {
                proposal.setCursorPosition(nextTablePosition);
                proposal.setSelectionLength((6));
            }
        }
        return findAndFilterAutocompletions;
    }

    /**
     * Convert Javascript array and apply invocation context
     * 
     * @param autocompletions the list of auto completion proposals
     * @param context the given invocation context
     * @return the array
     */
    protected SqlCodeCompletionProposal[] jsToArray(Array<SqlCodeCompletionProposal> autocompletions,
                                                    InvocationContext context) {
        if (autocompletions == null) {
            return null;
        }
        SqlCodeCompletionProposal[] proposals = new SqlCodeCompletionProposal[autocompletions.size()];
        for (int i = 0; i < autocompletions.size(); i++) {
            proposals[i] = autocompletions.get(i);
            proposals[i].setInvocationContext(context);
        }
        return proposals;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return new char[0];
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
