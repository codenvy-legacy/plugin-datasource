package com.codenvy.ide.ext.datasource.client.common;

import com.codenvy.ide.Resources;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.debug.BreakpointGutterManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.texteditor.TextEditorPresenter;
import com.codenvy.ide.texteditor.selection.SelectionModel;
import com.codenvy.ide.util.executor.UserActivityManager;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class ReadableContentTextEditorPresenter extends TextEditorPresenter implements ReadableContentTextEditor {

    @Inject
    public ReadableContentTextEditorPresenter(final Resources resources,
                                              final UserActivityManager userActivityManager,
                                              final BreakpointGutterManager breakpointGutterManager,
                                              final DtoFactory dtoFactory,
                                              final WorkspaceAgent workspaceAgent,
                                              final EventBus eventBus) {
        super(resources,
              userActivityManager,
              breakpointGutterManager,
              dtoFactory,
              workspaceAgent,
              eventBus);
    }

    @Override
    public String getEditorContent() {
        return editor.getTextStore().asText();
    }

    @Override
    public String getSelectedContent() {
        final SelectionModel selectionModel = editor.getSelection();
        return selectionModel.getSelectedText();
    }

}
