package com.codenvy.ide.ext.datasource.client.events;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.event.shared.GwtEvent;

/**
 * New datasource event.
 * 
 * @author "Mickaël Leduque"
 */
public class DatasourceCreatedEvent extends GwtEvent<DatasourceCreatedHandler> {

    /** Handler type. */
    private static Type<DatasourceCreatedHandler> TYPE;

    /** The (new) selected item. */
    private final DatabaseConfigurationDTO        newDatasource;

    public DatasourceCreatedEvent(final DatabaseConfigurationDTO newSelection) {
        super();
        this.newDatasource = newSelection;
    }

    @Override
    public GwtEvent.Type<DatasourceCreatedHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<DatasourceCreatedHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(DatasourceCreatedHandler handler) {
        handler.onDatasourceCreated(this);
    }

    /**
     * Returns the newly created datasource configuration item from this event.
     * 
     * @return the new datasource
     */
    public DatabaseConfigurationDTO getNewDatasource() {
        return newDatasource;
    }

    /**
     * Returns the type associated with this event.
     * 
     * @return returns the handler type
     */
    public static Type<DatasourceCreatedHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<DatasourceCreatedHandler>();
        }
        return TYPE;
    }
}
