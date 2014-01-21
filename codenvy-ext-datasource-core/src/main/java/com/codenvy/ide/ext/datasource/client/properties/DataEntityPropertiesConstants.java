package com.codenvy.ide.ext.datasource.client.properties;

import com.google.gwt.i18n.client.Constants;

public interface DataEntityPropertiesConstants extends Constants {

    @DefaultStringValue("Product name")
    String productNameLabel();

    @DefaultStringValue("Product version")
    String productVersionLabel();

    @DefaultStringValue("User name")
    String usernameLabel();

    @DefaultStringValue("Primary key")
    String primaryKeyLabel();

    @DefaultStringValue("Table type")
    String tableTypeLabel();


    @DefaultStringValue("Data type")
    String dataTypeLabel();

    @DefaultStringValue("Column size")
    String columnSizeLabel();

    @DefaultStringValue("Decimal digits")
    String decimalDigitsLabel();

    @DefaultStringValue("Nullable")
    String nullableLabel();

    @DefaultStringValue("Default value")
    String defaultValueLabel();

    @DefaultStringValue("Object type")
    String objectTypeLabel();

    @DefaultStringValue("Object name")
    String objectNameLabel();

    @DefaultStringValue("None")
    String noValue();


    @DefaultStringValue("database")
    String objectTypeDatabase();

    @DefaultStringValue("schema")
    String objectTypeSchema();

    @DefaultStringValue("table")
    String objectTypeTable();

    @DefaultStringValue("column")
    String objectTypeColumn();

}
