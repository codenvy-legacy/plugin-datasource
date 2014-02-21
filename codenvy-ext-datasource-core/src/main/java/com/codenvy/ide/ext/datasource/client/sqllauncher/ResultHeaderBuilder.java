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
