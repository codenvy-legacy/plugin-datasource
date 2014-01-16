package com.codenvy.ide.ext.datasource.client.properties;

import com.google.gwt.view.client.ProvidesKey;

class PropertyKeyProvider implements ProvidesKey<Property> {

    @Override
    public Object getKey(Property item) {
        return item.getName();
    }

}
