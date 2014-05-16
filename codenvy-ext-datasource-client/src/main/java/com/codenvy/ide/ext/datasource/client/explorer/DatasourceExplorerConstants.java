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
package com.codenvy.ide.ext.datasource.client.explorer;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;

/**
 * Constant interface for the datasource explorer.
 * 
 * @author "Mickaël Leduque"
 */
@DefaultLocale("en")
public interface DatasourceExplorerConstants extends Constants {

    /** The tooltip for the refresh button. */
    @DefaultStringValue("Refresh and explore")
    String exploreButtonTooltip();

    /** The string used in the part (top). */
    @DefaultStringValue("Datasource Explorer")
    String datasourceExplorerPartTitle();

    /** The string used in the side tab. */
    @DefaultStringValue("Datasource")
    String datasourceExplorerTabTitle();

    @DefaultStringArrayValue({"Simplest - Tables and views",
            "Standard - adds materialized views, aliases, syn.",
            "System - add system tables/views",
            "All existing table types"})
    String[] tableCategories();
}
