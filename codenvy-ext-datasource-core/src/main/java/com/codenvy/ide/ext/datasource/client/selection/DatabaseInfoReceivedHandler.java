package com.codenvy.ide.ext.datasource.client.selection;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for database info reception events.
 * 
 * @author "Mickaël Leduque"
 */
public interface DatabaseInfoReceivedHandler extends EventHandler {

    /**
     * Called when {@link DatabaseInfoReceivedEvent} is fired.
     * 
     * @param event the {@link DatabaseInfoReceivedEvent} that was fired
     */
    void onDatabaseInfoReceived(DatabaseInfoReceivedEvent event);
}
