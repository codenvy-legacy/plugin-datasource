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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

public abstract class SimpleView<T> extends Composite implements View<T> {

    private final SimpleLayoutPanel container;
    private T                       delegate;

    public SimpleView() {
        container = new SimpleLayoutPanel();
        initWidget(container);
        container.setSize("100%", "100%");
    }

    /** {@inheritDoc} */
    @Override
    public void setDelegate(T delegate) {
        this.delegate = delegate;
    }

    public T getDelegate() {
        return delegate;
    }

    public SimpleLayoutPanel getContainer() {
        return container;
    }
}
