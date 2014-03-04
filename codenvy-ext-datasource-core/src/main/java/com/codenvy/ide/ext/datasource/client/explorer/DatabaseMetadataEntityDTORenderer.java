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
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.ui.tree.NodeRenderer;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.dom.Elements;
import com.google.gwt.resources.client.ImageResource;

import elemental.html.DivElement;
import elemental.html.Element;
import elemental.html.SpanElement;

public class DatabaseMetadataEntityDTORenderer implements NodeRenderer<EntityTreeNode> {

    private final Css       css;
    private final Resources res;

    public static DatabaseMetadataEntityDTORenderer create(Resources res) {
        return new DatabaseMetadataEntityDTORenderer(res);
    }

    public interface Css extends com.codenvy.ide.tree.FileTreeNodeRenderer.Css {

        String schema();

        String table();

        String column();

    }

    public interface Resources extends com.codenvy.ide.Resources {
        @Source({"DatabaseMetadataEntityDTORenderer.css", "com/codenvy/ide/tree/FileTreeNodeRenderer.css",
                "com/codenvy/ide/common/constants.css", "com/codenvy/ide/api/ui/style.css"})
        DatabaseMetadataEntityDTORenderer.Css getCss();

        @Source("schema.png")
        ImageResource schema();

        @Source("table.png")
        ImageResource table();

        @Source("column.png")
        ImageResource column();
    }

    public DatabaseMetadataEntityDTORenderer(Resources resources) {
        this.res = resources;
        res.getCss().ensureInjected();
        this.css = res.getCss();
    }


    public static SpanElement renderNodeContents(Css css, String name,
                                                 String className, boolean renderIcon) {

        SpanElement root = Elements.createSpanElement(css.root());
        if (renderIcon) {
            DivElement icon = Elements.createDivElement(css.icon());
            icon.addClassName(className);
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
        String className = css.schema();

        if (data.getData() instanceof TableDTO) {
            className = css.table();
        } else if (data.getData() instanceof ColumnDTO) {
            className = css.column();
        }
        return renderNodeContents(css, data.getData().getName(),
                                  className, true);
    }

    @Override
    public void updateNodeContents(
                                   TreeNodeElement<EntityTreeNode> treeNode) {
        if (treeNode.getData().getData() instanceof TableDTO) {
            Element icon = treeNode.getNodeLabel().getFirstChildElement();
            icon.setClassName(css.icon());
            icon.addClassName(css.table());

        } else if (treeNode.getData().getData() instanceof SchemaDTO) {
            Element icon = treeNode.getNodeLabel().getFirstChildElement();
            icon.setClassName(css.icon());
            icon.addClassName(css.schema());
        }
        else if (treeNode.getData().getData() instanceof ColumnDTO) {
            Element icon = treeNode.getNodeLabel().getFirstChildElement();
            icon.setClassName(css.icon());
            icon.addClassName(css.column());
        }
    }

}
