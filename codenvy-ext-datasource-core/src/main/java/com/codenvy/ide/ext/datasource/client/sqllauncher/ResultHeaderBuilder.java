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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import java.util.List;

import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.DefaultHeaderOrFooterBuilder;

public class ResultHeaderBuilder extends DefaultHeaderOrFooterBuilder<List<String>> {

    private final SafeHtml firstLine;
    private final int      columnsCount;
    private final String   infoHeaderClass;

    public ResultHeaderBuilder(final AbstractCellTable<List<String>> table, final SafeHtml firstLine,
                               final int columns, final String infoHeaderClass) {
        super(table, false);
        this.firstLine = firstLine;
        this.columnsCount = columns;
        this.infoHeaderClass = infoHeaderClass;
    }

    @Override
    protected boolean buildHeaderOrFooterImpl() {
        TableRowBuilder tr = startRow();
        TableCellBuilder th = tr.startTH().colSpan(columnsCount).className(this.infoHeaderClass);
        th.html(firstLine);
        th.endTH();
        tr.endTR();

        buildSecondLine();
        return true;
    }

    protected boolean buildSecondLine() {
        return super.buildHeaderOrFooterImpl();
    }

}
