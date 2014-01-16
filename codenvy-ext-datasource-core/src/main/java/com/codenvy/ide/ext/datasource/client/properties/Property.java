package com.codenvy.ide.ext.datasource.client.properties;

class Property {

    private final String name;

    private final String value;

    public Property(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Property [name=" + name + ", value=" + value + "]";
    }
}
