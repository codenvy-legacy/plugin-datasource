/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Collection;

import org.vectomatic.dom.svg.ui.SVGPushButton;

import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.collections.js.JsoArray;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTODataAdapter.EntityTreeNode;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTORenderer.Resources;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ext.datasource.shared.ExploreTableType;
import com.codenvy.ide.ui.tree.Tree;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.input.SignalEvent;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import elemental.events.MouseEvent;

/**
 * The datasource explorer view component.
 */
@Singleton
public class DatasourceExplorerViewImpl extends
                                       BaseView<DatasourceExplorerView.ActionDelegate> implements
                                                                                      DatasourceExplorerView {

    /** The explorer tree. */
    @UiField(provided = true)
    protected Tree<EntityTreeNode>  tree;

    /** The dropdown to select the datasource to explore. */
    @UiField
    protected ListBox               datasourceListBox;

    /** The dropdown to select the table types to show. */
    @UiField
    protected ListBox               tableTypesListBox;

    /** The panel where we show the selected element properties. */
    @UiField
    protected SimplePanel           propertiesContainer;

    /** The button to refresh the datasource metadata. */
    @UiField
    protected SVGPushButton         refreshButton;

    /** The CSS resource. */
    @UiField(provided = true)
    protected DatasourceUiResources datasourceUiResources;

    /** The split layout panel - needed so that we can set the splitter size */
    @UiField(provided = true)
    protected SplitLayoutPanel      splitPanel;

    @Inject
    public DatasourceExplorerViewImpl(final Resources resources,
                                      final DatabaseMetadataEntityDTORenderer renderer,
                                      final DatasourceExplorerViewUiBinder uiBinder,
                                      final DatasourceExplorerConstants constants,
                                      final DatasourceUiResources clientResource) {
        super(resources);

        /* initialize provided fields */
        this.tree = Tree.create(resources, new DatabaseMetadataEntityDTODataAdapter(), renderer);
        this.datasourceUiResources = clientResource;
        this.splitPanel = new SplitLayoutPanel(4);

        container.add(uiBinder.createAndBindUi(this));

        this.refreshButton.setTitle(constants.exploreButtonTooltip());

    }

    @Override
    public void setDatasourceList(final Collection<String> datasourceIds) {
        if (datasourceIds == null || datasourceIds.isEmpty()) {
            this.datasourceListBox.clear();
            delegate.onSelectedDatasourceChanged(null);
            return;
        }

        // save the currently selected item
        int index = this.datasourceListBox.getSelectedIndex();
        String selectedValue = null;
        if (index != -1) {
            selectedValue = this.datasourceListBox.getValue(index);
        }

        this.datasourceListBox.clear();
        if (datasourceIds.size() > 1) {
            // add an empty item
            this.datasourceListBox.addItem("", "");
        }
        for (String datasourceId : datasourceIds) {
            this.datasourceListBox.addItem(datasourceId);
        }

        // restore selected value if needed
        if (index != -1) {
            for (int i = 0; i < this.datasourceListBox.getItemCount(); i++) {
                if (this.datasourceListBox.getItemText(i).equals(selectedValue)) {
                    this.datasourceListBox.setSelectedIndex(i);
                    break;
                }
            }
            delegate.onSelectedDatasourceChanged(getSelectedId());
        } else {
            if (this.datasourceListBox.getItemCount() == 1) {
                this.datasourceListBox.setSelectedIndex(0);
                delegate.onSelectedDatasourceChanged(this.datasourceListBox.getValue(0));
            }
        }
    }

    @Override
    public void setTableTypesList(final Collection<String> tableTypes) {
        this.tableTypesListBox.clear();
        for (final String tableType : tableTypes) {
            this.tableTypesListBox.addItem(tableType);
        }
    }

    @Override
    public void setTableTypes(final ExploreTableType tableType) {
        this.tableTypesListBox.setSelectedIndex(tableType.getIndex());
    }

    public String getSelectedId() {
        int index = this.datasourceListBox.getSelectedIndex();
        if (index != -1) {
            return this.datasourceListBox.getValue(index);
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setItems(final DatabaseMetadataEntityDTO database) {
        if (database == null) {
            // should probably clean up the model too but we'd need a null-safe DataAdapter and Renderer
            tree.asWidget().setVisible(false);
        } else {
            tree.asWidget().setVisible(true);
            tree.getModel().setRoot(new EntityTreeNode(null, database));
            tree.renderTree(1);
        }
    }

    @Override
    public AcceptsOneWidget getPropertiesDisplayContainer() {
        return this.propertiesContainer;
    }

    /**
     * Handler to react to clicks on the refresh button.
     * 
     * @param event the event
     */
    @UiHandler("refreshButton")
    public void onRefreshClick(final ClickEvent event) {
        delegate.onClickExploreButton(datasourceListBox.getValue(datasourceListBox.getSelectedIndex()));
    }

    /**
     * Handler to react to value change in the datasource listbox.
     * 
     * @param event the event
     */
    @UiHandler("datasourceListBox")
    public void onDatasourceListChanged(final ChangeEvent event) {
        delegate.onSelectedDatasourceChanged(datasourceListBox.getValue(datasourceListBox.getSelectedIndex()));
    }

    /**
     * Handler to react to value change in the table types listbox.
     * 
     * @param event the event
     */
    @UiHandler("tableTypesListBox")
    public void onTableTypesListChanged(final ChangeEvent event) {
        delegate.onSelectedTableTypesChanged(this.tableTypesListBox.getSelectedIndex());
    }

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
            public void onNodeDragStart(TreeNodeElement<EntityTreeNode> node, MouseEvent event) {
            }

            @Override
            public void onNodeDragDrop(TreeNodeElement<EntityTreeNode> node, MouseEvent event) {
            }

            @Override
            public void onNodeExpanded(TreeNodeElement<EntityTreeNode> node) {
            }

            @Override
            public void onNodeSelected(final TreeNodeElement<EntityTreeNode> node, final SignalEvent event) {
                // we must force single selection and check unselection
                final JsoArray<EntityTreeNode> selectedNodes = tree.getSelectionModel().getSelectedNodes();
                if (selectedNodes.isEmpty()) {
                    // this was a unselection
                    Log.info(DatasourceExplorerViewImpl.class, "Unselect tree item (CTRL+click) - send null as selected item.");
                    delegate.onDatabaseMetadataEntitySelected(null);
                } else if (selectedNodes.size() == 1) {
                    // normal selection with exactly one selected element
                    Log.info(DatasourceExplorerViewImpl.class, "Normal tree item selection.");
                    tree.getSelectionModel().clearSelections();
                    tree.getSelectionModel().selectSingleNode(node.getData());
                    delegate.onDatabaseMetadataEntitySelected(node.getData().getData());
                } else {
                    // attempt to do multiple selection with ctrl or shift
                    Log.info(DatasourceExplorerViewImpl.class,
                             "Multiple selection triggered in datasource explorer tree - keep the last one.");
                    tree.getSelectionModel().clearSelections();
                    tree.getSelectionModel().selectSingleNode(node.getData());
                    delegate.onDatabaseMetadataEntitySelected(node.getData().getData());
                }
            }

            @Override
            public void onRootContextMenu(int mouseX, int mouseY) {
                delegate.onContextMenu(mouseX, mouseY);
            }

            @Override
            public void onRootDragDrop(MouseEvent event) {
            }
        });
    }

    /** The binder interface for the datasource explorer view component. */
    interface DatasourceExplorerViewUiBinder extends UiBinder<Widget, DatasourceExplorerViewImpl> {
    }
}
