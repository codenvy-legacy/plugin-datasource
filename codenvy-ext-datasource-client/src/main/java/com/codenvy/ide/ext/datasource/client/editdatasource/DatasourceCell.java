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
package com.codenvy.ide.ext.datasource.client.editdatasource;

import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Display of a datasource in the edit/delete dialog list.
 * 
 * @author "Mickaël Leduque"
 */
public class DatasourceCell extends AbstractCell<DatabaseConfigurationDTO> {

    @Override
    public void render(final Context context, final DatabaseConfigurationDTO value, final SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        sb.appendHtmlConstant("<table><tr>");
        sb.appendHtmlConstant("<td>")
          .appendEscaped(value.getDatasourceId())
          .appendHtmlConstant("</td>");
        sb.appendHtmlConstant("<td>")
          .appendEscaped(value.getDatabaseType().toString())
          .appendHtmlConstant("</td>");
        sb.appendHtmlConstant("</tr></table>");
    }
}
