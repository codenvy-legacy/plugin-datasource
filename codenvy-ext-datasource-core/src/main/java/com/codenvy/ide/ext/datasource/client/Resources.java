package com.codenvy.ide.ext.datasource.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Client bundle interface for datasource plugin resources.
 */
public interface Resources extends ClientBundle {

    /**
     * The PostgreSQL logo.
     * 
     * @return an image resource for the PostGreSQL logo.
     */
    @Source("PostgreSQL.png")
    ImageResource getPostgreSqlLogo();
}
