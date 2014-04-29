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
 * Event thrown when the explorer selected datasource changes.
 * 
 * @author "Mickaël Leduque"
 */
public class SelectedDatasourceChangeEvent extends GwtEvent<SelectedDatasourceChangeHandler> {

    /** Handler type. */
    private static Type<SelectedDatasourceChangeHandler> TYPE;

    /** The id of the selected datasource. */
    private final String                                 selectedDatasourceId;

    /**
     * Creates an event.
     * 
     * @param selectionId the id of the selected datasource
     */
    public SelectedDatasourceChangeEvent(final String selectionId) {
        this.selectedDatasourceId = selectionId;
    }

    @Override
    public GwtEvent.Type<SelectedDatasourceChangeHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<SelectedDatasourceChangeHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(final SelectedDatasourceChangeHandler handler) {
        handler.onSelectedDatasourceChange(this);
    }

    /**
     * Returns the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<SelectedDatasourceChangeHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<SelectedDatasourceChangeHandler>();
        }
        return TYPE;
    }

    /**
     * Returns the id of the selected datasource.
     * 
     * @return the datasource id
     */
    public String getSelectedDatasourceId() {
        return selectedDatasourceId;
    }
}
