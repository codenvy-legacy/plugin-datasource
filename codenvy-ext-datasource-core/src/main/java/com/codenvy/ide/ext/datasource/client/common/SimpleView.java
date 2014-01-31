package com.codenvy.ide.ext.datasource.client.common;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.parts.PartStackUIResources;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

public abstract class SimpleView<T> extends Composite implements View<T> {

    private final SimpleLayoutPanel container;
    private T                       delegate;

    public SimpleView(final PartStackUIResources resources) {
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
