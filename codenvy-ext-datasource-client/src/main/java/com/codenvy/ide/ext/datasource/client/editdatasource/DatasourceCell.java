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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.inject.Inject;

/**
 * Display of a datasource in the edit/delete dialog list.
 * 
 * @author "Mickaël Leduque"
 */
public class DatasourceCell extends AbstractCell<DatabaseConfigurationDTO> {

    private final DatasourceCellStyle style;
    private final CellTemplate        template;

    @Inject
    public DatasourceCell(final DatasourceCellResource resource,
                          final CellTemplate template) {
        this.style = resource.datasourceCellStyle();
        this.template = template;
    }

    @Override
    public void render(final Context context, final DatabaseConfigurationDTO value, final SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }

        final SafeHtml id = SafeHtmlUtils.fromString(value.getDatasourceId());
        final SafeHtml type = SafeHtmlUtils.fromString(value.getDatabaseType().toString());

        sb.append(this.template.datasourceItem(id, style.datasourceIdStyle(), type, style.datasourceTypeStyle()));
    }

    /**
     * {@link SafeHtml} template for the datasource cell.
     * 
     * @author "Mickaël Leduque"
     */
    interface CellTemplate extends SafeHtmlTemplates {

        @Template("<table><tr><td> <span class='{1}'>{0}</span></td><td><span class='{3}'>{2}</span></td></tr></table>")
        SafeHtml datasourceItem(SafeHtml datasourceId, String idStyle, SafeHtml datasourceType, String typeStyle);
    }

    /**
     * Resource interface for the datasource cell component.
     * 
     * @author "Mickaël Leduque"
     */
    interface DatasourceCellResource extends ClientBundle {

        @Source({"datasource-cell.css", "com/codenvy/ide/api/ui/style.css"})
        DatasourceCellStyle datasourceCellStyle();
    }

    /**
     * CSS interface for the datasource cell component.
     * 
     * @author "Mickaël Leduque"
     */
    interface DatasourceCellStyle extends CssResource {

        /**
         * Style for the datasource Type display.
         * 
         * @return the style name
         */
        String datasourceTypeStyle();

        /**
         * Style for the datasource Id display.
         * 
         * @return the style name
         */
        String datasourceIdStyle();

    }
}
