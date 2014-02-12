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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Position;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.texteditor.api.CodeAssistCallback;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.util.loging.Log;

public class SqlCodeAssistProcessor implements CodeAssistProcessor {

    private SqlEditorResources resources;

    public SqlCodeAssistProcessor(SqlEditorResources resources) {
        this.resources = resources;
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
        if (query.getPrefix() == null) {
            codeAssistCallback.proposalComputed(null);
            return;
        }

        InvocationContext invocationContext = new InvocationContext(query, offset, resources, textEditorPartView);
        Array<SqlCodeCompletionProposal> completionProposals = SqlCodeTrie.findAndFilterAutocompletions(query);
        codeAssistCallback.proposalComputed(jsToArray(completionProposals, invocationContext));
    }

    /**
     * Build a query for the given editor. The query object will than be used to search for possible completion proposal through the trie.
     */
    protected SqlCodeQuery buildQuery(TextEditorPartView textEditorPartView) {
        // Extract the current cursor position and the document being edited from the text editor.
        Position cursorPosition = textEditorPartView.getSelection().getCursorPosition();
        Document document = textEditorPartView.getDocument();
        return buildQuery(cursorPosition, document);
    }

    protected SqlCodeQuery buildQuery(Position cursorPosition, Document document) {
        try {
            int line = document.getLineOfOffset(cursorPosition.getOffset());
            Region region = document.getLineInformation(line);
            int column = cursorPosition.getOffset() - region.getOffset();

            // get the text before the cursor
            Region lineInformation = document.getLineInformation(line);
            String text = document.get(lineInformation.getOffset(), column);

            return getQuery(text);
        } catch (BadLocationException e) {
            Log.error(SqlCodeAssistProcessor.class, e);
            return null;
        }
    }

    protected SqlCodeQuery getQuery(String text) {
        return new SqlCodeQuery(text);
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
