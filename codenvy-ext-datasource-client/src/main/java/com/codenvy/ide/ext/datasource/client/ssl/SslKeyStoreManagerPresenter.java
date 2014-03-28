/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.ssl;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.ui.preferences.AbstractPreferencesPagePresenter;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.commons.exception.ExceptionThrownEvent;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslKeyDialogPresenter;
import com.codenvy.ide.ext.datasource.client.ssl.upload.UploadSslTrustCertDialogPresenter;
import com.codenvy.ide.ext.datasource.shared.ssl.SslKeyStoreEntry;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.codenvy.ide.ui.loader.Loader;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

@Singleton
public class SslKeyStoreManagerPresenter extends AbstractPreferencesPagePresenter implements SslKeyStoreManagerView.ActionDelegate {
    private final DtoUnmarshallerFactory       dtoUnmarshallerFactory;
    private SslKeyStoreManagerView             view;
    private SslKeyStoreClientService           service;
    private SslMessages                        constant;
    private EventBus                           eventBus;
    private Loader                             loader;
    private UploadSslKeyDialogPresenter        uploadSshKeyPresenter;
    private UploadSslTrustCertDialogPresenter uploadSshServerCertPresenter;
    private NotificationManager                notificationManager;

    @Inject
    public SslKeyStoreManagerPresenter(SslKeyStoreManagerView view,
                                       SslKeyStoreClientService service,
                                       SslResources resources,
                                       SslMessages constant,
                                       EventBus eventBus,
                                       Loader loader,
                                       UploadSslKeyDialogPresenter uploadSshKeyPresenter,
                                       UploadSslTrustCertDialogPresenter uploadSshServerCertPresenter,
                                       NotificationManager notificationManager,
                                       DtoUnmarshallerFactory dtoUnmarshallerFactory) {
        super(constant.sslManagerTitle(), resources.sshKeyManager());

        this.view = view;
        this.uploadSshServerCertPresenter = uploadSshServerCertPresenter;
        this.dtoUnmarshallerFactory = dtoUnmarshallerFactory;
        this.view.setDelegate(this);
        this.service = service;
        this.constant = constant;
        this.eventBus = eventBus;
        this.loader = loader;
        this.uploadSshKeyPresenter = uploadSshKeyPresenter;
        this.notificationManager = notificationManager;
    }

    /** {@inheritDoc} */
    @Override
    public void onClientKeyDeleteClicked(@NotNull SslKeyStoreEntry key) {
        boolean needToDelete = Window.confirm(constant.deleteSslKeyQuestion(key.getAlias()));
        if (needToDelete) {
            service.deleteClientKey(key, new AsyncRequestCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    loader.hide();
                    refreshClientKeys();
                }

                @Override
                public void onFailure(Throwable exception) {
                    loader.hide();
                    Notification notification = new Notification(exception.getMessage(), Type.ERROR);
                    notificationManager.showNotification(notification);
                    eventBus.fireEvent(new ExceptionThrownEvent(exception));
                }
            });
        }
    }

    protected void refreshClientKeys() {
        service.getAllClientKeys(
               new AsyncRequestCallback<Array<SslKeyStoreEntry>>(dtoUnmarshallerFactory.newArrayUnmarshaller(SslKeyStoreEntry.class)) {
                   @Override
                   public void onSuccess(Array<SslKeyStoreEntry> result) {
                       loader.hide();
                       view.setClientKeys(result);
                   }

                   @Override
                   public void onFailure(Throwable exception) {
                       loader.hide();
                       Notification notification = new Notification(exception.getMessage(), Notification.Type.ERROR);
                       notificationManager.showNotification(notification);
                       eventBus.fireEvent(new ExceptionThrownEvent(exception));
                   }
               });
    }

    /** {@inheritDoc} */
    @Override
    public void onClientKeyUploadClicked() {
        uploadSshKeyPresenter.showDialog(new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                refreshClientKeys();
            }

            @Override
            public void onFailure(Throwable caught) {
                Log.error(SslKeyStoreManagerPresenter.class, "Failed showing dialog", caught);
            }
        });
    }

    @Override
    public void onServerCertDeleteClicked(SslKeyStoreEntry key) {
        boolean needToDelete = Window.confirm(constant.deleteSslKeyQuestion(key.getAlias()));
        if (needToDelete) {
            service.deleteServerCert(key, new AsyncRequestCallback<Void>() {
                @Override
                public void onSuccess(Void result) {
                    loader.hide();
                    refreshServerCerts();
                }

                @Override
                public void onFailure(Throwable exception) {
                    loader.hide();
                    Notification notification = new Notification(exception.getMessage(), Type.ERROR);
                    notificationManager.showNotification(notification);
                    eventBus.fireEvent(new ExceptionThrownEvent(exception));
                }
            });
        }
    }

    protected void refreshServerCerts() {
        service.getAllServerCerts(
               new AsyncRequestCallback<Array<SslKeyStoreEntry>>(dtoUnmarshallerFactory.newArrayUnmarshaller(SslKeyStoreEntry.class)) {
                   @Override
                   public void onSuccess(Array<SslKeyStoreEntry> result) {
                       loader.hide();
                       view.setServerCerts(result);
                   }

                   @Override
                   public void onFailure(Throwable exception) {
                       loader.hide();
                       Notification notification = new Notification(exception.getMessage(), Notification.Type.ERROR);
                       notificationManager.showNotification(notification);
                       eventBus.fireEvent(new ExceptionThrownEvent(exception));
                   }
               });
    }

    @Override
    public void onServerCertUploadClicked() {
        uploadSshServerCertPresenter.showDialog(new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                refreshServerCerts();
            }

            @Override
            public void onFailure(Throwable caught) {
                Log.error(SslKeyStoreManagerPresenter.class, "Failed showing dialog", caught);
            }
        });
    }

    @Override
    public void doApply() {
        // do nothing
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public void go(AcceptsOneWidget container) {
        refreshClientKeys();
        refreshServerCerts();
        container.setWidget(view);
    }

}
