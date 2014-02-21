package com.codenvy.ide.ext.datasource.client.common;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResources extends CellTable.Resources {

    public interface CellTableStyle extends CellTable.Style {

        String infoHeader();

        String queryReminder();

        String csvButton();

        String infoHeaderTitle();
    }

    @Source({"CellTable.css", "com/codenvy/ide/api/ui/style.css"})
    CellTableStyle cellTableStyle();
}
