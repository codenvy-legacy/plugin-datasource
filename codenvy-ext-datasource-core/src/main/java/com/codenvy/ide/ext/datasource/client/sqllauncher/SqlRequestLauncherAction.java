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
        SqlRequestLauncherAdapter requestLauncher = this.requestLauncherFactory.createSqlRequestLauncherAdapter();
        this.workspaceAgent.openPart(requestLauncher, PartStackType.EDITING);
    }

}
