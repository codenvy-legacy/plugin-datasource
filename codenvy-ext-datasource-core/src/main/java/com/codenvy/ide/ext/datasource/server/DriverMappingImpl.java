package com.codenvy.ide.ext.datasource.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.ide.ext.datasource.server.drivers.DriverAffinityManager;
import com.codenvy.ide.ext.datasource.server.drivers.DriverRegistry;
import com.codenvy.ide.ext.datasource.server.drivers.JdbcDriver;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.google.inject.Inject;

public class DriverMappingImpl implements DriverMapping {

    private static final Logger   LOG = LoggerFactory.getLogger(DriverMappingImpl.class);
    private DriverRegistry         driverManager;
    private DriverAffinityManager driverAffinityManager;


    @Inject
    public DriverMappingImpl(final DriverRegistry driverManager,
                             final DriverAffinityManager driverAffinityManager) {
        this.driverManager = driverManager;
        this.driverAffinityManager = driverAffinityManager;
    }


    @Override
    public JdbcDriver getFirstCompatibleDriver(final DatabaseType dbtype) {
        List<JdbcDriver> drivers = this.driverAffinityManager.getDatabasePreferredDriver(dbtype);
        for (JdbcDriver driver : drivers) {
            if (driver.isActive()) {
                return driver;
            }
        }
        return null;
    }
}
