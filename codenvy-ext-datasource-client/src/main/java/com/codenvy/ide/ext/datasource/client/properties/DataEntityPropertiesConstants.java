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

    @DefaultStringValue("Position in table")
    String ordinalPositionLabel();

    @DefaultStringValue("Object type")
    String objectTypeLabel();

    @DefaultStringValue("Object name")
    String objectNameLabel();

    @DefaultStringValue("Comment")
    String commentLabel();

    @DefaultStringValue("None")
    String noValue();

    @DefaultStringValue("Columns")
    String columnCountLabel();

    @DefaultStringValue("Tables")
    String tableCountLabel();

    @DefaultStringValue("Schemas")
    String schemaCountLabel();


    @DefaultStringValue("database")
    String objectTypeDatabase();

    @DefaultStringValue("schema")
    String objectTypeSchema();

    @DefaultStringValue("table")
    String objectTypeTable();

    @DefaultStringValue("column")
    String objectTypeColumn();

}
