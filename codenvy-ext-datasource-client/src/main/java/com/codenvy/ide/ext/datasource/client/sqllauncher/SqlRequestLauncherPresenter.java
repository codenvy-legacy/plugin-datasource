/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_LEFT;

import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.Notification.Type;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.preferences.PreferencesManager;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.common.AlignableColumnHeader;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.common.TextEditorPartAdapter;
import com.codenvy.ide.ext.datasource.client.common.interaction.DialogFactory;
import com.codenvy.ide.ext.datasource.client.common.interaction.message.MessageWindow;
import com.codenvy.ide.ext.datasource.client.common.pager.Pager;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeEvent;
import com.codenvy.ide.ext.datasource.client.events.DatasourceListChangeHandler;
import com.codenvy.ide.ext.datasource.client.service.FetchMetadataService;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorProvider;
import com.codenvy.ide.ext.datasource.client.sqllauncher.RequestResultHeaderImpl.RequestResultDelegate;
import com.codenvy.ide.ext.datasource.client.store.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.client.store.DatasourceManager;
import com.codenvy.ide.ext.datasource.shared.DatabaseConfigurationDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.request.ExecutionErrorResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultGroupDTO;
import com.codenvy.ide.ext.datasource.shared.request.SelectResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.UpdateResultDTO;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


