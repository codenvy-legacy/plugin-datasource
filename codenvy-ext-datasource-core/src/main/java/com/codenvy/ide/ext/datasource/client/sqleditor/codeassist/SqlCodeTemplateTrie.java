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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.util.AbstractTrie;

public class SqlCodeTemplateTrie {
    private static final Array<String> ELEMENTS = Collections.createArray(
            "SELECT * FROM aTable WHERE column = 'value';",//
            "SELECT COUNT(1) FROM aTable;",//
            "INSERT INTO aTable (column1, column2) VALUES ('value1', 0);",//
            "DELETE FROM aTable WHERE column = 'value';",//
            "UPDATE aTable SET column2 = 30 WHERE column1 = 'value1';",//
            "SELECT * FROM aTable WHERE column LIKE '%rg%';",//
            "CREATE TABLE aTable (IntColumn int, VarcharColumn1 varchar(255), VarcharColumn2 varchar(255));"
                                                                         );

    private static final AbstractTrie<SqlCodeCompletionProposal> sqlCodeTrie = createTrie();

    private static AbstractTrie<SqlCodeCompletionProposal> createTrie() {
        AbstractTrie<SqlCodeCompletionProposal> result = new AbstractTrie<SqlCodeCompletionProposal>();
        for (String name : ELEMENTS.asIterable()) {
            result.put(name.toLowerCase(), new SqlCodeCompletionProposal(name));
        }
        return result;
    }

    public static Array<SqlCodeCompletionProposal> findAndFilterAutocompletions(SqlCodeQuery query) {
        // use tolower case
        String prefix = query.getLastQueryPrefix();

        // search attributes
        Array<SqlCodeCompletionProposal> searchedProposals = sqlCodeTrie.search(prefix.toLowerCase());
        return searchedProposals;
    }
}
