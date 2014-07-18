/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.newdatasource.connector.nuodb;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.newdatasource.InitializableWizardPage;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizard;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceWizardMessages;
import com.codenvy.ide.ext.datasource.client.newdatasource.connector.AbstractNewDatasourceConnectorPage;
import com.codenvy.ide.ext.datasource.client.store.DatasourceManager;
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
                                                                                    NuoDBDatasourceConnectorView.NuoActionDelegate,
                                                                                    InitializableWizardPage {

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

        super(view, messages.nuodb(), resources.getNuoDBLogo(), NUODB_DB_ID, datasourceManager, eventBus, service,
              notificationManager, dtoFactory, messages);
        view.setNuoDelegate(this);

        this.dtoFactory = dtoFactory;

        final NuoDBBroker firstBroker = createNewBroker(0);
        brokersProvider.getList().add(firstBroker);

        view.bindBrokerList(brokersProvider);
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

    @Override
    public NuoDBDatasourceConnectorView getView() {
        return (NuoDBDatasourceConnectorView)super.getView();
    }

    /**
     * Returns the currently configured database.
     *
     * @return the database
     */
    @Override
    protected DatabaseConfigurationDTO getConfiguredDatabase() {
        String datasourceId = wizardContext.getData(NewDatasourceWizard.DATASOURCE_NAME);

        final List<NuoDBBrokerDTO> brokersConf = new ArrayList<>();
        for (final NuoDBBroker broker : brokersProvider.getList()) {
            if (broker.getHost() != null && !"".equals(broker.getHost()) && broker.getPort() != null) {
                final NuoDBBrokerDTO brokerDto = dtoFactory.createDto(NuoDBBrokerDTO.class)
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
                                                        .withPassword(getView().getEncryptedPassword())
                                                        .withBrokers(brokersConf);

        result.withConfigurationConnectorId(wizardContext.getData(NewDatasourceWizard.DATASOURCE_CONNECTOR).getId());

        return result;
    }

    @Override
    public Integer getDefaultPort() {
        return DEFAULT_PORT_NUODB_BROKER;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.NUODB;
    }

    @Override
    public void onAddBroker() {
        Log.debug(NuoDBDatasourceConnectorPage.class, "Adding a broker.");
        int brokerCount = brokersProvider.getList().size();
        final NuoDBBroker newBroker = createNewBroker(brokerCount);
        // insert the new row ; the display should be updated automatically
        brokersProvider.getList().add(newBroker);
    }


    @Override
    public void onDeleteBrokers() {
        Log.debug(NuoDBDatasourceConnectorPage.class, "Deleting selected brokers.");
        final Set<NuoDBBroker> selection = getView().getBrokerSelection();
        // remove selected items from the list provider
        // the list wrapper should update the view by itself
        brokersProvider.getList().removeAll(selection);
    }

    @Override
    public void initPage(final Object data) {
        // should set exactly the same fields as those read in getConfiguredDatabase except thos configured in first page
        if (!(data instanceof DatabaseConfigurationDTO)) {
            clearPage();
            return;
        }
        DatabaseConfigurationDTO initData = (DatabaseConfigurationDTO)data;
        getView().setDatabaseName(initData.getDatabaseName());
        getView().setUsername(initData.getUsername());
        getView().setEncryptedPassword(initData.getPassword(), true);

        brokersProvider.getList().clear();
        int id = 0;
        for (final NuoDBBrokerDTO brokerDto : initData.getBrokers()) {
            final NuoDBBroker broker = NuoDBBroker.create(id);
            broker.setHost(brokerDto.getHostName());
            broker.setPort(brokerDto.getPort());
            brokersProvider.getList().add(broker);
            id++;
        }
        brokersProvider.flush();
        delegate.updateControls();
    }

    @Override
    public void clearPage() {
        getView().setDatabaseName("");
        getView().setUsername("");
        getView().setPassword("");
        brokersProvider.getList().clear();
        brokersProvider.flush();
        delegate.updateControls();
    }
}
