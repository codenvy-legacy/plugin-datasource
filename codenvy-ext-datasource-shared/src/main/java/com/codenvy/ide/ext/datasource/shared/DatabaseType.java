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
package com.codenvy.ide.ext.datasource.shared;

public enum DatabaseType {

    POSTGRES("postgres", 5432, "postgres"),

    MYSQL("mysql", 3306, "root"),

    ORACLE("oracle", 1521, "SYSTEM"),

    JTDS("sqlserver", 1433, "sa"),

    NUODB("nuodb", 48004, "dba"),

    GOOGLECLOUDSQL("googleCloudSql", 3306, "root");

    private String connectorId;
    private int    defaultPort;
    private String defaultUsername;

    private DatabaseType(String connectorId, int defaultPort, String defaultUsername) {
        this.connectorId = connectorId;
        this.defaultPort = defaultPort;
        this.defaultUsername = defaultUsername;
    }

    public String getConnectorId() {
        return connectorId;
    }


    public int getDefaultPort() {
        return defaultPort;
    }

    public String getDefaultUsername() {
        return defaultUsername;
    }


}