public class SqlRequestLauncherPresenter extends TextEditorPartAdapter<ReadableContentTextEditor> implements
                                                                                                 SqlRequestLauncherView.ActionDelegate,
                                                                                                 DatasourceListChangeHandler,
                                                                                                 RequestResultDelegate {

    private static final int                          DEFAULT_RESULT_PAGE_SIZE             = 100;

    /** Preference property name for default result limit. */
    private static final String                       PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT = "SqlEditor_default_request_limit";

    /** Default value for request limit (when no pref is set). */
    private static final int                          DEFAULT_REQUEST_LIMIT                = 20;

    private static final MultipleRequestExecutionMode DEFAULT_EXECUTION_MODE               =
                                                                                             MultipleRequestExecutionMode.STOP_AT_FIRST_ERROR;
    /** The matching view. */
    private final SqlRequestLauncherView              view;
    /** The i18n-able constants. */
    private final SqlRequestLauncherConstants         constants;

    /** The DTO factory. */
    private final DtoFactory                          dtoFactory;

    private String                                    selectedDatasourceId                 = null;
    private int                                       resultLimit                          = DEFAULT_REQUEST_LIMIT;
    private MultipleRequestExecutionMode              executionMode                        = DEFAULT_EXECUTION_MODE;

    private final DatasourceClientService             datasourceClientService;
    private final NotificationManager                 notificationManager;
    private final DatasourceManager                   datasourceManager;
    private final DatabaseInfoStore                   databaseInfoStore;

    private final EditorDatasourceOracle              editorDatasourceOracle;
    private final CellTableResourcesQueryResults      cellTableResources;

    private final ResultItemBoxFactory                resultItemBoxFactory;

    private final RequestResultHeaderFactory          requestResultHeaderFactory;

    /** The factory used to build message windows. */
    private final DialogFactory                       dialogFactory;

    /** The service used to obtain datasource metadata. */
    private final FetchMetadataService                fetchMetadataService;

    @Inject
    public SqlRequestLauncherPresenter(final @NotNull SqlRequestLauncherView view,
                                       final @NotNull SqlRequestLauncherConstants constants,
                                       final @NotNull PreferencesManager preferencesManager,
                                       final @NotNull SqlEditorProvider sqlEditorProvider,
                                       final @NotNull DatasourceClientService datasourceClientService,
                                       final @NotNull FetchMetadataService fetchMetadataService,
                                       final @NotNull DatabaseInfoStore databaseInfoStore,
                                       final @NotNull NotificationManager notificationManager,
                                       final @NotNull DatasourceManager datasourceManager,
                                       final @NotNull EditorDatasourceOracle editorDatasourceOracle,
                                       final @NotNull EventBus eventBus,
                                       final @NotNull DtoFactory dtoFactory,
                                       final @NotNull WorkspaceAgent workspaceAgent,
                                       final @NotNull CellTableResourcesQueryResults cellTableResources,
                                       final @NotNull ResultItemBoxFactory resultItemBoxFactory,
                                       final @NotNull RequestResultHeaderFactory requestResultHeaderFactory,
                                       final @NotNull DialogFactory dialogFactory) {
        super(sqlEditorProvider.getEditor(), workspaceAgent, eventBus);
        this.databaseInfoStore = databaseInfoStore;
        this.editorDatasourceOracle = editorDatasourceOracle;
        Log.info(SqlRequestLauncherPresenter.class, "New instance of SQL request launcher presenter resquested.");
        this.view = view;
        this.view.setDelegate(this);
        this.constants = constants;
        this.dtoFactory = dtoFactory;

        this.datasourceClientService = datasourceClientService;
        this.fetchMetadataService = fetchMetadataService;
        this.notificationManager = notificationManager;
        this.datasourceManager = datasourceManager;
        this.cellTableResources = cellTableResources;
        this.resultItemBoxFactory = resultItemBoxFactory;
        this.requestResultHeaderFactory = requestResultHeaderFactory;
        this.dialogFactory = dialogFactory;

        setupResultLimit(preferencesManager);
        setupExecutionMode(preferencesManager);

        // register for datasource creation events
        eventBus.addHandler(DatasourceListChangeEvent.getType(), this);
    }

    private void setupResultLimit(final PreferencesManager preferencesManager) {
        final String prefRequestLimit = preferencesManager.getValue(PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT);

        if (prefRequestLimit != null) {
            try {
                int prefValue = Integer.parseInt(prefRequestLimit);
                if (prefValue > 0) {
                    this.resultLimit = prefValue;
                } else {
                    Log.warn(SqlRequestLauncherPresenter.class, "negative value stored in preference "
                                                                + PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT);
                }
            } catch (final NumberFormatException e) {
                StringBuilder sb = new StringBuilder("Preference stored in ")
                                                                             .append(PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT)
                                                                             .append(" is not an integer (")
                                                                             .append(resultLimit)
                                                                             .append(").");
                Log.warn(SqlRequestLauncherPresenter.class, sb.toString());
            }
        }

        // push the request limit value to the view
        this.view.setResultLimit(this.resultLimit);
    }

    private void setupExecutionMode(final PreferencesManager preferencesManager) {
        // TODO : try to read preferences
        this.view.setExecutionMode(this.executionMode);
    }

    private void setupDatasourceComponent() {
        Collection<String> datasourceIds = this.datasourceManager.getNames();
        this.view.setDatasourceList(datasourceIds);
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        container.setWidget(view);
        getEditor().go(this.view.getEditorZone());

        setupDatasourceComponent();
    }

    @Override
    public void datasourceChanged(final String dataSourceId) {
        String tempDataSourceId = dataSourceId; // we need newDataSourceId to be final so we must use a temp var here
        if (dataSourceId == null || dataSourceId.isEmpty()) {
            tempDataSourceId = null;
        }
        final String newDataSourceId = tempDataSourceId;

        Log.info(SqlRequestLauncherPresenter.class, "Datasource changed to " + newDataSourceId);
        this.selectedDatasourceId = newDataSourceId;
        String editorFileId = getEditorInput().getFile().getId();
        Log.info(SqlRequestLauncherPresenter.class, "Associating editor file id " + editorFileId + " to datasource " + newDataSourceId);
        editorDatasourceOracle.setSelectedDatasourceId(editorFileId, newDataSourceId);
        if (newDataSourceId == null) {
            return;
        }
        DatabaseDTO dsMeta = databaseInfoStore.getDatabaseInfo(newDataSourceId);
        if (dsMeta == null) {
            this.fetchMetadataService.fetchDatabaseInfo(this.datasourceManager.getByName(newDataSourceId));
        }
    }

    @Override
    public void resultLimitChanged(final String newResultLimitString) {
        Log.info(SqlRequestLauncherPresenter.class, "Attempt to change result limit to " + newResultLimitString);
        int resultLimitValue;
        try {
            resultLimitValue = Integer.parseInt(newResultLimitString);
            if (resultLimitValue < 0) {
                notifyUserError(constants.userErrorNegativeLimit());
                Log.debug(SqlRequestLauncherPresenter.class, "  new value for result limit is negative - abort change");
                this.view.setResultLimit(this.resultLimit);
            } else if (resultLimitValue == 0) {
                notifyUserError(constants.userErrorZeroLimit());
                Log.debug(SqlRequestLauncherPresenter.class, "  new value for result limit is zero - abort change");
                this.view.setResultLimit(this.resultLimit);
            } else {
                Log.debug(SqlRequestLauncherPresenter.class, "  valid value for result limit - changed");
                this.resultLimit = resultLimitValue;
            }
        } catch (NumberFormatException e) {
            Log.debug(SqlRequestLauncherPresenter.class, "  new value for result limit is not a number - abort change");
            this.view.setResultLimit(this.resultLimit);
        }
    }

    /**
     * Display a notification that tells the user they made an invalid operation.
     * 
     * @param explain what the user did wrong
     */
    private void notifyUserError(final String explain) {
        final Notification errorNotification = new Notification(explain, Notification.Type.WARNING);
        notificationManager.showNotification(errorNotification);
    }

    private String getSqlRequestInput() {
        final String selection = getSelectedText();
        if (selection == null || "".equals(selection.trim())) {
            final String content = getEditorContent();
            return content;
        } else {
            return selection;
        }
    }

    /**
     * Return the selected content of the editor zone.
     * 
     * @return the selected content
     */
    private String getSelectedText() {
        return getEditor().getSelectedContent();
    }

    /**
     * Return the whole content of the editor zone.
     * 
     * @return the content of the editor
     */
    private String getEditorContent() {
        return getEditor().getEditorContent();
    }

    @Override
    public void executeRequested() {
        Log.info(SqlRequestLauncherPresenter.class, "Execution requested.");
        if (this.selectedDatasourceId == null) {
            final MessageWindow dialog = this.dialogFactory.createMessageWindow(this.constants.executeNoDatasourceTitle(),
                                                                                this.constants.executeNoDatasourceMessage(),
                                                                                null);
            dialog.inform();
            return;
        }
        if (this.executionMode == null) {
            final MessageWindow dialog = this.dialogFactory.createMessageWindow(this.constants.executeNoExecutionModeTitle(),
                                                                                this.constants.executeNoExecutionModeMessage(),
                                                                                null);
            dialog.inform();
            return;
        }
        DatabaseConfigurationDTO databaseConf = this.datasourceManager.getByName(this.selectedDatasourceId);
        String rawSql = getSqlRequestInput();
        if (rawSql != null) {
            rawSql = rawSql.trim();
            if (!"".equals(rawSql)) {
                try {
                    final Notification requestNotification = new Notification("Executing SQL request...",
                                                                              Notification.Status.PROGRESS);
                    final Long startRequestTime = System.currentTimeMillis();
                    AsyncRequestCallback<String> callback = new AsyncRequestCallback<String>(new StringUnmarshaller()) {

                        @Override
                        protected void onSuccess(final String result) {
                            final Long endRequestTime = System.currentTimeMillis();
                            final Long requestDuration = endRequestTime - startRequestTime;
                            Log.info(SqlRequestLauncherPresenter.class, "SQL request result received (" + requestDuration + "ms)");
                            requestNotification.setMessage("SQL request execution completed");
                            requestNotification.setStatus(Notification.Status.FINISHED);

                            final Long startJsonTime = System.currentTimeMillis();
                            final RequestResultGroupDTO resultDto = dtoFactory.createDtoFromJson(result,
                                                                                                 RequestResultGroupDTO.class);
                            final Long endJsonTime = System.currentTimeMillis();
                            final Long jsonDuration = endJsonTime - startJsonTime;
                            Log.info(SqlRequestLauncherPresenter.class, "Result converted from JSON(" + jsonDuration + "ms)");

                            final Long startDisplayTime = System.currentTimeMillis();
                            updateResultDisplay(resultDto);
                            final Long endDisplayTime = System.currentTimeMillis();
                            final Long displayDuration = endDisplayTime - startDisplayTime;
                            Log.info(SqlRequestLauncherPresenter.class, "Build display for SQL request result(" + displayDuration + "ms)");
                        }

                        @Override
                        protected void onFailure(final Throwable exception) {
                            Log.info(SqlRequestLauncherPresenter.class, "SQL request failure " + exception.getMessage());
                            GWT.log("Full exception :", exception);
                            requestNotification.setStatus(Notification.Status.FINISHED);
                            notificationManager.showNotification(new Notification("SQL request failed",
                                                                                  Type.ERROR));

                            updateResultDisplay(exception.getMessage());
                        }
                    };

                    notificationManager.showNotification(requestNotification);
                    datasourceClientService.executeSqlRequest(databaseConf,
                                                              this.resultLimit,
                                                              rawSql,
                                                              this.executionMode,
                                                              callback);
                } catch (final RequestException e) {
                    Log.error(SqlRequestLauncherPresenter.class,
                              "Exception on SQL request execution : " + e.getMessage());
                    notificationManager.showNotification(new Notification("Failed execution of SQL request",
                                                                          Type.ERROR));
                }
            } else {
                Window.alert("No SQL request");
            }
        } else {
            Window.alert("No SQL request");
        }
    }

    protected void updateResultDisplay(final String message) {
        Log.info(SqlRequestLauncherPresenter.class, "Printing request error message.");
        Label messageLabel = new Label(message);
        this.view.appendResult(messageLabel);
    }

    /**
     * Update the result zone to display the last SQL execution result.
     * 
     * @param resultDto the result data
     */
    protected void updateResultDisplay(final RequestResultGroupDTO resultDto) {
        Log.info(SqlRequestLauncherPresenter.class,
                 "Printing request results. (" + resultDto.getResults().size() + " individual results).");
        this.view.clearResultZone();

        for (final RequestResultDTO result : resultDto.getResults()) {
            switch (result.getResultType()) {
                case UpdateResultDTO.TYPE:
                    Log.info(SqlRequestLauncherPresenter.class, "Found one result of type 'update'.");
                    appendUpdateResult(result);
                    break;
                case SelectResultDTO.TYPE:
                    Log.info(SqlRequestLauncherPresenter.class, "Found one result of type 'select'.");
                    appendSelectResult(result);
                    break;
                case ExecutionErrorResultDTO.TYPE:
                    Log.info(SqlRequestLauncherPresenter.class, "Found one result of type 'error'.");
                    appendErrorReport(result);
                    break;
                default:
                    Log.error(SqlRequestLauncherPresenter.class, "unknown result type : "
                                                                 + result.getResultType());
                    this.view.appendResult(new Label("Result can't be displayed"));
            }
        }

        Log.info(SqlRequestLauncherPresenter.class, "All individual results are processed.");
    }

    /**
     * Append a result for an failed request to the result zone.
     * 
     * @param result the result data
     */
    private void appendErrorReport(final RequestResultDTO result) {
        final RequestResultHeader infoHeader = this.requestResultHeaderFactory.createRequestResultHeader(this, result.getOriginRequest());

        ResultItemBox resultItemBox = this.resultItemBoxFactory.createResultItemBox(infoHeader);

        final Label errorDisplay = new Label(Integer.toString(result.getSqlExecutionError().getErrorCode())
                                             + " - "
                                             + result.getSqlExecutionError().getErrorMessage());
        resultItemBox.addResultItem(errorDisplay);

        this.view.appendResult(resultItemBox);
    }

    /**
     * Append a result for an select request to the result zone.
     * 
     * @param result the result data
     */
    private void appendSelectResult(final RequestResultDTO result) {

        final ResultCellTable resultTable = new ResultCellTable(DEFAULT_RESULT_PAGE_SIZE, cellTableResources, constants);

        final RequestResultHeader infoHeader = this.requestResultHeaderFactory.createRequestResultHeaderWithExport(this, result);

        int i = 0;
        for (final String headerEntry : result.getHeaderLine()) {
            resultTable.addColumn(new FixedIndexTextColumn(i, ALIGN_LEFT),
                                  new AlignableColumnHeader(headerEntry, ALIGN_LEFT));
            i++;
        }

        new ListDataProvider<List<String>>(result.getResultLines()).addDataDisplay(resultTable);


        ResultItemBox resultItemBox = this.resultItemBoxFactory.createResultItemBox(infoHeader);

        resultItemBox.addResultItem(resultTable);

        Pager bottomPager = new Pager(false, true);
        bottomPager.setDisplay(resultTable);
        resultItemBox.setFooter(bottomPager);

        this.view.appendResult(resultItemBox);

    }

    /**
     * Append a result for an update request to the result zone.
     * 
     * @param result the result data
     */
    private void appendUpdateResult(final RequestResultDTO result) {
        final RequestResultHeader infoHeader = this.requestResultHeaderFactory.createRequestResultHeader(this,
                                                                                                         result.getOriginRequest());

        ResultItemBox resultItemBox = this.resultItemBoxFactory.createResultItemBox(infoHeader);

        final Label resultDisplay = new Label(this.constants.updateCountMessage(result.getUpdateCount()));
        resultItemBox.addResultItem(resultDisplay);

        this.view.appendResult(resultItemBox);
    }

    @Override
    public void onDatasourceListChange(final DatasourceListChangeEvent event) {
        this.setupDatasourceComponent();
    }

    @Override
    public void executionModeChanged(final MultipleRequestExecutionMode mode) {
        this.executionMode = mode;
    }

    @Override
    public boolean onClose() {
        boolean parentResult = super.onClose();
        final String editorFileId = getEditorInput().getFile().getId();
        this.editorDatasourceOracle.forgetEditor(editorFileId);
        return parentResult;
    }

    @Override
    public void triggerCsvExport(final RequestResultDTO requestResult, final RequestResultHeaderImpl origin) {
        final Notification requestNotification = new Notification("Generating CSV export of results...",
                                                                  Notification.Status.PROGRESS);
        this.notificationManager.showNotification(requestNotification);
        try {
            this.datasourceClientService.exportAsCsv(requestResult, new AsyncRequestCallback<String>(new StringUnmarshaller()) {

                @Override
                protected void onSuccess(final String result) {
                    Log.info(SqlRequestLauncherPresenter.class, "CSV export : success.");
                    requestNotification.setStatus(Notification.Status.FINISHED);
                    notificationManager.showNotification(new Notification("CSV export : success", Type.INFO));

                    origin.showCsvLink(result);
                }

                @Override
                protected void onFailure(final Throwable e) {
                    Log.error(SqlRequestLauncherPresenter.class, "Exception on CSV export : " + e.getMessage());
                    notificationManager.showNotification(new Notification("CSV export failure", Type.ERROR));
                }
            });
        } catch (final RequestException e) {
            Log.error(SqlRequestLauncherPresenter.class, "Exception on CSV export : " + e.getMessage());
            this.notificationManager.showNotification(new Notification("CSV export failure", Type.ERROR));
        }

    }

    @Override
    public void clearResults() {
        this.view.clearResultZone();
    }
}
