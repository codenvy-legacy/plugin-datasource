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
import java.util.Map;
import java.util.Set;

import com.codenvy.ide.Resources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ext.datasource.shared.DatasourceConfigPreferences;
import com.codenvy.ide.ui.tree.Tree;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.input.SignalEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import elemental.html.DragEvent;

@Singleton
public class DatasourceExplorerViewImpl extends
                                       BaseView<DatasourceExplorerView.ActionDelegate> implements
                                                                                      DatasourceExplorerView {
    protected Tree<DatabaseMetadataEntityDTO> tree;
    protected DockLayoutPanel                 dsContainer;
    protected ListBox                         datasourceListBox;
    protected PreferencesManager              preferencesManager;
    protected DtoFactory                      dtoFactory;
    protected Button                          exploreButton;
    protected DockLayoutPanel                 topDsContainer;


    @Inject
    public DatasourceExplorerViewImpl(Resources resources, PreferencesManager preferenceManager, DtoFactory dtoFactory) {
        super(resources);
        this.preferencesManager = preferenceManager;
        this.dtoFactory = dtoFactory;
        dsContainer = new DockLayoutPanel(Style.Unit.PX);
        topDsContainer = new DockLayoutPanel(Style.Unit.PX);
        tree = Tree.create(resources,
                           new DatabaseMetadataEntityDTODataAdapter(),
                           new DatabaseMetadataEntityDTORenderer(resources));
        datasourceListBox = new ListBox();
        exploreButton = new Button("Explore");
        exploreButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                delegate.onClickExploreButton(datasourceListBox.getValue(datasourceListBox.getSelectedIndex()));
            }
        });
        dsContainer.addNorth(topDsContainer, 25);
        topDsContainer.addWest(datasourceListBox, 120);
        topDsContainer.add(exploreButton);
        dsContainer.addSouth(new Label("Property Panel"), 200);
        dsContainer.add(tree.asWidget());
        container.add(dsContainer);
    }

    @Override
    public void refreshDatasourceList() {
        // TODO do it from a service
        String datasourcesJson = preferencesManager.getValue("datasources");

        if (datasourcesJson == null) {
            return;
        }
        DatasourceConfigPreferences datasourcesPreferences =
                                                             dtoFactory.createDtoFromJson(datasourcesJson,
                                                                                          DatasourceConfigPreferences.class);

        Map<String, DatabaseConfigurationDTO> datasourcesMap = datasourcesPreferences.getDatasources();
        Set<String> datasourcesIds = datasourcesMap.keySet();

        int i = 0;
        datasourceListBox.clear();
        for (String dsIds : datasourcesIds) {
            datasourceListBox.insertItem(dsIds, i++);
        }
    }

    @Override
    public void setDatasourceList(final Collection<String> datasourceIds) {
        datasourceListBox.clear();
        for (String dsIds : datasourceIds) {
            datasourceListBox.addItem(dsIds);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setItems(DatabaseMetadataEntityDTO database) {
        tree.getModel().setRoot(database);
        tree.renderTree(1);
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.delegate = delegate;
        tree.setTreeEventHandler(new Tree.Listener<DatabaseMetadataEntityDTO>() {
            @Override
            public void onNodeAction(TreeNodeElement<DatabaseMetadataEntityDTO> node) {
                delegate.onDatabaseMetadataEntityAction(node.getData());
            }

            @Override
            public void onNodeClosed(TreeNodeElement<DatabaseMetadataEntityDTO> node) {
            }

            @Override
            public void onNodeContextMenu(int mouseX, int mouseY, TreeNodeElement<DatabaseMetadataEntityDTO> node) {
                delegate.onContextMenu(mouseX, mouseY);
            }

            @Override
            public void onNodeDragStart(TreeNodeElement<DatabaseMetadataEntityDTO> node, DragEvent event) {
            }

            @Override
            public void onNodeDragDrop(TreeNodeElement<DatabaseMetadataEntityDTO> node, DragEvent event) {
            }

            @Override
            public void onNodeExpanded(TreeNodeElement<DatabaseMetadataEntityDTO> node) {
            }

            @Override
            public void onNodeSelected(TreeNodeElement<DatabaseMetadataEntityDTO> node, SignalEvent event) {
                delegate.onDatabaseMetadataEntitySelected(node.getData());
            }

            @Override
            public void onRootContextMenu(int mouseX, int mouseY) {
                delegate.onContextMenu(mouseX, mouseY);
            }

            @Override
            public void onRootDragDrop(DragEvent event) {
            }
        });
    }
}
