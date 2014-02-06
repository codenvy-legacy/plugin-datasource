package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.util.loging.Log;
import com.google.inject.Inject;

public class SqlLauncherEditorProvider implements EditorProvider {

    private SqlRequestLauncherFactory sqlRequestLauncherFactory;

    @Inject
    public SqlLauncherEditorProvider(final SqlRequestLauncherFactory sqlRequestLauncherFactory) {
        this.sqlRequestLauncherFactory = sqlRequestLauncherFactory;
    }

    @Override
    public EditorPartPresenter getEditor() {
        Log.info(SqlLauncherEditorProvider.class, "New instance of SQL launcher editor requested.");
        return sqlRequestLauncherFactory.createSqlRequestLauncherPresenter();
    }
}
