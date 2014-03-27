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
package com.codenvy.ide.ext.datasource.client.sqleditor.codeassist;

import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.texteditor.api.TextEditorPartView;

public class InvocationContext {
    private final SqlCodeQuery       query;

    private final int                offset;

    private final SqlEditorResources resources;

    private final TextEditorPartView editor;

    public InvocationContext(SqlCodeQuery query, int offset, SqlEditorResources resources, TextEditorPartView editor) {
        super();
        this.query = query;
        this.offset = offset;
        this.resources = resources;
        this.editor = editor;
    }

    public SqlCodeQuery getQuery() {
        return query;
    }

    public int getOffset() {
        return offset;
    }

    public SqlEditorResources getResources() {
        return resources;
    }

    public TextEditorPartView getEditor() {
        return editor;
    }
}
