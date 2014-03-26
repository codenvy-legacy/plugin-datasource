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
