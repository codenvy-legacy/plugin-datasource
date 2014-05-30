/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
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
