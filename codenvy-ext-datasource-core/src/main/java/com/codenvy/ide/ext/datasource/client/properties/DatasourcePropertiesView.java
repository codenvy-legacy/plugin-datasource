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
import com.codenvy.ide.api.parts.base.BaseActionDelegate;

/**
 * Interface for the datasource properties display view.
 * 
 * @author Mickaël LEDUQUE
 */
public interface DatasourcePropertiesView extends View<DatasourcePropertiesView.ActionDelegate> {

    /**
     * Interface for the action delegate on datasource properties display view.
     * 
     * @author Mickaël LEDUQUE
     */
    interface ActionDelegate extends BaseActionDelegate {
    }

    void setDatasourceName(String name);
}
