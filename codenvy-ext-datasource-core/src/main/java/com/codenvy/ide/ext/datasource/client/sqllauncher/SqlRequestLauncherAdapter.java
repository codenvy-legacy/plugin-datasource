package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.common.SinglePartPresenter;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SqlRequestLauncherAdapter extends SinglePartPresenter {

    @Inject
    public SqlRequestLauncherAdapter(final SqlRequestLauncherView view,
                                    final SqlRequestLauncherConstants constants,
                                    final PreferencesManager preferencesManager,
                                    final SqlEditorProvider sqlEditorProvider,
                                    final DatasourceClientService service,
                                    final NotificationManager notificationManager,
                                    final DatasourceManager datasourceManager,
                                    final EventBus eventBus,
                                    final DtoFactory dtoFactory) {
        super(new SqlRequestLauncherPresenter(view,
                                              constants,
                                              preferencesManager,
                                              sqlEditorProvider,
                                              service,
                                              notificationManager,
                                              datasourceManager,
                                              eventBus,
                                              dtoFactory));
    }
}
