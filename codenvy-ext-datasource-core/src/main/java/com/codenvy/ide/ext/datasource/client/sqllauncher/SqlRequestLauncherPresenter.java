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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import java.util.Collection;

import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.api.ui.workspace.AbstractPartPresenter;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedEvent;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedHandler;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorProvider;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.resources.marshal.StringUnmarshaller;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SqlRequestLauncherPresenter extends AbstractPartPresenter implements
                                                                      SqlRequestLauncherView.ActionDelegate,
                                                                      DatasourceCreatedHandler {

    /** Preference property name for default result limit. */
    private static final String               PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT = "SqlEditor_default_request_limit";

    /** Default value for request limit (when no pref is set). */
    private static final int                  DEFAULT_REQUEST_LIMIT                = 20;
    /** The matching view. */
    private final SqlRequestLauncherView      view;
    /** The i18n-able constants. */
    private final SqlRequestLauncherConstants constants;

    private String                            selectedDatasourceId                 = null;
    private int                               resultLimit                          = DEFAULT_REQUEST_LIMIT;

    private EditorPartPresenter               editor;

    private DatasourceClientService           datasourceClientService;
    private NotificationManager               notificationManager;
    private DatasourceManager                 datasourceManager;

    private TextArea                          editorArea;

    @Inject
    public SqlRequestLauncherPresenter(final SqlRequestLauncherView view,
                                       final SqlRequestLauncherConstants constants,
                                       final PreferencesManager preferencesManager,
                                       final SqlEditorProvider sqlEditorProvider,
                                       final DatasourceClientService service,
                                       final NotificationManager notificationManager,
                                       final DatasourceManager datasourceManager,
                                       final EventBus eventBus) {
        this.view = view;
        this.view.setDelegate(this);
        this.constants = constants;

        this.editor = sqlEditorProvider.getEditor();
        this.datasourceClientService = service;
        this.notificationManager = notificationManager;
        this.datasourceManager = datasourceManager;

        final String prefRequestLimit = preferencesManager.getValue(PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT);

        if (prefRequestLimit != null) {
            try {
                int prefValue = Integer.valueOf(prefRequestLimit);
                if (prefValue > 0) {
                    this.resultLimit = prefValue;
                } else {
                    Log.warn(SqlRequestLauncherPresenter.class, "negative value stored in preference "
                                                                + PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT);
                }
            } catch (final NumberFormatException e) {
                StringBuilder sb = new StringBuilder("Preference stored in ")
                                                                             .append(PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT)
                                                                             .append(" is not an integer (")
                                                                             .append(resultLimit)
                                                                             .append(").");
                Log.warn(SqlRequestLauncherPresenter.class, sb.toString());
            }
        }

        // push the request limit value to the view
        this.view.setResultLimit(this.resultLimit);

        // register for datasource creation events
        eventBus.addHandler(DatasourceCreatedEvent.getType(), this);

        // temporary
        editorArea = new TextArea();
        editorArea.getElement().getStyle().clearBackgroundColor();
    }

    private void setupDatasourceComponent() {
        Collection<String> datasourceIds = this.datasourceManager.getNames();
        this.view.setDatasourceList(datasourceIds);
    }

    @Override
    public String getTitle() {
        return this.constants.sqlEditorWindowTitle();
    }

    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    @Override
    public String getTitleToolTip() {
        return null;
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(view);
        // editor.go(this.view.getEditorZone());
        this.view.getEditorZone().setWidget(editorArea);


        setupDatasourceComponent();
    }

    @Override
    public boolean onClose() {
        return this.editor.onClose();
    }

    @Override
    public void onOpen() {
        this.editor.onOpen();
    }

    @Override
    public void datasourceChanged(final String newDataSourceId) {
        this.selectedDatasourceId = newDataSourceId;
    }

    @Override
    public void resultLimitChanged(final int newResultLimit) {
        if (newResultLimit > 0) {
            this.resultLimit = newResultLimit;
        } else {
            this.view.setResultLimit(this.resultLimit);
        }
    }

    @Override
    public void executeRequested(final String request) {
        if (this.selectedDatasourceId == null) {
            Window.alert("No datasource selected");
        }
        DatabaseConfigurationDTO databaseConf = this.datasourceManager.getByName(this.selectedDatasourceId);
        String rawSql = this.editor.getEditorInput().getFile().getContent();
        if (rawSql != null) {
            rawSql = rawSql.trim();
            if (!"".equals(rawSql)) {
                try {
                    datasourceClientService.executeSqlRequest(databaseConf,
                                                              this.resultLimit,
                                                              rawSql,
                                                              new AsyncRequestCallback<String>(new StringUnmarshaller()) {

                                                                  @Override
                                                                  protected void onSuccess(final String result) {
                                                                      // TODO Auto-generated method stub

                                                                  }

                                                                  @Override
                                                                  protected void onFailure(final Throwable exception) {
                                                                      // TODO Auto-generated method stub

                                                                  }
                                                              });
                } catch (final RequestException e) {
                    Log.error(SqlRequestLauncherPresenter.class,
                              "Exception on SQL request execution : " + e.getMessage());
                    notificationManager.showNotification(new Notification("Failed execution of SQL request",
                                                                          Type.ERROR));
                }
            }
        }
        Window.alert("No SQL request");
    }

    @Override
    public void onDatasourceCreated(DatasourceCreatedEvent event) {
        this.setupDatasourceComponent();
    }

    @Override
    public void minimize() {
        // nothing to do
    }
}
