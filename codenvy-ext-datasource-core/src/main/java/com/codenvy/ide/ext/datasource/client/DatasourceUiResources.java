package com.codenvy.ide.ext.datasource.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Client bundle interface for datasource plugin resources.
 */
public interface DatasourceUiResources extends ClientBundle {

    /**
     * The PostgreSQL logo.
     * 
     * @return an image resource for the PostGreSQL logo.
     */
    @Source("PostgreSQL.png")
    ImageResource getPostgreSqlLogo();

    @Source("mySQL.png")
    ImageResource getMySqlLogo();

    @Source("sqlServer.png")
    ImageResource getSqlServerLogo();

    @Source("oracle.png")
    ImageResource getOracleLogo();
    
    @Source("refresh.png")
    ImageResource getRefreshIcon();
}
