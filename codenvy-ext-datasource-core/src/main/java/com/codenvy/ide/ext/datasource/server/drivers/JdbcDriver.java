package com.codenvy.ide.ext.datasource.server.drivers;

import java.util.Set;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public abstract class JdbcDriver {
    private final String name;
    private final String jdbcClassname;

    public JdbcDriver(final String name, final String jdbcClassName) {
        this.name = name;
        this.jdbcClassname = jdbcClassName;
    }

    public abstract String getJdbcURIForDatasource(DatabaseConfigurationDTO configuration);

    public abstract Set<DatabaseType> getSupportedDatabaseTypes();

    public abstract boolean supportsDatabaseType(DatabaseType type);

    public boolean supportsDatabase(final DatabaseConfigurationDTO configuration) {
        return supportsDatabaseType(configuration.getDatabaseType());
    }

    public String getName() {
        return name;
    }

    public String getJdbcClassname() {
        return jdbcClassname;
    }

    public abstract boolean isActive();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((jdbcClassname == null) ? 0 : jdbcClassname.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JdbcDriver other = (JdbcDriver)obj;
        if (jdbcClassname == null) {
            if (other.jdbcClassname != null) {
                return false;
            }
        } else if (!jdbcClassname.equals(other.jdbcClassname)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "JdbcDriver [name=" + name + ", jdbcClassname=" + jdbcClassname + "]";
    }
}
