package com.codenvy.ide.ext.datasource.client.sqllauncher;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

class FixedIndexTextColumn extends TextColumn<List<String>> {

    private final int columnIndex;

    public FixedIndexTextColumn(final int columnIndex, HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
        this.columnIndex = columnIndex;
        setHorizontalAlignment(alignment);
    }

    @Override
    public String getValue(final List<String> line) {
        return line.get(this.columnIndex);
    }


}
