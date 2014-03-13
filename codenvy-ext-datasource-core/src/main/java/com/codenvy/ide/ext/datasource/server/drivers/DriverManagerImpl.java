package com.codenvy.ide.ext.datasource.server.drivers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class DriverManagerImpl implements DriverManager {

    private final Set<JdbcDriver> drivers                 = new HashSet<>();

    private Set<DatabaseType>     supportedDatatypesCache = null;

    @Override
    public synchronized void registerDriver(final JdbcDriver driver) {
        if (driver != null && driver.isActive()) {
            this.supportedDatatypesCache = null; // change contents, invalidate cache
            this.drivers.add(driver);
        }
    }

    @Override
    public Iterator<JdbcDriver> iterator() {
        return this.drivers.iterator();
    }

    @Override
    public Set<DatabaseType> getSupportedDatabaseTypes() {
        if (this.supportedDatatypesCache != null) {
            return this.supportedDatatypesCache;
        }
        synchronized (this) {
            this.supportedDatatypesCache = new HashSet<>();
            for (final JdbcDriver driver : this.drivers) {
                this.supportedDatatypesCache.addAll(driver.getSupportedDatabaseTypes());
            }
        }
        return this.supportedDatatypesCache;
    }
}
