/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2013] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.datasource.client.common;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public class AlignableColumnHeader extends SafeHtmlHeader {

    public AlignableColumnHeader(final String text, final HorizontalAlignmentConstant alignment) {
        super(getSafeHtml(text, alignment));
    }

    private static SafeHtml getSafeHtml(final String text, final HorizontalAlignmentConstant alignment) {
        SafeHtmlBuilder safeBuilder = new SafeHtmlBuilder();
        boolean needCloseTag = false;
        if (HasHorizontalAlignment.ALIGN_RIGHT.equals(alignment)) {
            safeBuilder.appendHtmlConstant("<p style=\"text-align:right;\">");
            needCloseTag = true;
        } else if (HasHorizontalAlignment.ALIGN_CENTER.equals(alignment)) {
            safeBuilder.appendHtmlConstant("<p style=\"text-align:center;\">");
            needCloseTag = true;
        }
        safeBuilder.appendEscaped(text);
        if (needCloseTag) {
            safeBuilder.appendHtmlConstant("</p>");
        }
        return safeBuilder.toSafeHtml();
    }
}
