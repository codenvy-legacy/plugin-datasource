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

import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.api.ui.workspace.AbstractPartPresenter;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class SqlRequestLauncherPresenter extends AbstractPartPresenter implements SqlRequestLauncherView.ActionDelegate {

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

    @Inject
    public SqlRequestLauncherPresenter(final SqlRequestLauncherView view,
                                       final SqlRequestLauncherConstants constants,
                                       final PreferencesManager preferencesManager) {
        this.view = view;
        this.view.setDelegate(this);
        this.constants = constants;

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

    }
}
