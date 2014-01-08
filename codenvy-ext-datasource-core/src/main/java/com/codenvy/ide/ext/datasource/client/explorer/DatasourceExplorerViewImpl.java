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

import com.codenvy.ide.Resources;
import com.codenvy.ide.api.parts.base.BaseView;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ui.tree.Tree;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.input.SignalEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import elemental.html.DragEvent;

@Singleton
public class DatasourceExplorerViewImpl extends
                                       BaseView<DatasourceExplorerView.ActionDelegate> implements
                                                                                      DatasourceExplorerView {
    protected Tree<DatabaseMetadataEntityDTO> tree;

    @Inject
    public DatasourceExplorerViewImpl(Resources resources) {
        super(resources);

        tree = Tree.create(resources,
                           new DatabaseMetadataEntityDTODataAdapter(),
                           new DatabaseMetadataEntityDTORenderer(resources));
        container.add(tree.asWidget());
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
