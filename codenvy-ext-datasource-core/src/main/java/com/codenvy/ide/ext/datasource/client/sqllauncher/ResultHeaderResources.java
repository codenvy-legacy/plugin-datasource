package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface ResultHeaderResources extends ClientBundle {

    public interface CellTableStyle extends CssResource {

        String infoHeader();

        String queryReminder();

        String csvButton();

        String infoHeaderTitle();
    }

    @Source({"CellTable.css", "com/codenvy/ide/api/ui/style.css"})
    CellTableStyle cellTableStyle();
}
