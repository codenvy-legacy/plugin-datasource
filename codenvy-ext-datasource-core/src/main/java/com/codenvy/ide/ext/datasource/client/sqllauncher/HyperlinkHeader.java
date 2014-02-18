package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.ext.datasource.client.common.HyperlinkCell;
import com.google.gwt.user.cellview.client.Header;

/**
 * A header/footer that displays a hyperlink.
 * 
 * @author "Mickaël Leduque"
 */
public class HyperlinkHeader extends Header<String> {

    /** The URL of the link. */
    private final String url;

    public HyperlinkHeader(final String url, final String linkText) {
        super(new HyperlinkCell(url, linkText));
        this.url = url;
    }

    @Override
    public String getValue() {
        return this.url;
    }
}
