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
package com.codenvy.ide.ext.datasource.shared;

import com.codenvy.dto.shared.DTO;

@DTO
public interface ColumnDTO extends DatabaseMetadataEntityDTO {

    ColumnDTO withName(String name);


    ColumnDTO withColumnDataType(String datatypeName);

    String getColumnDataType();

    void setColumnDataType(String columnDataTypeName);


    ColumnDTO withDefaultValue(String defaultValue);

    void setDefaultValue(String defaultValue);

    String getDefaultValue();


    ColumnDTO withDataSize(int size);

    void setDataSize(int size);

    int getDataSize();


    ColumnDTO withDecimalDigits(int digits);

    void setDecimalDigits(int digits);

    int getDecimalDigits();


    ColumnDTO withNullable(boolean nullable);

    void setNullable(boolean nullable);

    boolean getNullable();
}
