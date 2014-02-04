package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.editor.EditorProvider;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorProvider;
import com.google.gwt.core.shared.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SqlLauncherEditorProvider implements EditorProvider {

    private final NotificationManager   notificationManager;

    private final SqlEditorProvider     sqlEditorProvider;

    private SqlRequestLauncherConstants constants;

    private PreferencesManager          preferencesManager;

    private DatasourceManager           datasourceManager;

    private EventBus                    eventBus;

    private DatasourceClientService     service;

    private DtoFactory                  dtoFactory;


    @Inject
    public SqlLauncherEditorProvider(final NotificationManager notificationManager,
                                     final SqlEditorProvider sqlEditorProvider,
                                     final SqlRequestLauncherConstants constants,
                                     final PreferencesManager preferencesManager,
                                     final DatasourceManager datasourceManager,
                                     final EventBus eventBus,
                                     final DatasourceClientService service,
                                     final DtoFactory dtoFactory) {
        this.notificationManager = notificationManager;
        this.sqlEditorProvider = sqlEditorProvider;
        this.constants = constants;
        this.preferencesManager = preferencesManager;
        this.datasourceManager = datasourceManager;
        this.eventBus = eventBus;
        this.service = service;
        this.dtoFactory = dtoFactory;
    }

    @Override
    public EditorPartPresenter getEditor() {
        return new SqlRequestLauncherAdapter(GWT.<SqlRequestLauncherView> create(SqlRequestLauncherView.class),
                                             constants,
                                             preferencesManager,
                                             sqlEditorProvider,
                                             service,
                                             notificationManager,
                                             datasourceManager,
                                             eventBus,
                                             dtoFactory);

    }
}
