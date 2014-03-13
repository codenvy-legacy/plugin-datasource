package com.codenvy.ide.ext.datasource.server.drivers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.shared.DatabaseType;

public class DriverAffinityManagerImpl implements DriverAffinityManager {

    private static final Logger                           LOG               = LoggerFactory.getLogger(DriverAffinityManagerImpl.class);

    private final EnumMap<DatabaseType, List<JdbcDriver>> driverPreferences = new EnumMap<>(DatabaseType.class);

    @Override
    public void declareDriverAffinity(DatabaseType databaseType, JdbcDriver driver) {
        LOG.debug("Set driver affinity for database type {} : {} ", databaseType, driver);
        List<JdbcDriver> preferences = this.driverPreferences.get(databaseType);
        if (preferences == null) {
            LOG.debug("Initialize driver preference list for database type {}", databaseType);
            preferences = new ArrayList<>();
            this.driverPreferences.put(databaseType, preferences);
        }
        this.driverPreferences.get(databaseType).add(driver);
    }

    @Override
    public List<JdbcDriver> getDatabasePreferredDriver(final DatabaseType dbtype) {
        return this.driverPreferences.get(dbtype);
    }
}
