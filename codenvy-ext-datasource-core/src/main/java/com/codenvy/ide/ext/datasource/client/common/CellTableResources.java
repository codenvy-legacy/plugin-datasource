package com.codenvy.ide.ext.datasource.client.common;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResources extends CellTable.Resources {

    interface CellTableStyle extends CellTable.Style {
    }

    @Source({"CellTable.css", "com/codenvy/ide/api/ui/style.css"})
    CellTableStyle cellTableStyle();
}
