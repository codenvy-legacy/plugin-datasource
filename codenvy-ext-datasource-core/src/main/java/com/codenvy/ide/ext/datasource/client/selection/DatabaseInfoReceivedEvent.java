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
    public DatabaseDTO getSelection() {
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
