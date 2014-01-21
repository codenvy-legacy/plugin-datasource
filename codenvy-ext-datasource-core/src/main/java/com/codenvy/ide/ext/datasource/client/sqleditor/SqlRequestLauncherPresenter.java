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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.codenvy.ide.api.ui.workspace.AbstractPartPresenter;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

public class SqlRequestLauncherPresenter extends AbstractPartPresenter implements SqlRequestLauncherView.ActionDelegate {

    /** The matching view. */
    private final SqlRequestLauncherView      view;
    /** The i18n-able constants. */
    private final SqlRequestLauncherConstants constants;

    @Inject
    public SqlRequestLauncherPresenter(final SqlRequestLauncherView view, final SqlRequestLauncherConstants constants) {
        this.view = view;
        this.view.setDelegate(this);
        this.constants = constants;
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
}
