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

import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.google.gwt.event.shared.GwtEvent;

public class DatabaseEntitySelectionEvent extends GwtEvent<DatabaseEntitySelectionHandler> {

    /** Handler type. */
    private static Type<DatabaseEntitySelectionHandler> TYPE;

    /** The (new) selected item. */
    private final DatabaseMetadataEntityDTO             selection;

    public DatabaseEntitySelectionEvent(final DatabaseMetadataEntityDTO newSelection) {
        super();
        this.selection = newSelection;
    }

    @Override
    public GwtEvent.Type<DatabaseEntitySelectionHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DatabaseEntitySelectionHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(DatabaseEntitySelectionHandler handler) {
        handler.onDatabaseEntitySelection(this);
    }

    /**
     * Returns the selected item from this event.
     * 
     * @return the selected item
     */
    public DatabaseMetadataEntityDTO getSelection() {
        return selection;
    }

    /**
     * Returns the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<DatabaseEntitySelectionHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DatabaseEntitySelectionHandler>();
        }
        return TYPE;
    }
}
