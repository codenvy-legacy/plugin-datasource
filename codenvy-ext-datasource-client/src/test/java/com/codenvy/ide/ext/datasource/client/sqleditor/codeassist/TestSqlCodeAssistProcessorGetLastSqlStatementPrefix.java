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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;
import com.codenvy.ide.text.BadLocationException;
import com.codenvy.ide.text.DocumentImpl;
import com.codenvy.ide.text.Position;

@RunWith(MockitoJUnitRunner.class)
public class TestSqlCodeAssistProcessorGetLastSqlStatementPrefix {

    @Mock
    protected ReadableContentTextEditor textEditor;

    @Mock
    protected SqlEditorResources        resources;
    @Mock
    protected DatabaseInfoOracle        databaseInfoOracle;
    @Mock
    protected EditorDatasourceOracle    editorDatasourceOracle;

    protected SqlCodeAssistProcessor    codeAssistProcessor;

    @Before
    public void init() {
        codeAssistProcessor = new SqlCodeAssistProcessor(textEditor, resources, databaseInfoOracle, editorDatasourceOracle);
    }

    @Test
    public void testingSqlStatementWithCarriageReturns() throws BadLocationException {
        String expectedlastQuery = "select * \nfrom table\nwhere colum";
        String content = "Select * from Database;\n" + expectedlastQuery;

        DocumentImpl document = new DocumentImpl(content);
        Position position = new Position(content.length());
        assertEquals("last sql query is", expectedlastQuery, codeAssistProcessor.getLastSQLStatementPrefix(position, document));
    }
    
    @Test
    public void testingSqlStatementWithCarriageReturns2() throws BadLocationException {
        String expectedlastQuery = "select * \nfrom table\nwhere ";
        String content = "Select * from Database\n;" + expectedlastQuery;

        DocumentImpl document = new DocumentImpl(content);
        Position position = new Position(content.length());
        assertEquals("last sql query is", expectedlastQuery, codeAssistProcessor.getLastSQLStatementPrefix(position, document));
    }
}
