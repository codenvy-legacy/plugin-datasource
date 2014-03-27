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

import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;

public class ExecuteSqlAction extends Action {

    private EditorAgent editorAgent;

    @Inject
    public ExecuteSqlAction(final EditorAgent editorAgent) {
        this.editorAgent = editorAgent;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final EditorPartPresenter editor = editorAgent.getActiveEditor();
        if (editor instanceof SqlRequestLauncherPresenter) {
            Log.info(ExecuteSqlAction.class, "Execute SQL action triggered.");
            SqlRequestLauncherPresenter sqlEditor = (SqlRequestLauncherPresenter)editor;
            sqlEditor.executeRequested();
        } else {
            Log.warn(ExecuteSqlAction.class, "Execute SQL action triggered but active editor is not compatible.");
        }

    }

}
