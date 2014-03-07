/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import com.codenvy.ide.collections.Array;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Provider;


public class NewDatasourceConnector implements Comparable<NewDatasourceConnector> {

    private final int                                                            priority;
    private final String                                                         id;
    private final String                                                         title;
    private final ImageResource                                                  image;
    private final String                                                         jdbcClassName;
    private final Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages;

    public NewDatasourceConnector(final String connectorId,
                                  final int priority,
                                  final String title,
                                  final ImageResource logo,
                                  final String jdbcClassName,
                                  final Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages) {
        this.id = connectorId;
        this.priority = priority;
        this.title = title;
        this.image = logo;
        this.jdbcClassName = jdbcClassName;
        this.wizardPages = wizardPages;
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
