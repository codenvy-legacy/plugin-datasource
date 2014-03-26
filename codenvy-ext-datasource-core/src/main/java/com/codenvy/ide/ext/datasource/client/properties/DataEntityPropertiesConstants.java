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
