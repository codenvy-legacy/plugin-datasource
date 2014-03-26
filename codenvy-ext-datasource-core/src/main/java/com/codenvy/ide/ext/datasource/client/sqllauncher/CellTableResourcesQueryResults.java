package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResourcesQueryResults extends CellTable.Resources {

    interface CellTableStyle extends CellTable.Style {
    }

    @Source({"CellTable-sql-results.css", "com/codenvy/ide/api/ui/style.css"})
    CellTableStyle cellTableStyle();
}
