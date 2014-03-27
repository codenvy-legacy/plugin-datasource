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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracle;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.codeassist.SqlCodeAssistProcessor;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.texteditor.api.TextEditorConfiguration;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.texteditor.api.parser.BasicTokenFactory;
import com.codenvy.ide.texteditor.api.parser.CmParser;
import com.codenvy.ide.texteditor.api.parser.Parser;

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
