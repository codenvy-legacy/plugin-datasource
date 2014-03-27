/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
