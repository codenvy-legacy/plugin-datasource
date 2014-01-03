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

import com.codenvy.ide.collections.Array;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Provider;

/**
 * Provides DB registered connectors
 */
public interface NewDatasourceConnectorAgent {

    void register(@NotNull String id,
                  @NotNull String title,
                  @Nullable ImageResource image,
                  @NotNull Array<Provider< ? extends AbstractNewDatasourceConnectorPage>> wizardPages);

    Array<NewDatasourceConnector> getConnectors();

}
