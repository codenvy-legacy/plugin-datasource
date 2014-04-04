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
package com.codenvy.ide.ext.datasource.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event thrown when the datasource list changes.
 * 
 * @author "Mickaël Leduque"
 */
public class DatasourceListChangeEvent extends GwtEvent<DatasourceListChangeHandler> {

    /** Handler type. */
    private static Type<DatasourceListChangeHandler> TYPE;

    @Override
    public GwtEvent.Type<DatasourceListChangeHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DatasourceListChangeHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(final DatasourceListChangeHandler handler) {
        handler.onDatasourceListChange(this);
    }

    /**
     * Returns the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<DatasourceListChangeHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DatasourceListChangeHandler>();
        }
        return TYPE;
    }
}
