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

import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.text.Region;
import com.codenvy.ide.text.RegionImpl;
import com.codenvy.ide.text.edits.MalformedTreeException;
import com.codenvy.ide.text.edits.ReplaceEdit;
import com.codenvy.ide.texteditor.api.codeassistant.Completion;
import com.codenvy.ide.texteditor.api.codeassistant.CompletionProposal;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class SqlCodeCompletionProposal implements CompletionProposal {

    protected String            name;
    protected String            replacementString;
    protected int               cursorPosition;
    protected int               selectionLength = 0;
    protected InvocationContext invocationContext;


    public SqlCodeCompletionProposal(String name) {
        this(name, name, name.length());
    }

    public SqlCodeCompletionProposal(String name, String replacementString, int cursorPosition) {
        this.name = name;
        this.replacementString = replacementString;
        this.cursorPosition = cursorPosition;

    }

    @Override
    public Widget getAdditionalProposalInfo() {
        return null;
    }

    @Override
    public String getDisplayString() {
        return new SafeHtmlBuilder().appendEscaped(name).toSafeHtml().asString();
    }

    @Override
    public Image getImage() {
        Image image = new Image();
        image.setResource(invocationContext.getResources().sqlCompletionIcon());
        return image;
    }

    @Override
    public char[] getTriggerCharacters() {
        // when space is entered we may have completion
        return new char[]{' '};
        // return new char[0];
    }

    @Override
    public boolean isAutoInsertable() {
        return true;
    }

    @Override
    public void getCompletion(CompletionCallback completionCallback) {
        completionCallback.onCompletion(new Completion() {
            /** {@inheritDoc} */
            @Override
            public void apply(Document document) {
                ReplaceEdit replaceEdit =
                                          new ReplaceEdit(
                                                          invocationContext.getOffset()
                                                              - invocationContext.getQuery().getLastQueryPrefix().length(),
                                                          invocationContext.getQuery().getLastQueryPrefix().length(), replacementString);
                try {
                    replaceEdit.apply(document);
                    // Do not try a new codeassist proposal
                    // invocationContext.getEditor().doOperation(TextEditorOperations.CODEASSIST_PROPOSALS);
                } catch (MalformedTreeException e) {
                    Log.error(getClass(), e);
                } catch (BadLocationException e) {
                    Log.error(getClass(), e);
                }
            }

            /** {@inheritDoc} */
            @Override
            public Region getSelection(Document document) {
                return new RegionImpl(invocationContext.getOffset() + cursorPosition
                                      - invocationContext.getQuery().getLastQueryPrefix().length(), selectionLength);
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setInvocationContext(InvocationContext invocationContext) {
        this.invocationContext = invocationContext;
    }

    public String getReplacementString() {
        return replacementString;
    }

    public void setReplacementString(String replacementString) {
        this.replacementString = replacementString;
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    public void setSelectionLength(int selectionLength) {
        this.selectionLength = selectionLength;
    }

}
