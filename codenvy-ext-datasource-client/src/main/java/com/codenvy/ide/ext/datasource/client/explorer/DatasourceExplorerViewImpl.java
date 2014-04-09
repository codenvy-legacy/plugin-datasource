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

import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTODataAdapter.EntityTreeNode;
import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTORenderer.Resources;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ui.tree.Tree;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.input.SignalEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import elemental.html.DragEvent;

/**
 * The datasource explorer view component.
 */
@Singleton
public class DatasourceExplorerViewImpl extends
                                       BaseView<DatasourceExplorerView.ActionDelegate> implements
                                                                                      DatasourceExplorerView {

    @UiField
    protected Panel                 mainContainer;

    /** The explorer tree. */
    @UiField(provided = true)
    protected Tree<EntityTreeNode>  tree;

    /** the dropdown to select the datasource to explroe. */
    @UiField
    protected ListBox               datasourceListBox;

    /** The panel where we show the selected element properties. */
    @UiField
    protected SimplePanel           propertiesContainer;

    /** the button to refresh the datasource metadata. */
    @UiField
    protected PushButton            refreshButton;

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

        uiBinder.createAndBindUi(this);

        this.refreshButton.getUpFace().setImage(new Image(clientResource.getRefreshIcon()));
        this.refreshButton.setTitle(constants.exploreButtonTooltip());

        container.add(mainContainer);
    }

    @Override
    public void setDatasourceList(final Collection<String> datasourceIds) {
        if (datasourceIds.isEmpty()) {
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
        if (datasourceIds != null) {
            if (datasourceIds.size() > 1) {
                // add an empty item
                this.datasourceListBox.addItem("", "");
            }
            for (String datasourceId : datasourceIds) {
                this.datasourceListBox.addItem(datasourceId);
            }
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
    public void onClick(final ClickEvent event) {
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

    /** The binder interface for the datasource explorer view component. */
    interface DatasourceExplorerViewUiBinder extends UiBinder<Widget, DatasourceExplorerViewImpl> {
    }
}
