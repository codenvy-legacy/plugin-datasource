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
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.base.BaseActionDelegate;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;

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
    }
}
