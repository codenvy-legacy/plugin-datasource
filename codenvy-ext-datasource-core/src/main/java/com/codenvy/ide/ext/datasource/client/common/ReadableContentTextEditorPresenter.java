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
