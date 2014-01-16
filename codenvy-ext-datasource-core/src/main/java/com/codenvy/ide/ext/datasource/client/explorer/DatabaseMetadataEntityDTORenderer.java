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

import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTODataAdapter.EntityTreeNode;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.tree.FileTreeNodeRenderer.Css;
import com.codenvy.ide.tree.FileTreeNodeRenderer.Resources;
import com.codenvy.ide.ui.tree.NodeRenderer;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.dom.Elements;

import elemental.html.DivElement;
import elemental.html.Element;
import elemental.html.SpanElement;

public class DatabaseMetadataEntityDTORenderer implements NodeRenderer<EntityTreeNode> {

    public DatabaseMetadataEntityDTORenderer(Resources resources) {
        this.res = resources;
        this.css = res.workspaceNavigationFileTreeNodeRendererCss();
    }

    private final Css       css;

    private final Resources res;

    /**
     * Renders the given information as a node.
     * 
     * @param mouseDownListener an optional listener to be attached to the anchor. If not given, the label will not be an anchor.
     */
    public static SpanElement renderNodeContents(Css css, String name,
                                                 boolean isFile, boolean renderIcon) {

        SpanElement root = Elements.createSpanElement(css.root());
        if (renderIcon) {
            DivElement icon = Elements.createDivElement(css.icon());
            icon.addClassName(isFile ? css.file() : css.folder());
            root.appendChild(icon);
        }

        final Element label;
        label = Elements.createSpanElement(css.label());

        label.setTextContent(name);

        root.appendChild(label);

        return root;
    }

    @Override
    public Element getNodeKeyTextContainer(SpanElement treeNodeLabel) {
        return (Element)treeNodeLabel.getChildNodes().item(1);
    }

    @Override
    public SpanElement renderNodeContents(EntityTreeNode data) {
        return renderNodeContents(css, data.getData().getName(),
                                  (data instanceof ColumnDTO), true);
    }

    @Override
    public void updateNodeContents(
                                   TreeNodeElement<EntityTreeNode> treeNode) {
    }

}
