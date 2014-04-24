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

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.util.AbstractTrie;

public class SqlCodeTemplateTrie {
    private static final Array<String> ELEMENTS = Collections.createArray(
            "SELECT * FROM aTable;",//
            "SELECT * FROM aTable INNER JOIN anotherTable ON aTable.id = anotherTable.id;",//
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
        String prefix = query.getLastQueryPrefix();
        // remove leading trailing space
        prefix = prefix.replaceAll("^\\s*", "").toLowerCase();

        Array<SqlCodeCompletionProposal> searchedProposals = sqlCodeTrie.search(prefix);
        return searchedProposals;
    }
}
