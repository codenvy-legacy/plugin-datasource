package com.codenvy.ide.ext.datasource.client.common;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;

public class RightAlignColumnHeader extends SafeHtmlHeader {

    public RightAlignColumnHeader(String text) {
        super(getSafeHtml(text));
    }

    private static SafeHtml getSafeHtml(final String text) {
        SafeHtmlBuilder safeBuilder = new SafeHtmlBuilder();
        safeBuilder.appendHtmlConstant("<p style=\"text-align:right;\">");
        safeBuilder.appendEscaped(text);
        safeBuilder.appendHtmlConstant("</p>");
        return safeBuilder.toSafeHtml();
    }
}
