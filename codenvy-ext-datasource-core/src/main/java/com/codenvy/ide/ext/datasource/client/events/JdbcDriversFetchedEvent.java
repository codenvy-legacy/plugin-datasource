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
package com.codenvy.ide.ext.datasource.client.events;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

public class JdbcDriversFetchedEvent extends GwtEvent<JdbcDriversFetchedEventHandler> {

    private static Type<JdbcDriversFetchedEventHandler> TYPE;

    protected List<String>                              drivers;

    public JdbcDriversFetchedEvent(List<String> drivers) {
        this.drivers = drivers;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<JdbcDriversFetchedEventHandler> getAssociatedType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<JdbcDriversFetchedEventHandler>();
        }
        return TYPE;
    }

    @Override
    protected void dispatch(JdbcDriversFetchedEventHandler handler) {
        handler.onJdbcDriversFetched(drivers);
    }

    public static com.google.gwt.event.shared.GwtEvent.Type<JdbcDriversFetchedEventHandler> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<JdbcDriversFetchedEventHandler>();
        }
        return TYPE;
    }
}
