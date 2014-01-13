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
package com.codenvy.ide.ext.datasource.client.properties;

import com.codenvy.ide.api.ui.workspace.AbstractPartPresenter;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

/**
 * The presenter part for the datasource properties display.
 * 
 * @author "Mickaël Leduque"
 */
public class DatasourcePropertiesPresenter extends AbstractPartPresenter {

    /** The view component. */
    private DatasourcePropertiesView view;

    @Inject
    public DatasourcePropertiesPresenter(final DatasourcePropertiesView view) {
        super();
        this.view = view;
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public String getTitle() {
        return "Datasource properties";
    }

    @Override
    public ImageResource getTitleImage() {
        return null;
    }

    @Override
    public String getTitleToolTip() {
        return null;
    }

    public void onDataSourceSelectionChanged(final DatabaseDTO databaseDto) {
        this.view.setDatasourceName(databaseDto.getName());
    }
}
