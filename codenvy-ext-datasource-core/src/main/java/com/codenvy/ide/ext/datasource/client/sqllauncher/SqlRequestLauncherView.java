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

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * Interface for the SQL editor view component.
 * 
 * @author "Mickaël Leduque"
 */
public interface SqlRequestLauncherView extends View<SqlRequestLauncherView.ActionDelegate> {

    /** Change the displayed value of the request result limit. */
    void setResultLimit(int newResultLimit);

    /** Returns the zone in which the SQL editor is to be shown. */
    AcceptsOneWidget getEditorZone();

    AcceptsOneWidget getResultZone();

    void setDatasourceList(Collection<String> datasourceIds);

    /** Required for delegating functions in view. */
    public interface ActionDelegate extends BaseActionDelegate {

        void datasourceChanged(String newDataSourceId);

        void resultLimitChanged(int newResultLimit);

        void executeRequested(String request);
    }

}
