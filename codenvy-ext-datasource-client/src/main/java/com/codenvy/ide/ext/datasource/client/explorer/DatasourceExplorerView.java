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
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * Interface of datasource tree view.
 */
public interface DatasourceExplorerView extends
                                       View<DatasourceExplorerView.ActionDelegate> {

    /**
     * Sets items into tree.
     * 
     * @param resource The root resource item
     */
    void setItems(@NotNull DatabaseMetadataEntityDTO databaseMetadataEntyDTO);

    /**
     * Sets title of part.
     * 
     * @param title title of part
     */
    void setTitle(@NotNull String title);

    /**
     * Fills the datasource selection component.
     * 
     * @param datasourceIds the ids (keys) of the datasources
     */
    void setDatasourceList(Collection<String> datasourceIds);

    /**
     * Returns the component used for datasource properties display.
     * 
     * @return the properties display component
     */
    AcceptsOneWidget getPropertiesDisplayContainer();

    /**
     * The action delegate for this view.
     */
    public interface ActionDelegate extends BaseActionDelegate {
        /**
         * Performs any actions in response to node selection.
         * 
         * @param resource node
         */
        void onDatabaseMetadataEntitySelected(@NotNull DatabaseMetadataEntityDTO dbMetadataEntity);

        /**
         * Performs any actions in response to some node action.
         * 
         * @param resource node
         */
        void onDatabaseMetadataEntityAction(@NotNull DatabaseMetadataEntityDTO dbMetadataEntity);

        /**
         * Performs any actions appropriate in response to the user having clicked right button on mouse.
         * 
         * @param mouseX the mouse x-position within the browser window's client area.
         * @param mouseY the mouse y-position within the browser window's client area.
         */
        void onContextMenu(int mouseX, int mouseY);

        void onClickExploreButton(String datasourceId);

        void onSelectedDatasourceChanged(String datasourceId);
    }
}
