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

import java.util.Map;

import com.codenvy.dto.shared.DTO;

@DTO
public interface SchemaDTO extends DatabaseMetadataEntityDTO {

    SchemaDTO withName(String name);

    SchemaDTO withLookupKey(String lookupKey);

    SchemaDTO withComment(String comment);

    SchemaDTO withTables(Map<String, TableDTO> tables);

    Map<String, TableDTO> getTables();

    void setTables(Map<String, TableDTO> tables);

}
