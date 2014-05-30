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
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorResources;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoOracle;
import com.google.gwt.dev.util.collect.Lists;

/**
 * Testing template based, table and column completion processing.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSqlCodeAssistProcessor {

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
        Assert.assertEquals("For number of results for SELEC autocompletion, we expect ", 5, results.size());
    }

    @Test
    public void completeSelectTemplateAfter2statements() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findAutoCompletions(new SqlCodeQuery("\nSELEC"));
        Assert.assertEquals("For number of results for SELEC autocompletion after two statements, we expect ", 5, results.size());
    }

    @Test
    public void completeInsertTemplate() {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findAutoCompletions(new SqlCodeQuery("inser"));
        assertEquals("For number of results for inser autocompletion, we expect ", 1, results.size());
        assertEquals("Replacement String should be", "INSERT INTO aTable (column1, column2) VALUES ('value1', 0);",
                     results.get(0).getReplacementString());
    }

    @Test
    public void completeTableForSelectFrom() {
        testTableCompletion("Select * from ", "for number of results for table autocompletion, we expect ", 4);
    }

    protected Array<SqlCodeCompletionProposal> testTableCompletion(String queryPrefix, String assertMessage, int expectedResultsCount) {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findTableAutocompletions(new SqlCodeQuery(queryPrefix));
        assertEquals(assertMessage, expectedResultsCount, results.size());
        return results;
    }

    @Test
    public void completeTableForFirstLetterSelectFrom() {
        Array<SqlCodeCompletionProposal> result = testTableCompletion("Select * from t", "for number of results for table autocompletion starting with t, we expect ", 1);
        assertEquals("result completion should be","Select * from public.table", result.get(0).replacementString);
    }

    @Test
    public void completeTableCaseInsensitive() {
        testTableCompletion("Select * from metat", "for number of results for table autocompletion starting with metat, we expect ", 1);
    }

    @Test
    public void completeTableForSelectFromMultipleTable() {
        testTableCompletion("Select * from atable, ",
                            "for number of results for table autocompletion (with multiple tables selected), we expect ",
                            4);
    }

    @Test
    public void completeTableForSelectFromFourthTable() {
        testTableCompletion("Select * from atable, another, table, ",
                            "for number of results for table autocompletion (with 4+ tables selected), we expect ", 4);
    }

    @Test
    public void completeTableForSelectFromTableWithSchema() {
        testTableCompletion("Select * from schema.atable, ",
                            "for number of results for table autocompletion with a table with schema selected), we expect ",
                            4);
    }

    @Test
    public void completeTableForSelectFromWithCarriageRuturns() {
        testTableCompletion("select * \nfrom ",
                            "for number of results for table autocompletion with a statement with CR, we expect ",
                            4);
    }

    @Test
    public void completeTableWithFullName() {
        testTableCompletion(
                            "select * \nfrom pu",
                            "for number of results for table autocompletion when starting writing public, we expect ",
                            2);
    }

    @Test
    public void completeColumnSelectWhere() {
        testColumnCompletion("select * \nfrom public.atable where ",
                             "For the results of a column autocompletion when using a select/where statement, we expect ",
                             2);
    }

    protected void testColumnCompletion(String queryPrefix, String assertMessage, int expectedResultsCount) {
        Array<SqlCodeCompletionProposal> results = codeAssistProcessor.findColumnAutocompletions(new SqlCodeQuery(queryPrefix));
        assertEquals(assertMessage, expectedResultsCount, results.size());
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
        testColumnCompletion("Select * from atable, meta.metaTable WHERE ",
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
        testColumnCompletion("Select * from atable, meta.metaTable WHERE column1 = 'value1' AND ",
                             "For the results of a column autocompletion using an SELECT/WHERE statement with multiple selected table, we expect ",
                             5);
    }

}
