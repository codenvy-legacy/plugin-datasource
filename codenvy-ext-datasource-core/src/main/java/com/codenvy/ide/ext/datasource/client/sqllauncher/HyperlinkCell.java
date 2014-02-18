package com.codenvy.ide.ext.datasource.client.sqllauncher;

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
