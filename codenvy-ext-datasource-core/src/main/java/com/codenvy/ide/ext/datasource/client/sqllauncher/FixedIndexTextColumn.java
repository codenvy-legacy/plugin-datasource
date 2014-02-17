package com.codenvy.ide.ext.datasource.client.sqllauncher;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;

class FixedIndexTextColumn extends TextColumn<List<String>> {

    private final int columnIndex;

    public FixedIndexTextColumn(final int columnIndex) {
        this.columnIndex = columnIndex;
        setHorizontalAlignment(ALIGN_RIGHT);
    }

    @Override
    public String getValue(final List<String> line) {
        return line.get(this.columnIndex);
    }


}
