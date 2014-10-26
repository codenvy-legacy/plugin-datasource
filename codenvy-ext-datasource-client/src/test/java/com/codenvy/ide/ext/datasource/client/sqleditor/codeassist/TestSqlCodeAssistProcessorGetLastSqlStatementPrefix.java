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
package com.codenvy.ide.ext.datasource.client.sqleditor.codeassist;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.api.text.BadLocationException;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;
import com.codenvy.ide.jseditor.client.document.EmbeddedDocument;
import com.codenvy.ide.jseditor.client.text.LinearRange;
import com.codenvy.ide.jseditor.client.text.TextPosition;
import com.codenvy.ide.jseditor.client.texteditor.ConfigurableTextEditor;

@RunWith(MockitoJUnitRunner.class)
public class TestSqlCodeAssistProcessorGetLastSqlStatementPrefix {

    @Mock
    protected ConfigurableTextEditor textEditor;

    @Mock
    protected SqlEditorResources        resources;
    @Mock
    protected DatabaseInfoOracle        databaseInfoOracle;
    @Mock
    protected EditorDatasourceOracle    editorDatasourceOracle;

    protected SqlCodeAssistProcessor    codeAssistProcessor;

    @Mock
    private EmbeddedDocument document;

    @Before
    public void init() {
        codeAssistProcessor = new SqlCodeAssistProcessor(textEditor, resources, databaseInfoOracle, editorDatasourceOracle);
    }

    @Test
    public void testingSqlStatementWithCarriageReturns() throws BadLocationException {
        String expectedlastQuery = "select * \nfrom table\nwhere colum";
        String content = "Select * from Database;\n" + expectedlastQuery;

        int position = content.length();

        Mockito.when(document.getPositionFromIndex(Matchers.eq(position))).thenReturn(new TextPosition(0, position));
        Mockito.when(document.getLinearRangeForLine(anyInt())).thenReturn(LinearRange.createWithStart(0).andLength(content.length()));
        Mockito.when(document.getContentRange(Matchers.eq(0), Matchers.eq(content.length()))).thenReturn(content);

        assertEquals("last sql query is", expectedlastQuery, codeAssistProcessor.getLastSQLStatementPrefix(position, document));
    }
    
    @Test
    public void testingSqlStatementWithCarriageReturns2() throws BadLocationException {
        String expectedlastQuery = "select * \nfrom table\nwhere ";
        String content = "Select * from Database\n;" + expectedlastQuery;

        int position = content.length();

        Mockito.when(document.getPositionFromIndex(Matchers.eq(position))).thenReturn(new TextPosition(0, position));
        Mockito.when(document.getLinearRangeForLine(anyInt())).thenReturn(LinearRange.createWithStart(0).andLength(content.length()));
        Mockito.when(document.getContentRange(Matchers.eq(0), Matchers.eq(content.length()))).thenReturn(content);

        assertEquals("last sql query is", expectedlastQuery, codeAssistProcessor.getLastSQLStatementPrefix(position, document));
    }
}
