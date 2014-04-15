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
package com.codenvy.ide.ext.datasource.client.selection;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event triggered when database info retrieval fails.
 * 
 * @author "Mickaël Leduque"
 */
public class DatabaseInfoErrorEvent extends GwtEvent<DatabaseInfoErrorHandler> {

    /** Handler type. */
    private static Type<DatabaseInfoErrorHandler> TYPE;

    /** The id of the datasource for which retrieval failed. */
    private final String                          databaseId;

    public DatabaseInfoErrorEvent(final String databaseId) {
        super();
        this.databaseId = databaseId;
    }

    @Override
    public GwtEvent.Type<DatabaseInfoErrorHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DatabaseInfoErrorHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(DatabaseInfoErrorHandler handler) {
        handler.onDatabaseInfoError(this);
    }

    /**
     * Returns the id of the datasource for which metadata retrieval failed.
     * 
     * @return the received datasource id
     */
    public String getDatabaseId() {
        return databaseId;
    }

    /**
     * Returns the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<DatabaseInfoErrorHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DatabaseInfoErrorHandler>();
        }
        return TYPE;
    }
}
