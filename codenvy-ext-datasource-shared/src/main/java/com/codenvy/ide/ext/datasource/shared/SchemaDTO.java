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
