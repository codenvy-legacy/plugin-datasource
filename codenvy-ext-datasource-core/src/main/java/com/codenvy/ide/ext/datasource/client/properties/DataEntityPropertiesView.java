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

    void setShown(boolean shown);

    void bindDataProvider(AbstractDataProvider<Property> dataProvider);

}
