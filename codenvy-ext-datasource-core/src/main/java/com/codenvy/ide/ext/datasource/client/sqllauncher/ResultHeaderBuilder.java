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
