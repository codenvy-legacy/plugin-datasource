/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client.editdatasource;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Status;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.common.interaction.ConfirmCallback;
import com.codenvy.ide.ext.datasource.client.common.interaction.DialogFactory;
import com.codenvy.ide.ext.datasource.client.common.interaction.confirm.ConfirmWindow;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindow;
import com.codenvy.ide.ext.datasource.client.editdatasource.celllist.DatasourceKeyProvider;
import com.codenvy.ide.ext.datasource.client.editdatasource.wizard.EditDatasourceLauncher;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeEvent;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeHandler;
import com.codenvy.ide.ext.datasource.client.newdatasource.NewDatasourceAction;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * The presenter for the datasource edit/delete datasources dialog.
 * 
 * @author "Mickaël Leduque"
 */
public class EditDatasourcesPresenter implements EditDatasourcesView.ActionDelegate, DatasourceListChangeHandler {

    /** The view component. */
    private final EditDatasourcesView                           view;

    /** The component that stores datasources. */
    private final DatasourceManager                             datasourceManager;

    /** The datasource list model component. */
    private final ListDataProvider<DatabaseConfigurationDTO>    dataProvider = new ListDataProvider<>();
    /** The selection model for the datasource list widget. */
    private final MultiSelectionModel<DatabaseConfigurationDTO> selectionModel;

    /** The i18n messages instance. */
    private final EditDatasourceMessages                        messages;

    private final NotificationManager                           notificationManager;

    /** the event bus, used to send event "datasources list modified". */
    private final EventBus                                      eventBus;

    /** A factory that will provide modification wizards. */
    private final EditDatasourceLauncher                        editDatasourceLauncher;

    /** A reference to remove handler from eventbus. */
    private HandlerRegistration                                 handlerRegistration;

    /** The action object to create new datasources. */
    private final NewDatasourceAction                           newDatasourceAction;

    /** Factory for confirmation and message windows. */
    private final DialogFactory                                 dialogFactory;

    @Inject
    public EditDatasourcesPresenter(final @NotNull EditDatasourcesView view,
                                    final @NotNull DatasourceManager datasourceManager,
                                    final @NotNull @Named(DatasourceKeyProvider.NAME) DatasourceKeyProvider keyProvider,
                                    final @NotNull EditDatasourceMessages messages,
                                    final @NotNull NotificationManager notificationManager,
                                    final @NotNull EventBus eventBus,
                                    final @NotNull EditDatasourceLauncher editDatasourceLauncher,
                                    final @NotNull NewDatasourceAction newDatasourceAction,
                                    final @NotNull DialogFactory dialogFactory) {
        this.view = view;
        this.datasourceManager = datasourceManager;
        this.messages = messages;
        this.notificationManager = notificationManager;
        this.eventBus = eventBus;
        this.editDatasourceLauncher = editDatasourceLauncher;
        this.view.bindDatasourceModel(dataProvider);
        this.view.setDelegate(this);
        this.selectionModel = new MultiSelectionModel<>(keyProvider);
        this.view.bindSelectionModel(this.selectionModel);
        this.newDatasourceAction = newDatasourceAction;
        this.dialogFactory = dialogFactory;
    }

    /** Show dialog. */
    public void showDialog() {
        setupDatasourceList();
        this.handlerRegistration = this.eventBus.addHandler(DatasourceListChangeEvent.getType(), this);
        this.view.showDialog();
    }

    @Override
    public void closeDialog() {
        this.view.closeDialog();
        this.handlerRegistration.removeHandler();
        this.handlerRegistration = null;
    }

    /** Sets the content of the datasource widget. */
    private void setupDatasourceList() {
        this.dataProvider.getList().clear();
        for (DatabaseConfigurationDTO datasource : this.datasourceManager) {
            this.dataProvider.getList().add(datasource);
        }
        this.dataProvider.refresh();

        // selection must be redone
        final Set<DatabaseConfigurationDTO> oldSelection = this.selectionModel.getSelectedSet();
        fixSelection(oldSelection);
    }

