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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseType;
import com.codenvy.ide.ext.datasource.shared.NuoDBBrokerDTO;
import com.codenvy.ide.ext.datasource.shared.NuoDBDatasourceDefinitionDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


/**
 * This connector page is using JTDS JDBC Driver to connect to MS SQLserver.
 */
public class NuoDBDatasourceConnectorPage extends AbstractNewDatasourceConnectorPage implements
                                                                                    NuoDBDatasourceConnectorView.NuoActionDelegate {

    public static final String                  NUODB_DB_ID               = "nuodb";
    private static final int                    DEFAULT_PORT_NUODB_BROKER = 48004;

    private final ListDataProvider<NuoDBBroker> brokersProvider           = new ListDataProvider<>();
    private final DtoFactory                    dtoFactory;

    @Inject
    public NuoDBDatasourceConnectorPage(final NuoDBDatasourceConnectorView view,
                                        final NotificationManager notificationManager,
                                        final DtoFactory dtoFactory,
                                        final DatasourceManager datasourceManager,
                                        final EventBus eventBus,
                                        final DatasourceClientService service,
                                        final DatasourceUiResources resources,
                                        final NewDatasourceWizardMessages messages) {

        super(view, "NuoDB", resources.getNuoDBLogo(), NUODB_DB_ID, datasourceManager, eventBus, service,
              notificationManager, dtoFactory, messages);
        view.setNuoDelegate(this);

        this.dtoFactory = dtoFactory;

        final NuoDBBroker firstBroker = createNewBroker(0);
        this.brokersProvider.getList().add(firstBroker);

        view.bindBrokerList(this.brokersProvider);
    }

    private NuoDBBroker createNewBroker(final int id) {
        final NuoDBBroker newBroker = NuoDBBroker.create(id);
        newBroker.setHost("localhost");
        newBroker.setPort(DEFAULT_PORT_NUODB_BROKER);
        return newBroker;
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(getView());
    }

    public NuoDBDatasourceConnectorView getView() {
        return (NuoDBDatasourceConnectorView)super.getView();
    }

    /**
     * Returns the currently configured database.
     * 
     * @return the database
     */
    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);

        final List<NuoDBBrokerDTO> brokersConf = new ArrayList<>();
        for (final NuoDBBroker broker : this.brokersProvider.getList()) {
            if (broker.getHost() != null && !"".equals(broker.getHost()) && broker.getPort() != null) {
                final NuoDBBrokerDTO brokerDto = this.dtoFactory.createDto(NuoDBBrokerDTO.class)
                                                                .withHostName(broker.getHost())
                                                                .withPort(broker.getPort());
                brokersConf.add(brokerDto);
            }
        }

        NuoDBDatasourceDefinitionDTO result = dtoFactory.createDto(NuoDBDatasourceDefinitionDTO.class)
                                                        .withDatabaseName(getView().getDatabaseName())
                                                        .withDatabaseType(getDatabaseType())
                                                        .withDatasourceId(datasourceId)
                                                        .withUsername(getView().getUsername())
                                                        .withPassword(getView().getPassword())
                                                        .withBrokers(brokersConf);


        return result;
    }

    public Integer getDefaultPort() {
        return DEFAULT_PORT_NUODB_BROKER;
    }

    public DatabaseType getDatabaseType() {
        return DatabaseType.NUODB;
    }

    @Override
    public void onAddBroker() {
        Log.info(NuoDBDatasourceConnectorPage.class, "Adding a broker.");
        int brokerCount = this.brokersProvider.getList().size();
        final NuoDBBroker newBroker = createNewBroker(brokerCount);
        // insert the new row ; the display should be updated automatically
        this.brokersProvider.getList().add(newBroker);
    }


    @Override
    public void onDeleteBrokers() {
        Log.info(NuoDBDatasourceConnectorPage.class, "Deleting selected brokers.");
        final Set<NuoDBBroker> selection = getView().getBrokerSelection();
        // remove selected items from the list provider
        // the list wrapper should update the view by itself
        this.brokersProvider.getList().removeAll(selection);
    }
}
