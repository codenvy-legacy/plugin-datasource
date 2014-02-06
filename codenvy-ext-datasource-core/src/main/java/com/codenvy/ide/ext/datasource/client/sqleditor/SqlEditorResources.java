package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface SqlEditorResources extends ClientBundle {

    @Source("codemirror-parser-sql.js")
    TextResource sqlParserJs();

    @Source("sql-icon.png")
    ImageResource sqlFile();

    @Source("sql.css")
    CssResource sqlCSS();
}
