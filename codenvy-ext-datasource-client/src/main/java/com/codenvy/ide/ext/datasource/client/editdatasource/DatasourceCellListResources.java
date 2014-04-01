package com.codenvy.ide.ext.datasource.client.editdatasource;

import com.google.gwt.user.cellview.client.CellList;

public interface DatasourceCellListResources extends CellList.Resources {

    interface DatasourceCellListStyle extends CellList.Style {
    }

    @Source({"CellList.css", "com/codenvy/ide/api/ui/style.css"})
    DatasourceCellListStyle cellListStyle();
}
