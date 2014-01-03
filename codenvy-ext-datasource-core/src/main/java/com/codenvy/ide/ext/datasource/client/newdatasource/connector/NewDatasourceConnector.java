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

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import com.google.gwt.resources.client.ImageResource;


/**
 * Aggregate information about registered DatasourceConnector.
 */
public class NewDatasourceConnector {
    private String        id;
    private String        title;
    private ImageResource image;

    public NewDatasourceConnector(@NotNull String id,
                               @NotNull String title,
                               @Nullable ImageResource image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    /** @return {@link String} PaaS id */
    public String getId() {
        return id;
    }

    /** @return the title */
    public String getTitle() {
        return title;
    }

    /** @return the image */
    public ImageResource getImage() {
        return image;
    }
}
