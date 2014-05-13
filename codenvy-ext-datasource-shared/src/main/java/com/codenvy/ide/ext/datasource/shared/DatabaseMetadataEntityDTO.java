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

import com.codenvy.dto.shared.DTO;

/**
 * Interface for all database entity info.
 * 
 * @author "Mickaël Leduque"
 */
@DTO
public interface DatabaseMetadataEntityDTO {

    /**
     * Returns the (human readable) name of the object.
     * 
     * @return the name of the object.
     */
    String getName();

    /**
     * Affects the (human readable) name of the object.
     * 
     * @param name the new value
     */
    void setName(String name);


    /**
     * Chainable version of the name property setter.
     * 
     * @param name the value for name
     * @return this object
     */
    DatabaseMetadataEntityDTO withName(String name);

    /**
     * Returns an identifier for the object relative to the database.
     * 
     * @return the identifier
     */
    String getLookupKey();

    /**
     * Affect the identifier for the object in the database.
     * 
     * @param lookupKey the new value
     */
    void setLookupKey(String lookupKey);


    /**
     * Chainable version of the lookupKey property setter.
     * 
     * @param lookupKey the value for lookupKey
     * @return this object
     */
    DatabaseMetadataEntityDTO withLookupKey(String lookupKey);

    /**
     * Returns the comment for the database entity (if available).
     * 
     * @return the comment
     */
    String getComment();

    /**
     * Affect the comment for the database entity.
     * 
     * @param comment the new value
     */
    void setComment(String comment);

    /**
     * Chainable version of the comment property setter.
     * 
     * @param comment the value for comment
     * @return this object
     */
    DatabaseMetadataEntityDTO withComment(String comment);
}
