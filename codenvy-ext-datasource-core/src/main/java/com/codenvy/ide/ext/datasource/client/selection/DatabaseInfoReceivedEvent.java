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
package com.codenvy.ide.ext.datasource.client.selection;

import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.google.gwt.event.shared.GwtEvent;

public class DatabaseInfoReceivedEvent extends GwtEvent<DatabaseInfoReceivedHandler> {

    /** Handler type. */
    private static Type<DatabaseInfoReceivedHandler> TYPE;

    /** The received item. */
    private final DatabaseDTO                        receivedInfo;

    public DatabaseInfoReceivedEvent(final DatabaseDTO newSelection) {
        super();
        this.receivedInfo = newSelection;
    }

    @Override
    public GwtEvent.Type<DatabaseInfoReceivedHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DatabaseInfoReceivedHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(DatabaseInfoReceivedHandler handler) {
        handler.onDatabaseInfoReceived(this);
    }

    /**
     * Returns the received database info.
     * 
     * @return the received item
     */
    public DatabaseDTO getReceivedInfo() {
        return receivedInfo;
    }

    /**
     * Returns the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<DatabaseInfoReceivedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DatabaseInfoReceivedHandler>();
        }
        return TYPE;
    }
}
