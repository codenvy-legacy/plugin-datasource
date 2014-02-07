package com.codenvy.ide.ext.datasource.client.sqleditor.codeassist;

import com.codenvy.ide.texteditor.api.CodeAssistCallback;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;

public class SqlCodeAssistProcessor implements CodeAssistProcessor {

    @Override
    public void computeCompletionProposals(final TextEditorPartView view,
                                           final int offset,
                                           final CodeAssistCallback callback) {
        callback.proposalComputed(null);

    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

}
