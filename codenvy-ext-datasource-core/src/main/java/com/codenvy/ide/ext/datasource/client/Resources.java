package com.codenvy.ide.ext.datasource.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface Resources extends ClientBundle{

    @Source("PostgreSQL.png")
    ImageResource getPostgreSqlLogo();
}