    @Override
    public void deleteSelectedDatasources() {
        final Set<DatabaseConfigurationDTO> selection = this.selectionModel.getSelectedSet();
        if (selection.isEmpty()) {
            final String warnMessage = this.messages.deleteNoSelectionMessage();
            final String warnTitle = this.messages.editOrDeleteNoSelectionTitle();
            final MessageWindow messageWindow = this.dialogFactory.createMessageWindow(warnTitle, warnMessage, null);
            messageWindow.inform();
            return;
        }
        final String confirmMessage = messages.confirmDeleteDatasources(selection.size());
        final String confirmTitle = messages.confirmDeleteDatasourcesTitle();
        final ConfirmCallback callback = new ConfirmCallback() {
            @Override
            public void accepted() {
                doDeleteSelection(selection);
            }
        };

        // no cancel callback
        final ConfirmWindow confirmWindow = this.dialogFactory.createConfirmWindow(confirmTitle, confirmMessage,
                                                                                   callback, null);
        confirmWindow.confirm();

    }

    private void doDeleteSelection(final Set<DatabaseConfigurationDTO> selection) {
        for (final DatabaseConfigurationDTO datasource : selection) {
            this.dataProvider.getList().remove(datasource);
            this.datasourceManager.remove(datasource);
        }
        final Notification persistNotification = new Notification("Saving datasources definitions", Status.PROGRESS);
        this.notificationManager.showNotification(persistNotification);
        try {
            this.datasourceManager.persist(new AsyncCallback<Profile>() {

                @Override
                public void onSuccess(final Profile result) {
                    Log.info(EditDatasourcesPresenter.class, "Datasource definitions saved.");
                    persistNotification.setMessage("Datasource definitions saved");
                    persistNotification.setStatus(Notification.Status.FINISHED);

                }

                @Override
                public void onFailure(final Throwable e) {
                    Log.error(EditDatasourcesPresenter.class, "Exception when persisting datasources: " + e.getMessage());
                    GWT.log("Full exception :", e);
                    notificationManager.showNotification(new Notification("Failed to persist datasources", Type.ERROR));

                }
            });
        } catch (final Exception e) {
            Log.error(EditDatasourcesPresenter.class, "Exception when persisting datasources: " + e.getMessage());
            notificationManager.showNotification(new Notification("Failed to persist datasources", Type.ERROR));
        }

        // reset datasource model
        setupDatasourceList();
        // inform the world about the datasource list modification
        this.eventBus.fireEvent(new DatasourceListChangeEvent());
    }

    @Override
    public void editSelectedDatasource() {
        final Set<DatabaseConfigurationDTO> selection = this.selectionModel.getSelectedSet();
        if (selection.isEmpty()) {
            final String warnMessage = this.messages.editNoSelectionMessage();
            final String warnTitle = this.messages.editOrDeleteNoSelectionTitle();
            final MessageWindow messageWindow = this.dialogFactory.createMessageWindow(warnTitle, warnMessage, null);
            messageWindow.inform();
            return;
        }
        if (selection.size() > 1) {
            final String warnMessage = this.messages.editMultipleSelectionMessage();
            final String warnTitle = this.messages.editMultipleSelectionTitle();
            final MessageWindow messageWindow = this.dialogFactory.createMessageWindow(warnTitle, warnMessage, null);
            messageWindow.inform();
            return;
        }

        for (DatabaseConfigurationDTO datasource : selection) { // there is only one !
            this.editDatasourceLauncher.launch(datasource);
        }
    }

    @Override
    public void onDatasourceListChange(final DatasourceListChangeEvent event) {
        // reset datasource model
        setupDatasourceList();
    }

    /**
     * Repair the selection after the datasource list has been modified. The element with the same id as those which where selected before
     * the datasource model was changed are selected.
     * 
     * @param oldSelection the selection before model change
     */
    private void fixSelection(final Set<DatabaseConfigurationDTO> oldSelection) {
        this.selectionModel.clear();

        final List<DatabaseConfigurationDTO> items = this.dataProvider.getList();
        for (final DatabaseConfigurationDTO oldItem : oldSelection) { // if no selection, no list traversal
            for (DatabaseConfigurationDTO item : items) {
                if (this.selectionModel.getKey(item).equals(this.selectionModel.getKey(oldItem))) {
                    this.selectionModel.setSelected(item, true);
                }
            }
        }
    }

    @Override
    public void createDatasource() {
        this.newDatasourceAction.actionPerformed();
    }
}
