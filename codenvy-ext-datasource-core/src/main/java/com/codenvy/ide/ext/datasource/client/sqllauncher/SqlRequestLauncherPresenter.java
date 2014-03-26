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
import com.codenvy.ide.ext.datasource.client.DatabaseInfoStore;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.client.DatasourceManager;
import com.codenvy.ide.ext.datasource.client.DatasourceUiResources;
import com.codenvy.ide.ext.datasource.client.MetadataNotificationConstants;
import com.codenvy.ide.ext.datasource.client.common.AlignableColumnHeader;
import com.codenvy.ide.ext.datasource.client.common.ReadableContentTextEditor;
import com.codenvy.ide.ext.datasource.client.common.TextEditorPartAdapter;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedEvent;
import com.codenvy.ide.ext.datasource.client.events.DatasourceCreatedHandler;
import com.codenvy.ide.ext.datasource.client.explorer.DatasourceExplorerPartPresenter;
import com.codenvy.ide.ext.datasource.client.sqleditor.EditorDatasourceOracle;
import com.codenvy.ide.ext.datasource.client.sqleditor.SqlEditorProvider;
import com.codenvy.ide.ext.datasource.client.sqllauncher.RequestResultHeader.RequestResultDelegate;
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
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;


public class SqlRequestLauncherPresenter extends TextEditorPartAdapter<ReadableContentTextEditor> implements
                                                                                                 SqlRequestLauncherView.ActionDelegate,
                                                                                                 DatasourceCreatedHandler,
                                                                                                 RequestResultDelegate {

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
    private final MetadataNotificationConstants       notificationConstants;
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
    private final DatasourceUiResources               datasourceUiResources;

    @Inject
    public SqlRequestLauncherPresenter(final @NotNull SqlRequestLauncherView view,
                                       final @NotNull SqlRequestLauncherConstants constants,
                                       final @NotNull MetadataNotificationConstants notificationConstants,
                                       final @NotNull PreferencesManager preferencesManager,
                                       final @NotNull SqlEditorProvider sqlEditorProvider,
                                       final @NotNull DatasourceClientService service,
                                       final @NotNull DatabaseInfoStore databaseInfoStore,
                                       final @NotNull NotificationManager notificationManager,
                                       final @NotNull DatasourceManager datasourceManager,
                                       final @NotNull EditorDatasourceOracle editorDatasourceOracle,
                                       final @NotNull EventBus eventBus,
                                       final @NotNull DtoFactory dtoFactory,
                                       final @NotNull WorkspaceAgent workspaceAgent,
                                       final @NotNull CellTableResourcesQueryResults cellTableResources,
                                       final @NotNull DatasourceUiResources datasourceUiResources) {
        super(sqlEditorProvider.getEditor(), workspaceAgent, eventBus);
        this.databaseInfoStore = databaseInfoStore;
        this.editorDatasourceOracle = editorDatasourceOracle;
        Log.info(SqlRequestLauncherPresenter.class, "New instance of SQL request launcher presenter resquested.");
        this.view = view;
        this.view.setDelegate(this);
        this.constants = constants;
        this.notificationConstants = notificationConstants;
        this.dtoFactory = dtoFactory;

        this.datasourceClientService = service;
        this.notificationManager = notificationManager;
        this.datasourceManager = datasourceManager;
        this.cellTableResources = cellTableResources;
        this.datasourceUiResources = datasourceUiResources;

        setupResultLimit(preferencesManager);
        setupExecutionMode(preferencesManager);

        // register for datasource creation events
        eventBus.addHandler(DatasourceCreatedEvent.getType(), this);
    }

    private void setupResultLimit(final PreferencesManager preferencesManager) {
        final String prefRequestLimit = preferencesManager.getValue(PREFERENCE_KEY_DEFAULT_REQUEST_LIMIT);

        if (prefRequestLimit != null) {
            try {
                int prefValue = Integer.valueOf(prefRequestLimit);
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
    public void datasourceChanged(final String newDataSourceId) {
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
            try {
                final Notification fetchDatabaseNotification = new Notification(notificationConstants.notificationFetchStart(),
                                                                                Notification.Status.PROGRESS);
                notificationManager.showNotification(fetchDatabaseNotification);
                datasourceClientService.fetchDatabaseInfo(datasourceManager.getByName(newDataSourceId),
                                                          new AsyncRequestCallback<String>(new StringUnmarshaller()) {
                                                              @Override
                                                              protected void onSuccess(String result) {
                                                                  DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                                                                      DatabaseDTO.class);
                                                                  fetchDatabaseNotification.setMessage(notificationConstants.notificationFetchSuccess());
                                                                  fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                                                                  notificationManager.showNotification(fetchDatabaseNotification);
                                                                  databaseInfoStore.setDatabaseInfo(newDataSourceId, database);
                                                              }

                                                              @Override
                                                              protected void onFailure(Throwable exception) {
                                                                  fetchDatabaseNotification.setStatus(Notification.Status.FINISHED);
                                                                  notificationManager.showNotification(new Notification(
                                                                                                                        notificationConstants.notificationFetchFailure(),
                                                                                                                        Type.ERROR));
                                                              }
                                                          }

                                       );
            } catch (RequestException e) {
                Log.error(DatasourceExplorerPartPresenter.class,
                          "Exception on database info fetch : " + e.getMessage());
                notificationManager.showNotification(new Notification(notificationConstants.notificationFetchFailure(),
                                                                      Type.ERROR));
            }
        }
    }

    @Override
    public void resultLimitChanged(final String newResultLimitString) {
        Log.info(SqlRequestLauncherPresenter.class, "Attempt to change result limit to " + newResultLimitString);
        int resultLimitValue;
        try {
            resultLimitValue = Integer.parseInt(newResultLimitString);
            if (resultLimit < 0) {
                Log.debug(SqlRequestLauncherPresenter.class, "  new value for result limit is negative - abort change");
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
            Window.alert("No datasource selected");
            return;
        }
        if (this.executionMode == null) {
            Window.alert("No execute mode selected");
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
                    AsyncRequestCallback<String> callback = new AsyncRequestCallback<String>(new StringUnmarshaller()) {

                        @Override
                        protected void onSuccess(final String result) {
                            Log.info(SqlRequestLauncherPresenter.class, "SQL request result received.");
                            requestNotification.setMessage("SQL request execution completed");
                            requestNotification.setStatus(Notification.Status.FINISHED);
                            final RequestResultGroupDTO resultDto = dtoFactory.createDtoFromJson(result,
                                                                                                 RequestResultGroupDTO.class);
                            Log.info(SqlRequestLauncherPresenter.class, "JSON->dto conversion OK - result :"
                                                                        + resultDto);
                            updateResultDisplay(resultDto);
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
        final RequestResultHeader infoHeader = buildErrorHeader(result.getOriginRequest());
        this.view.appendResult(infoHeader, new Label(Integer.toString(result.getSqlExecutionError().getErrorCode())
                                                     + " - "
                                                     + result.getSqlExecutionError().getErrorMessage()));
    }

    /**
     * Append a result for an select request to the result zone.
     * 
     * @param result the result data
     */
    private void appendSelectResult(final RequestResultDTO result) {

        final CellTable<List<String>> resultTable = new CellTable<List<String>>(result.getResultLines().size(), cellTableResources);

        final RequestResultHeader infoHeader = buildResultHeader(result, constants.exportCsvLabel());

        int i = 0;
        for (final String headerEntry : result.getHeaderLine()) {
            resultTable.addColumn(new FixedIndexTextColumn(i, ALIGN_LEFT),
                                  new AlignableColumnHeader(headerEntry, ALIGN_LEFT));
            i++;
        }


        resultTable.addCellPreviewHandler(new CellPreviewEvent.Handler<List<String>>() {
            @Override
            public void onCellPreview(CellPreviewEvent<List<String>> event) {
                if ("click".equals(event.getNativeEvent().getType())) {
                    TableCellElement cellElement = resultTable.getRowElement(event.getIndex()).getCells().getItem(event.getColumn());
                    cellElement.setTitle(cellElement.getInnerText());
                }
            }
        });

        new ListDataProvider<List<String>>(result.getResultLines()).addDataDisplay(resultTable);
        this.view.appendResult(infoHeader, resultTable);

    }

    /**
     * Append a result for an update request to the result zone.
     * 
     * @param result the result data
     */
    private void appendUpdateResult(final RequestResultDTO result) {
        final RequestResultHeader infoHeader = buildResultHeader(result, null);
        this.view.appendResult(infoHeader, new Label(this.constants.updateCountMessage(result.getUpdateCount())));
    }

    /**
     * Creates a result header for SQL requests result that succeeded.
     * 
     * @param originRequest the SQL request
     * @return a result header
     */
    private RequestResultHeader buildResultHeader(final RequestResultDTO requestResult, final String text) {
        final RequestResultHeader result = new RequestResultHeader(this.datasourceUiResources.datasourceUiCSS(), this);
        result.setInfoHeaderTitle(constants.queryResultsTitle());
        result.setRequestReminder(requestResult.getOriginRequest());
        if (text != null) {
            result.withExportButton(requestResult, text);
        }
        return result.prepare();
    }

    /**
     * Creates a result header for SQL requests result that failed.
     * 
     * @param originRequest the SQL request
     * @return a result header
     */
    private RequestResultHeader buildErrorHeader(final String originRequest) {
        final RequestResultHeader result = new RequestResultHeader(this.datasourceUiResources.datasourceUiCSS(), this);
        result.setInfoHeaderTitle(constants.queryErrorTitle());
        result.setRequestReminder(originRequest);

        return result.prepare();
    }

    @Override
    public void onDatasourceCreated(final DatasourceCreatedEvent event) {
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
    public void triggerCsvExport(final RequestResultDTO requestResult, final RequestResultHeader origin) {
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
        } catch (RequestException e) {
            Log.error(SqlRequestLauncherPresenter.class, "Exception on CSV export : " + e.getMessage());
            this.notificationManager.showNotification(new Notification("CSV export failure", Type.ERROR));
        }

    }
}
