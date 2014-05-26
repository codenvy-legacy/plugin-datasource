/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.client.action;

import com.codenvy.ide.api.editor.EditorAgent;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.selection.SelectionAgent;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.newresource.DefaultNewResourceAction;
import com.google.inject.Inject;

/**
 * IDE action to create a new SQL file.
 *
 * @author Artem Zatsarynnyy
 */
public class NewSqlFileAction extends DefaultNewResourceAction {

    @Inject
    public NewSqlFileAction(ResourceProvider resourceProvider,
                            SelectionAgent selectionAgent,
                            EditorAgent editorAgent,
                            SqlEditorResources resources) {
        super("SQL File", "Creates new SQL file", resources.sqlFile(), null, resourceProvider, selectionAgent, editorAgent);
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
