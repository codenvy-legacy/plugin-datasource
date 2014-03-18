package com.codenvy.ide.ext.datasource.client.common;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResourcesNoAlt extends CellTable.Resources {

    interface CellTableStyle extends CellTable.Style {
    }

    @Source({"CellTable-no-alt.css", "com/codenvy/ide/api/ui/style.css"})
    CellTableStyle cellTableStyle();
}
