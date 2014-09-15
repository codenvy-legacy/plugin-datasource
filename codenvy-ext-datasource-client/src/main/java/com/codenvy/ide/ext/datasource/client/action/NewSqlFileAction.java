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
package com.codenvy.ide.ext.datasource.client.action;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.ide.api.app.AppContext;
import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.selection.SelectionAgent;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.newresource.DefaultNewResourceAction;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

/**
 * IDE action to create a new SQL file.
 *
 * @author Artem Zatsarynnyy
 */
public class NewSqlFileAction extends DefaultNewResourceAction {

    @Inject
    public NewSqlFileAction(AppContext appContext,
                            SelectionAgent selectionAgent,
                            EditorAgent editorAgent,
                            DatasourceUiResources resources,
                            EventBus eventBus,
                            ProjectServiceClient projectServiceClient) {
        super("SQL File", "Creates new SQL file", null, resources.sqlIcon(), appContext, selectionAgent, editorAgent, projectServiceClient, eventBus);
    }

    @Override
    protected String getExtension() {
        return SqlEditorExtension.SQL_FILE_EXTENSION;
    }

    @Override
    protected String getMimeType() {
        return SqlEditorExtension.GENERIC_SQL_MIME_TYPE;
    }

}
