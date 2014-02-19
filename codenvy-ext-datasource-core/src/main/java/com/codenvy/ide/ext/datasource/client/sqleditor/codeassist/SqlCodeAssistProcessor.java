/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.sqleditor.codeassist;

import java.util.Collection;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracle;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Position;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.texteditor.api.CodeAssistCallback;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class SqlCodeAssistProcessor implements CodeAssistProcessor {

    private SqlEditorResources          resources;
    protected DatabaseInfoOracle        databaseInfoOracle;
    protected ReadableContentTextEditor textEditor;
    protected EditorDatasourceOracle    editorDatasourceOracle;

    protected final static RegExp       TABLE_REGEXP_PATTERN =
                                                               RegExp.compile(".*((from((\\s+(\\w+.)*\\w+\\s*,)*))|insert into|alter table|update)\\s+(\\w*.*\\w*)$");
    protected final static int          TABLE_REGEXP_GROUP   = 6;

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
        return textBefore;

    }

    protected SqlCodeQuery getQuery(String text) {
        return new SqlCodeQuery(text);
    }

    protected Array<SqlCodeCompletionProposal> findAutoCompletions(SqlCodeQuery query) {
        Array<SqlCodeCompletionProposal> completionProposals = findTableAutocompletions(query);
        completionProposals.addAll(SqlCodeTemplateTrie.findAndFilterAutocompletions(query));
        return completionProposals;
    }

    protected Array<SqlCodeCompletionProposal> findTableAutocompletions(SqlCodeQuery query) {
        Array<SqlCodeCompletionProposal> array = Collections.createArray();
        String linePrefix = query.getLastQueryPrefix();
        linePrefix = linePrefix.toLowerCase();

        MatchResult matcher = TABLE_REGEXP_PATTERN.exec(linePrefix);
        if (matcher != null) {
            String tablePrefix = matcher.getGroup(TABLE_REGEXP_GROUP);
            String lineReplacementPrefix = linePrefix.substring(0, linePrefix.length() - tablePrefix.length());
            String datasourceId = editorDatasourceOracle.getSelectedDatasourceId(textEditor.getEditorInput().getFile().getId());
            Collection<String> schemas = databaseInfoOracle.getSchemasFor(datasourceId);
            for (String schema : schemas) {
                Collection<String> tables = databaseInfoOracle.getTablesFor(datasourceId, schema);
                for (String table : tables) {
                    if (table.startsWith(tablePrefix) || (schema + "." + table).startsWith(tablePrefix)) {
                        String replacementString = lineReplacementPrefix + schema + "." + table;
                        array.add(new SqlCodeCompletionProposal(schema + "." + table, replacementString, replacementString.length()));
                    }
                }
            }
        }
        return array;
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
