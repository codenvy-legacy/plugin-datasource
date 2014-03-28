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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.ext.datasource.client.DatabaseCategoryType;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Provider;


public class NewDatasourceConnector implements Comparable<NewDatasourceConnector> {

    private final int                                                            priority;
    private final String                                                         id;
    private final String                                                         title;
    private final ImageResource                                                  image;
    private final String                                                         jdbcClassName;
    private final Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages;

    private final DatabaseCategoryType categoryType;

    public NewDatasourceConnector(final String connectorId,
                                  final int priority,
                                  final String title,
                                  final ImageResource logo,
                                  final String jdbcClassName,
                                  final Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages,
                                  final DatabaseCategoryType categoryType) {
        this.id = connectorId;
        this.priority = priority;
        this.title = title;
        this.image = logo;
        this.jdbcClassName = jdbcClassName;
        this.wizardPages = wizardPages;
        this.categoryType = categoryType;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ImageResource getImage() {
        return image;
    }

    public String getJdbcClassName() {
        return jdbcClassName;
    }

    public Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> getWizardPages() {
        return wizardPages;
    }

    public DatabaseCategoryType getCategoryType() {
        return categoryType;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NewDatasourceConnector other = (NewDatasourceConnector)obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(final NewDatasourceConnector o) {
        return new Integer(this.priority).compareTo(o.priority);
    }
}
