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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.codenvy.ide.api.text.Document;
import com.codenvy.ide.api.texteditor.TextEditorConfiguration;
import com.codenvy.ide.api.texteditor.TextEditorPartView;
import com.codenvy.ide.api.texteditor.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.api.texteditor.parser.BasicTokenFactory;
import com.codenvy.ide.api.texteditor.parser.CmParser;
import com.codenvy.ide.api.texteditor.parser.Parser;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.codeassist.SqlCodeAssistProcessor;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;

import javax.validation.constraints.NotNull;

public class SqlEditorConfiguration extends TextEditorConfiguration {

    private SqlCodeAssistProcessor          codeAssistProcessor;
    private final SqlEditorResources        resource;
    private final DatabaseInfoOracle        databaseInfoOracle;
    private final ReadableContentTextEditor textEditor;
    private final EditorDatasourceOracle    editorDatasourceOracle;

    public SqlEditorConfiguration(@NotNull final ReadableContentTextEditor textEditor,
                                  @NotNull final SqlEditorResources resource,
                                  @NotNull final DatabaseInfoOracle databaseInfoOracle,
                                  @NotNull final EditorDatasourceOracle editorDatasourceOracle) {
        this.textEditor = textEditor;
        this.resource = resource;
        this.databaseInfoOracle = databaseInfoOracle;
        this.editorDatasourceOracle = editorDatasourceOracle;
    }

    @Override
    public Parser getParser(final TextEditorPartView view) {
        CmParser parser = getParserForMime(SqlEditorExtension.GENERIC_SQL_MIME_TYPE);
        parser.setNameAndFactory("sql", new BasicTokenFactory());
        return parser;
    }

    @Override
    public StringMap<CodeAssistProcessor> getContentAssistantProcessors(final TextEditorPartView view) {
        StringMap<CodeAssistProcessor> map = Collections.createStringMap();
        map.put(Document.DEFAULT_CONTENT_TYPE, getOrCreateCodeAssistProcessor());
        return map;
    }

    private SqlCodeAssistProcessor getOrCreateCodeAssistProcessor() {
        if (codeAssistProcessor == null) {
            codeAssistProcessor = new SqlCodeAssistProcessor(textEditor, resource, databaseInfoOracle, editorDatasourceOracle);
        }
        return codeAssistProcessor;
    }
}
