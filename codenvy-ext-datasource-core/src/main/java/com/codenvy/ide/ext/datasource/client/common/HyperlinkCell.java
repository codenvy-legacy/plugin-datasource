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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;

/**
 * A {@link Cell} type that contains a hyperlink.
 * 
 * @author "Mickaël Leduque"
 */
public class HyperlinkCell extends TextCell {

    /** The instance of the hyperlink template. */
    private static final Template TEMPLATE = GWT.create(Template.class);

    public HyperlinkCell(final String url, final String linkText) {
        super(new AbstractSafeHtmlRenderer<String>() {
            @Override
            public SafeHtml render(final String value) {
                return TEMPLATE.hyperlink(UriUtils.fromString(value), linkText);
            }
        });
    }

    /**
     * Template to build a SafeHtml hyperlink.
     * 
     * @author "Mickaël Leduque"
     */
    interface Template extends SafeHtmlTemplates {
        @Template("<a target=\"_blank\" href=\"{0}\">{1}</a>")
        SafeHtml hyperlink(SafeUri link, String text);
    }
}
