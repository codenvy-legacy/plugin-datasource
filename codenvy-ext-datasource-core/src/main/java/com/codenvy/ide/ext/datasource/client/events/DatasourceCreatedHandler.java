package com.codenvy.ide.ext.datasource.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for datasource creation events.
 * 
 * @author "Mickaël Leduque"
 */
public interface DatasourceCreatedHandler extends EventHandler {

    /**
     * Action taken on datasource creation.
     * 
     * @param event the event
     */
    void onDatasourceCreated(DatasourceCreatedEvent event);
}
