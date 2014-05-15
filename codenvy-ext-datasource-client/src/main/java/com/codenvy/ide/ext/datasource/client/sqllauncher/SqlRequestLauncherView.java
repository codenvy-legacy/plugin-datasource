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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import java.util.Collection;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Widget;

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

    /** Replaces the items in the datasources list. */
    void setDatasourceList(Collection<String> datasourceIds);

    /** Add a request result block in the result zone. */
    void appendResult(Widget widget);

    /** Removes the contents of the result zone. */
    void clearResultZone();

    /** Sets the value in the execution mode input. */
    void setExecutionMode(MultipleRequestExecutionMode executionMode);

    /** Required for delegating functions in view. */
    public interface ActionDelegate {

        /** Reaction to the change of the datasource input value. */
        void datasourceChanged(String newDataSourceId);

        /** Reaction to the change of the result limit input value. */
        void resultLimitChanged(String newResultLimitString);

        /** Reaction to the execution click. */
        void executeRequested();

        /** Reaction to the change of the execution mode input value. */
        void executionModeChanged(MultipleRequestExecutionMode oneByOne);

        /** Removes the contents of the result zone. */
        void clearResults();
    }
}
