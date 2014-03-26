package com.codenvy.ide.ext.datasource.client.properties;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResourcesProperties extends CellTable.Resources {

    interface CellTableStyle extends CellTable.Style {
    }

    @Source({"CellTable-properties.css", "com/codenvy/ide/api/ui/style.css"})
    CellTableStyle cellTableStyle();
}
