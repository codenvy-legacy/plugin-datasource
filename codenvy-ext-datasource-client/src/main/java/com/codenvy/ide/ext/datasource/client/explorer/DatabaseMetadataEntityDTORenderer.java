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

import com.codenvy.ide.ext.datasource.client.explorer.DatabaseMetadataEntityDTODataAdapter.EntityTreeNode;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.ui.tree.NodeRenderer;
import com.codenvy.ide.ui.tree.TreeNodeElement;
import com.codenvy.ide.util.dom.Elements;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;

import elemental.html.DivElement;
import elemental.dom.Element;
import elemental.html.SpanElement;

/**
 * Node renderer for the datasource explorer tree.
 */
public class DatabaseMetadataEntityDTORenderer implements NodeRenderer<EntityTreeNode> {

    /** The tree CSS resource. */
    private final Css css;

    @Inject
    public DatabaseMetadataEntityDTORenderer(final Resources resources) {
        this.css = resources.getCss();
        resources.getCss().ensureInjected();
    }

    @Override
    public Element getNodeKeyTextContainer(final SpanElement treeNodeLabel) {
        return (Element)treeNodeLabel.getChildNodes().item(1);
    }

    @Override
    public SpanElement renderNodeContents(final EntityTreeNode data) {
        String iconClassName = css.schemaIcon();
        String labelClassName = css.schemaLabel();

        if (data.getData() instanceof TableDTO) {
            iconClassName = css.tableIcon();
            labelClassName = css.tableLabel();
        } else if (data.getData() instanceof ColumnDTO) {
            iconClassName = css.columnIcon();
            labelClassName = css.columnLabel();
        }

        return renderNodeContents(css, data.getData().getName(), iconClassName, true, labelClassName);
    }

    @Override
    public void updateNodeContents(final TreeNodeElement<EntityTreeNode> treeNode) {
        if (treeNode.getData().getData() instanceof TableDTO) {
            Element icon = treeNode.getNodeLabel().getFirstElementChild();
            icon.setClassName(css.icon());
            Elements.addClassName(css.tableIcon(), icon);

        } else if (treeNode.getData().getData() instanceof SchemaDTO) {
            Element icon = treeNode.getNodeLabel().getFirstElementChild();
            icon.setClassName(css.icon());
            Elements.addClassName(css.schemaIcon(), icon);
        } else if (treeNode.getData().getData() instanceof ColumnDTO) {
            Element icon = treeNode.getNodeLabel().getFirstElementChild();
            icon.setClassName(css.icon());
            Elements.addClassName(css.columnIcon(), icon);
        }
    }

    /**
     * Renders the tree node.
     * 
     * @param css the CSS resource for this tree
     * @param contents the node contents
     * @param iconClassName the class name for the icon
     * @param renderIcon true to render the icon
     * @param labelClassName the class name for the abel part
     * @return the HTML element for the treer node
     */
    public static SpanElement renderNodeContents(final Css css, final String contents, final String iconClassName,
                                                 final boolean renderIcon, final String labelClassName) {

        SpanElement root = Elements.createSpanElement(css.root());
        if (renderIcon) {
            DivElement icon = Elements.createDivElement(css.icon());
            Elements.addClassName(iconClassName, icon);
            root.appendChild(icon);
        }

        final Element label;
        label = Elements.createSpanElement(css.label(), labelClassName);
        label.setTextContent(contents);
        root.appendChild(label);

        return root;
    }

    /**
     * The CSSResource interface for the datasource explorer tree.
     */
    public interface Css extends CssResource {

        /**
         * Returns the CSS class for schema icon.
         * 
         * @return class name
         */
        String schemaIcon();

        /**
         * Returns the CSS class for table icon.
         * 
         * @return class name
         */
        String tableIcon();

        /**
         * Returns the CSS class for column icon.
         * 
         * @return class name
         */
        String columnIcon();

        /**
         * Returns the CSS class for schema label.
         * 
         * @return class name
         */
        String schemaLabel();

        /**
         * Returns the CSS class for table label.
         * 
         * @return class name
         */
        String tableLabel();

        /**
         * Returns the CSS class for column label.
         * 
         * @return class name
         */
        String columnLabel();

        /**
         * Returns the CSS class for tree root.
         * 
         * @return class name
         */
        String root();

        /**
         * Returns the CSS class for tree icons.
         * 
         * @return class name
         */
        String icon();

        /**
         * Returns the CSS class for tree labels.
         * 
         * @return class name
         */
        String label();

    }

    /**
     * The resource interface for the datasource explorer tree.
     */
    public interface Resources extends com.codenvy.ide.Resources {

        /** Returns the CSS resource for the datasource explorer tree. */
        @Source({"DatabaseMetadataEntityDTORenderer.css", "com/codenvy/ide/common/constants.css",
                "com/codenvy/ide/api/ui/style.css"})
        DatabaseMetadataEntityDTORenderer.Css getCss();

        /** Returns the icon for schema nodes. */
        @Source("schema.png")
        ImageResource schema();

        /** Returns the icon for table nodes. */
        @Source("table.png")
        ImageResource table();

        /** Returns the icon for column nodes. */
        @Source("column.png")
        ImageResource column();
    }
}
