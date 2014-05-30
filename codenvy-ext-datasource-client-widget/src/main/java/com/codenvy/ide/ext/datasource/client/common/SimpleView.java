/*******************************************************************************
* Copyright (c) 2012-2014 Codenvy, S.A.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Codenvy, S.A. - initial API and implementation
*******************************************************************************/
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
