package com.codenvy.ide.ext.datasource.client.selection;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler interface for database entity selection events.
 * 
 * @author "Mickaël Leduque"
 */
public interface DatabaseEntitySelectionHandler extends EventHandler {

    /**
     * Called when {@link DatabaseEntitySelectionEvent} is fired.
     * 
     * @param event the {@link DatabaseEntitySelectionEvent} that was fired
     */
    void onDatabaseEntitySelection(DatabaseEntitySelectionEvent event);
}
