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

    DatabaseMetadataEntityDTO withName(String name);

    DatabaseMetadataEntityDTO withLookupKey(String lookupKey);
}
