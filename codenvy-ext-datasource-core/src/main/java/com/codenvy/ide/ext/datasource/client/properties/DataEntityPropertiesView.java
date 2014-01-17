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
package com.codenvy.ide.ext.datasource.client.properties;

import com.codenvy.ide.api.mvp.View;
import com.google.gwt.view.client.AbstractDataProvider;

/**
 * Interface for the database item properties display view.
 * 
 * @author Mickaël LEDUQUE
 */
public interface DataEntityPropertiesView extends View<DataEntityPropertiesView.ActionDelegate> {

    /**
     * Interface for the action delegate on data item properties display view.
     * 
     * @author Mickaël LEDUQUE
     */
    interface ActionDelegate {
    }

    void setObjectName(String name);

    void setShown(boolean shown);

    void setObjectType(String type);

    void bindDataProvider(AbstractDataProvider<Property> dataProvider);

}
