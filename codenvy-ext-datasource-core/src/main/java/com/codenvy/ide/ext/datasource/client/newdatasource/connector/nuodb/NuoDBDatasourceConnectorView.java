/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

import java.util.Set;

import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorView;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.ImplementedBy;

@ImplementedBy(NuoDBDatasourceConnectorViewImpl.class)
public interface NuoDBDatasourceConnectorView extends AbstractNewDatasourceConnectorView {

    /** Additional delegate methods. */
    public interface NuoActionDelegate {

        void onAddBroker();

        void onDeleteBrokers();
    }

    String getDatabaseName();

    void bindBrokerList(ListDataProvider<NuoDBBroker> dataProvider);

    Set<NuoDBBroker> getBrokerSelection();

    void setNuoDelegate(NuoActionDelegate delegate);
}
