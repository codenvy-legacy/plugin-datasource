/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.ext.datasource.client.sqleditor.codeassist;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracle;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
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
        String expectedlastQuery = "\nselect * \nfrom table\nwhere colum";
        String content = "Select * from Database;" + expectedlastQuery;

        DocumentImpl document = new DocumentImpl(content);
        Position position = new Position(content.length());
        assertEquals("last sql query is", expectedlastQuery, codeAssistProcessor.getLastSQLStatementPrefix(position, document));
    }
    
    @Test
    public void testingSqlStatementWithCarriageReturns2() throws BadLocationException {
        String expectedlastQuery = "\nselect * \nfrom table\nwhere ";
        String content = "Select * from Database;" + expectedlastQuery;

        DocumentImpl document = new DocumentImpl(content);
        Position position = new Position(content.length());
        assertEquals("last sql query is", expectedlastQuery, codeAssistProcessor.getLastSQLStatementPrefix(position, document));
    }
}
