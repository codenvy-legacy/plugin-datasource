package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.google.gwt.user.client.ui.TextArea;

// temporary class
public class SqlLauncherTextArea extends TextArea {

    SqlLauncherTextArea(boolean readOnly) {
        super();
        setReadOnly(readOnly);
        getElement().getStyle().setBackgroundColor("white");
        getElement().getStyle().setProperty("fontFamily", "monospace");
    }
}
