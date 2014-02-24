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
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.DatabaseInfoOracle;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.google.gwt.dev.util.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class TestSqlCodeAssistProcessorBuildQuery {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
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
        String editorId = "testEditorInputId";
        when(textEditor.getEditorInput().getFile().getId()).thenReturn(editorId);
        when(editorDatasourceOracle.getSelectedDatasourceId(editorId)).thenReturn("datasourceId");
        when(databaseInfoOracle.getSchemasFor("datasourceId")).thenReturn(Lists.create("public", "meta"));
        when(databaseInfoOracle.getTablesFor("datasourceId", "public")).thenReturn(Lists.create("table", "atable"));
        when(databaseInfoOracle.getTablesFor("datasourceId", "meta")).thenReturn(Lists.create("metaTable", "aMetaTable"));
        when(databaseInfoOracle.getColumnsFor("datasourceId", "public", "atable")).thenReturn(Lists.create("column11", "column12"));
        when(databaseInfoOracle.getColumnsFor("datasourceId", "public", "table")).thenReturn(Lists.create("column21", "column22", "coumn23"));
        when(databaseInfoOracle.getColumnsFor("datasourceId", "meta", "metaTable")).thenReturn(Lists.create("column11", "column12",
                                                                                                            "column222"));
        when(databaseInfoOracle.getColumnsFor("datasourceId", "meta", "aMetaTable")).thenReturn(Lists.create("column11", "column12",
                                                                                                             "column234"));

        codeAssistProcessor = new SqlCodeAssistProcessor(textEditor, resources, databaseInfoOracle, editorDatasourceOracle);
    }


    @Test
    public void completeSelectTemplate() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findAutoCompletions(new SqlCodeQuery("SELEC"));
        Assert.assertEquals("For number of results for SELEC autocompletion, we expect ", 3, results.size());
    }

    @Test
    public void completeSelectTemplateAfter2statements() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findAutoCompletions(new SqlCodeQuery("\nSELEC"));
        Assert.assertEquals("For number of results for SELEC autocompletion after two statements, we expect ", 3, results.size());
    }

    @Test
    public void completeInsertTemplate() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findAutoCompletions(new SqlCodeQuery("inser"));
        assertEquals("For number of results for inser autocompletion, we expect ", 1, results.size());
        assertEquals("Replacement String should be", "INSERT INTO aTable (column1, column2) VALUES ('value1', 0);",
                     results.get(0).replacementString);
    }

    @Test
    public void completeTableForSelectFrom() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery("Select * from "));
        assertEquals("for number of results for table autocompletion, we expect ", 4, results.size());
    }

    @Test
    public void completeTableForFirstLetterSelectFrom() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery("Select * from t"));
        assertEquals("for number of results for table autocompletion starting with t, we expect ", 1, results.size());
    }

    @Test
    public void completeTableCaseInsensitive() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery("Select * from metat"));
        assertEquals("for number of results for table autocompletion starting with metat, we expect ", 1, results.size());
    }

    @Test
    public void completeTableForSelectFromMultipleTable() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery("Select * from atable, "));
        assertEquals("for number of results for table autocompletion (with multiple tables selected), we expect ", 4, results.size());
    }

    @Test
    public void completeTableForSelectFromFourthTable() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findTableAutocompletions(
                                                                      new SqlCodeQuery("Select * from atable, another, table, "));
        assertEquals("for number of results for table autocompletion (with 4+ tables selected), we expect ", 4, results.size());
    }

    @Test
    public void completeTableForSelectFromTableWithSchema() {
        Array<SqlCodeCompletionProposal> results =
                                                   codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery(
                                                                                                                 "Select * from schema.atable, "));
        assertEquals("for number of results for table autocompletion with a table with schema selected), we expect ", 4, results.size());
    }

    @Test
    public void completeTableForSelectFromWithCarriageRuturns() {
        Array<SqlCodeCompletionProposal> results =
                                                   codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery(
                                                                                                                 "select * \nfrom "));
        assertEquals("for number of results for table autocompletion with a statement with CR, we expect ", 4, results.size());
    }

    @Test
    public void completeTableWithFullName() {
        Array<SqlCodeCompletionProposal> results =
                                                   codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery(
                                                                                                                 "select * \nfrom pu"));
        assertEquals("for number of results for table autocompletion when starting writing public, we expect ", 2, results.size());
    }

    @Test
    public void completeColumnSelectWhere() {
        testColumnCompletion("select * \nfrom public.atable where ",
                             "For the results of a column autocompletion when using a select/where statement, we expect ",
                             2);
    }

    protected void testColumnCompletion(String queryPrefix, String assertMessage, int expectedResults) {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findColumnAutocompletions(new SqlCodeQuery(queryPrefix));
        assertEquals(assertMessage, expectedResults, results.size());
    }

    @Test
    public void completeColumnInsertInto() {
        testColumnCompletion("INSERT INTO atable (",
                             "For the results of a column autocompletion using an insert into statement, we expect ",
                             2);
    }

    @Test
    public void completeColumnUpdateTable() {
        testColumnCompletion("UPDATE atable SET ",
                             "For the results of a column autocompletion using an UPDATE/SET statement, we expect ",
                             2);
    }

    @Test
    public void completeColumnSelectMultipleTable() {
        testColumnCompletion("Select * from atable, meta.metaTable WHERE "
                             ,
                             "For the results of a column autocompletion using an SELECT/WHERE statement with multiple selected table, we expect ",
                             5);
    }

    @Test
    public void complete2ndColumnSelectWhere() {
        testColumnCompletion("select * \nfrom public.atable where public.atable.column1 = 'test' AND ",
                             "For the results of a column autocompletion when using a select/where statement, we expect ",
                             2);
    }

    @Test
    public void complete2ndColumnInsertInto() {
        testColumnCompletion("INSERT INTO atable (atable.column1, ",
                             "For the results of a column autocompletion using an insert into statement, we expect ",
                             2);
    }

    @Test
    public void complete2ndColumnUpdateTable() {
        testColumnCompletion("UPDATE atable SET column1 = 'value1', ",
                             "For the results of a column autocompletion using an UPDATE/SET statement, we expect ",
                             2);
    }

    @Test
    public void complete2ndColumnSelectMultipleTable() {
        testColumnCompletion("Select * from atable, meta.metaTable WHERE column1 = 'value1' AND "
                             ,
                             "For the results of a column autocompletion using an SELECT/WHERE statement with multiple selected table, we expect ",
                             5);
    }

}
