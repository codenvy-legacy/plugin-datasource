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
package com.codenvy.ide.ext.datasource.client.common;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.PartStackUIResources;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.LayoutPanel;

public abstract class ViewWithToolbar<T> extends Composite implements View<T> {

    private final LayoutPanel     toolBar;
    private final DockLayoutPanel container;
    private T                     delegate;

    public ViewWithToolbar(final PartStackUIResources resources) {
        container = new DockLayoutPanel(Style.Unit.PX);
        initWidget(container);
        container.setSize("100%", "100%");
        toolBar = new LayoutPanel();
        toolBar.addStyleName(resources.partStackCss().ideBasePartToolbar());
        container.addNorth(toolBar, 20);

        // this hack used for adding box shadow effect to toolbar
        toolBar.getElement().getParentElement().getStyle().setOverflow(Style.Overflow.VISIBLE);
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(T delegate) {
        this.delegate = delegate;
    }

    public T getDelegate() {
        return delegate;
    }

    public DockLayoutPanel getContainer() {
        return container;
    }

    public LayoutPanel getToolBar() {
        return toolBar;
    }
}
