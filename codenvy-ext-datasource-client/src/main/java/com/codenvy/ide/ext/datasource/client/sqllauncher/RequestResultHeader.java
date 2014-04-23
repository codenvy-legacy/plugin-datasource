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

import javax.validation.constraints.NotNull;

import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Result header is displayed along (on top of) the results themselves. It shows information on the request and controls on the results.
 * 
 * @author "Mickaël Leduque"
 */
public class RequestResultHeader extends Composite {

    /** The default name for the CSV resource. */
    private static final String         DEFAULT_CSV_FILENAME = "data.csv";

    /** The template used to generte the header. */
    private static final HeaderTemplate TEMPLATE             = GWT.create(HeaderTemplate.class);

    private static int                  TRUNCATE_LIMIT       = 150;

    /** A reminder of the SQL query that caused this result. */
    @UiField
    SimplePanel                         queryReminderPlace;

    /** A button to export the content to a CSV resource. */
    @UiField
    SimplePanel                         csvButtonPlace;

    @UiField
    SimplePanel                         csvLinkPlace;

    @UiField(provided = true)
    final DatasourceUiResources         datasourceUiResources;

    private final RequestResultDelegate delegate;

    @Inject
    public RequestResultHeader(@NotNull final DatasourceUiResources datasourceUiResources,
                               @NotNull final RequestResultHeaderUiBinder uiBinder,
                               @NotNull @Assisted final RequestResultDelegate delegate,
                               @NotNull @Assisted final String query) {
        super();
        this.datasourceUiResources = datasourceUiResources;
        initWidget(uiBinder.createAndBindUi(this));

        setRequestReminder(query);

        this.delegate = delegate;
        addStyleName(datasourceUiResources.datasourceUiCSS().resultItemHeaderBar());
    }

    private RequestResultHeader setRequestReminder(final String query) {
        // limit size of displayed query - just a bit over display overflow
        final String queryPart = query.substring(0, Math.min(query.length(), TRUNCATE_LIMIT));
        SafeHtml queryHtml = TEMPLATE.queryReminder(datasourceUiResources.datasourceUiCSS().resultItemQueryReminder(),
                                                    queryPart);
        final HTML queryReminder = new HTML(queryHtml);
        this.queryReminderPlace.setWidget(queryReminder);
        return this;
    }

    public RequestResultHeader withExportButton(final RequestResultDTO requestResult, final String text) {
        final Button exportButton = new Button(text);
        this.csvButtonPlace.setWidget(exportButton);
        exportButton.setStyleName(datasourceUiResources.datasourceUiCSS().resultItemCsvButton());
        exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                delegate.triggerCsvExport(requestResult, RequestResultHeader.this);
            }
        });
        return this;
    }

    public void showCsvLink(final String contentData) {
        final Anchor csvLink = new Anchor(TEMPLATE.csvExportLink(datasourceUiResources.datasourceUiCSS().resultItemCsvLink(),
                                                                 contentData, "Download CSV", DEFAULT_CSV_FILENAME));
        this.csvLinkPlace.setWidget(csvLink);
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

    /**
     * UIBinder interface for {@link RequestResultHeader}.
     * 
     * @author "Mickaël Leduque"
     */
    interface RequestResultHeaderUiBinder extends UiBinder<Widget, RequestResultHeader> {
    }
}
