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

/**
 * All the elements needed after having parse the context to compute results.
 */
public class SqlCodeQuery {
    private String lastQueryPrefix;

    /**
     * @param lastQueryPrefix : Prefix of the last query in the SQL file or selected element. for instance "SELECT * FRO".
     */
    public SqlCodeQuery(String lastQueryPrefix) {
        this.lastQueryPrefix = lastQueryPrefix;
    }

    public String getLastQueryPrefix() {
        return lastQueryPrefix;
    }

    public void setLastQueryPrefix(String queryPrefix) {
        this.lastQueryPrefix = queryPrefix;
    }
}
