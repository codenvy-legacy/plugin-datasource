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
