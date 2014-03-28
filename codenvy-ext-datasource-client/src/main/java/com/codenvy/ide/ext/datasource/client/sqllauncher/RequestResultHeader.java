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
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.ext.datasource.client.DatasourceUiResources.DatasourceUiStyle;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Result header is displayed along (on top of) the results themselves. It shows information on the request and controls on the results.
 * 
 * @author "Mickaël Leduque"
 */
public class RequestResultHeader extends DockLayoutPanel {

    private static final String         DEFAULT_CSV_FILENAME = "data.csv";
    private static final HeaderTemplate TEMPLATE             = GWT.create(HeaderTemplate.class);
    private static final int            INFO_HEADER_WIDTH    = 150;
    private static final int            EXPORT_BUTTON_WIDTH  = 70;
    private static final int            CSV_LINK_WIDTH       = 90;

    private static int                  TRUNCATE_LIMIT       = 150;

    private Widget                      infoHeaderTitle;
    private Widget                      queryReminder;
    private Button                      exportButton;
    private final SimpleLayoutPanel     csvLinkPanel         = new SimpleLayoutPanel();
    private final DatasourceUiStyle     style;
    private final RequestResultDelegate delegate;

    public RequestResultHeader(final DatasourceUiStyle style,
                               final RequestResultDelegate delegate) {
        super(Unit.PX);
        this.style = style;
        this.delegate = delegate;
        setStyleName(style.infoHeader());
    }

    public RequestResultHeader setInfoHeaderTitle(final String label) {
        this.infoHeaderTitle = new HTML(TEMPLATE.infoHeaderTitle(style.infoHeaderTitle(), label));
        return this;
    }

    public RequestResultHeader setRequestReminder(final String query) {
        // limit size of displayed query - just a bit over display overflow
        final String queryPart = query.substring(0, Math.min(query.length(), TRUNCATE_LIMIT));

        this.queryReminder = new HTML(TEMPLATE.queryReminder(style.queryReminder(), queryPart));
        return this;
    }

    public RequestResultHeader withExportButton(final RequestResultDTO requestResult, final String text) {
        this.exportButton = new Button(text);
        this.exportButton.setStyleName(style.csvButton());
        this.exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                delegate.triggerCsvExport(requestResult, RequestResultHeader.this);
            }
        });
        return this;
    }

    public void showCsvLink(final String contentData) {
        Anchor csvLink = new Anchor(TEMPLATE.csvExportLink(style.csvLink(), contentData, "Download CSV", DEFAULT_CSV_FILENAME));
        this.csvLinkPanel.setWidget(csvLink);
        setWidgetSize(this.csvLinkPanel, CSV_LINK_WIDTH);
    }

    public RequestResultHeader prepare() {
        clear();
        addWest(this.infoHeaderTitle, INFO_HEADER_WIDTH);
        if (this.exportButton != null) {
            addEast(this.exportButton, EXPORT_BUTTON_WIDTH);
        }
        addEast(this.csvLinkPanel, 0);
        add(this.queryReminder);
        return this;
    }

    /**
     * Template for the different pieces of the header.
     * 
     * @author "Mickaël Leduque"
     */
    interface HeaderTemplate extends SafeHtmlTemplates {

        /**
         * Template for the "query reminder" part of the result header. It shows the query that was made and gave this result.
         * 
         * @param className the CSS class name
         * @param query the query string to display
         * @return the html
         */
        @Template("<div class='{0}'>{1}</div>")
        SafeHtml queryReminder(String className, String query);

        /**
         * Template for the header title part.
         * 
         * @param className the CSS class name
         * @param label the title
         * @return the html
         */
        @Template("<span class='{0}'>{1}</span>")
        SafeHtml infoHeaderTitle(String className, String label);

        /**
         * Template for the header CSV link part.
         * 
         * @param className the CSS class name
         * @param csvDataContent the Base64-encoded content to include in the data: URI
         * @param label the label of the link
         * @param filename the saved file name
         * @return the html
         */
        @Template("<a class='{0}' target='_blank' href='data:text/csv;charset=utf8;base64,{1}' download='{3}'>{2}</a>")
        SafeHtml csvExportLink(String className, String csvDataContent, String label, String filename);
    }

    /**
     * Interface for the control delegate for the RequestResultHeader actions.
     * 
     * @author "Mickaël Leduque"
     */
    public interface RequestResultDelegate {
        /**
         * Causes the given request result to be converted to CSV and sent to user.
         * 
         * @param requestResult the request result
         * @param target the header that triggered the action, to be updated on completion
         */
        void triggerCsvExport(RequestResultDTO requestResult, RequestResultHeader target);
    }
}
