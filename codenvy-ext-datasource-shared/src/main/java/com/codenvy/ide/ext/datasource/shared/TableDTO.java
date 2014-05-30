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
package com.codenvy.ide.ext.datasource.shared;

import java.util.List;
import java.util.Map;

import com.codenvy.dto.shared.DTO;

@DTO
public interface TableDTO extends DatabaseMetadataEntityDTO {

    TableDTO withName(String name);

    TableDTO withLookupKey(String lookupKey);

    TableDTO withComment(String comment);

    TableDTO withIsView(boolean b);

    TableDTO withColumns(Map<String, ColumnDTO> columns);

    TableDTO withType(String tableType);

    TableDTO withPrimaryKey(List<String> primaryKey);

    boolean getIsView();

    void setIsView(boolean b);

    Map<String, ColumnDTO> getColumns();

    void setColumns(Map<String, ColumnDTO> columns);

    void setType(String tabletype);

    String getType();

    void setPrimaryKey(List<String> primaryKey);

    List<String> getPrimaryKey();
}
