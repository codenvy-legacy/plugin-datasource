package com.codenvy.ide.ext.datasource.client.common;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResources extends CellTable.Resources {

    @Source({"CellTable.css", "com/codenvy/ide/api/ui/style.css"})
    CellTable.Style cellTableStyle();
}
