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

package com.codenvy.ide.ext.datasource.shared;

/**
 * Explore mode, to tell which table types are returned.
 * 
 * @author "Mickaël Leduque"
 */
public enum ExploreTableType {

    /** Explore simplest mode. Only tables and views. */
    SIMPLE(0),
    /** Default explore mode. Adds materialized views, alias and synonyms toSIMPLE. */
    STANDARD(1),
    /** Explore mode with STANDARD tables plus system tables and views. */
    SYSTEM(2),
    /** Explore mode for all table/entities types. */
    ALL(3);

    private final int index;

    private ExploreTableType(final int index) {
        this.index = index;
    }

    /**
     * Returns the index of the enum.
     * 
     * @return the index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Retrieve the enum value that has this index.
     * 
     * @param searchedIndex the index
     * @return the enum value or null if there is none
     */
    public static ExploreTableType fromIndex(final int searchedIndex) {
        for (final ExploreTableType type : values()) {
            if (type.getIndex() == searchedIndex) {
                return type;
            }
        }
        return null;
    }
}
