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

import com.codenvy.ide.api.ui.action.Action;
import com.codenvy.ide.api.ui.action.ActionEvent;
import com.codenvy.ide.api.ui.workspace.PartStackType;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.google.inject.Inject;

/**
 * Action to launch and show a SQL editor.
 * 
 * @author "Mickaël Leduque"
 */
public class SqlRequestLauncherAction extends Action {

    /** The workspace agent. */
    private final WorkspaceAgent            workspaceAgent;

    /** The factory to create new SQL editors. */
    private final SqlRequestLauncherFactory requestLauncherFactory;

    @Inject
    public SqlRequestLauncherAction(final SqlRequestLauncherConstants constants,
                                    final WorkspaceAgent workspaceAgent,
                                    final SqlRequestLauncherFactory requestLauncherFactory) {
        super(constants.menuEntryOpenSqlEditor());
        this.workspaceAgent = workspaceAgent;
        this.requestLauncherFactory = requestLauncherFactory;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        SqlRequestLauncherPresenter requestLauncher = this.requestLauncherFactory.createSqlRequestLauncherPresenter();
        this.workspaceAgent.openPart(requestLauncher, PartStackType.EDITING);
    }

}
