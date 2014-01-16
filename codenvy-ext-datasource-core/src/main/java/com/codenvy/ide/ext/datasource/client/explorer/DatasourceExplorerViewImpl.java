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

import com.codenvy.ide.Resources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTODataAdapter.EntityTreeNode;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ui.tree.Tree;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.input.SignalEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import elemental.html.DragEvent;

@Singleton
public class DatasourceExplorerViewImpl extends
                                       BaseView<DatasourceExplorerView.ActionDelegate> implements
                                                                                      DatasourceExplorerView {
    protected Tree<EntityTreeNode> tree;
    protected DockLayoutPanel      dsContainer;
    protected ListBox              datasourceListBox;
    protected PreferencesManager   preferencesManager;
    protected DtoFactory           dtoFactory;
    protected Button               exploreButton;
    protected DockLayoutPanel      topDsContainer;

    private SimplePanel            propertiesContainer;

    @Inject
    public DatasourceExplorerViewImpl(final Resources resources,
                                      final PreferencesManager preferenceManager,
                                      final DtoFactory dtoFactory) {
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

        this.propertiesContainer = new SimplePanel();
        dsContainer.addSouth(this.propertiesContainer, 200);

        dsContainer.add(tree.asWidget());
        container.add(dsContainer);
    }

    @Override
    public void setDatasourceList(final Collection<String> datasourceIds) {
        datasourceListBox.clear();
        if (datasourceIds != null) {
            for (String dsIds : datasourceIds) {
                datasourceListBox.addItem(dsIds);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setItems(DatabaseMetadataEntityDTO database) {
        tree.getModel().setRoot(new EntityTreeNode(null, database));
        tree.renderTree(1);
    }

    @Override
    public AcceptsOneWidget getPropertiesDisplayContainer() {
        return this.propertiesContainer;
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(final ActionDelegate delegate) {
        this.delegate = delegate;
        tree.setTreeEventHandler(new Tree.Listener<EntityTreeNode>() {
            @Override
            public void onNodeAction(TreeNodeElement<EntityTreeNode> node) {
                delegate.onDatabaseMetadataEntityAction(node.getData().getData());
            }

            @Override
            public void onNodeClosed(TreeNodeElement<EntityTreeNode> node) {
            }

            @Override
            public void onNodeContextMenu(int mouseX, int mouseY, TreeNodeElement<EntityTreeNode> node) {
                delegate.onContextMenu(mouseX, mouseY);
            }

            @Override
            public void onNodeDragStart(TreeNodeElement<EntityTreeNode> node, DragEvent event) {
            }

            @Override
            public void onNodeDragDrop(TreeNodeElement<EntityTreeNode> node, DragEvent event) {
            }

            @Override
            public void onNodeExpanded(TreeNodeElement<EntityTreeNode> node) {
            }

            @Override
            public void onNodeSelected(TreeNodeElement<EntityTreeNode> node, SignalEvent event) {
                delegate.onDatabaseMetadataEntitySelected(node.getData().getData());
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
