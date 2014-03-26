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
package com.codenvy.ide.ext.datasource.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codenvy.dto.server.DtoFactory;
import com.codenvy.ide.ext.datasource.shared.MultipleRequestExecutionMode;
import com.codenvy.ide.ext.datasource.shared.RequestParameterDTO;
import com.codenvy.ide.ext.datasource.shared.exception.DatabaseDefinitionException;
import com.codenvy.ide.ext.datasource.shared.request.ExecutionErrorResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultGroupDTO;
import com.codenvy.ide.ext.datasource.shared.request.SelectResultDTO;
import com.codenvy.ide.ext.datasource.shared.request.SqlExecutionError;
import com.codenvy.ide.ext.datasource.shared.request.UpdateResultDTO;
import com.google.common.base.Splitter;

public class SqlRequestService {

    private static final Logger                      LOG                   = LoggerFactory.getLogger(SqlRequestService.class);

    private static final String                      SQL_REQUEST_DELIMITER = ";";
    private static final Splitter                    SQL_REQUEST_SPLITTER  = Splitter.on(SQL_REQUEST_DELIMITER)
                                                                                     .omitEmptyStrings()
                                                                                     .trimResults();

    public static final MultipleRequestExecutionMode DEFAULT_MODE          = MultipleRequestExecutionMode.ONE_BY_ONE;

    public RequestResultGroupDTO executeSqlRequest(final RequestParameterDTO requestParameter,
                                                   final Connection connection,
                                                   final MultipleRequestExecutionMode mode) throws SQLException,
                                                                                           DatabaseDefinitionException {

        LOG.info("Execution request ; parameter : {}", requestParameter);
        final String agglutinatedRequests = requestParameter.getSqlRequest();
        Iterable<String> requests = SQL_REQUEST_SPLITTER.split(agglutinatedRequests); // TODO allow ; inside /* ... */ or '...'

        // prepare result dto
        final RequestResultGroupDTO resultGroup = DtoFactory.getInstance().createDto(RequestResultGroupDTO.class);
        final List<RequestResultDTO> resultList = new ArrayList<>();
        resultGroup.setResults(resultList);

        boolean terminate = false;
        for (final String request : requests) {
            try (final Statement statement = connection.createStatement();) {
                statement.setMaxRows(requestParameter.getResultLimit());
                resultList.addAll(processSingleRequest(request, statement));
            } catch (final SQLException e) {
                final ExecutionErrorResultDTO errorDto = DtoFactory.getInstance().createDto(ExecutionErrorResultDTO.class);
                final SqlExecutionError sqlError = DtoFactory.getInstance().createDto(SqlExecutionError.class);
                errorDto.setSqlExecutionError(sqlError);
                errorDto.setOriginRequest(request);
                sqlError.withErrorCode(e.getErrorCode())
                        .withErrorMessage(e.getMessage());
                resultList.add(errorDto);

                switch (mode) {
                    case STOP_AT_FIRST_ERROR:
                        terminate = true;
                        break;
                    case TRANSACTIONAL:
                        throw new RuntimeException("transaction execution mode is not impemented");
                    default:
                        break;
                }
            }
            if (terminate) {
                break;
            }
        }

        return resultGroup;
    }

    private List<RequestResultDTO> processSingleRequest(final String request, final Statement statement) throws SQLException {
        boolean returnsRows = statement.execute(request);
        LOG.info("Request executed successfully");

        ResultSet resultSet = statement.getResultSet();
        int count = statement.getUpdateCount();

        List<RequestResultDTO> resultList = new ArrayList<>();
        while (resultSet != null || count != -1) {
            LOG.info("New result returned by request :");

            if (count != -1) {
                LOG.info("   is an update count");
                final UpdateResultDTO result = DtoFactory.getInstance().createDto(UpdateResultDTO.class);
                resultList.add(result);
                result.withResultType(UpdateResultDTO.TYPE)
                      .withUpdateCount(count)
                      .withOriginRequest(request);
            } else {
                LOG.info("   is a result set");
                final SelectResultDTO result = DtoFactory.getInstance().createDto(SelectResultDTO.class);
                result.withResultType(SelectResultDTO.TYPE)
                      .withOriginRequest(request);
                resultList.add(result);

                final ResultSetMetaData metadata = resultSet.getMetaData();
                final int columnCount = metadata.getColumnCount();

                // header : column names
                final List<String> columnNames = new ArrayList<>();
                for (int i = 1; i < columnCount + 1; i++) {
                    columnNames.add(metadata.getColumnLabel(i));
                }
                result.setHeaderLine(columnNames);

                final List<List<String>> lines = new ArrayList<>();

                // result : actual data
                while (resultSet.next()) {
                    final List<String> line = new ArrayList<>();
                    for (int i = 1; i < columnCount + 1; i++) {
                        line.add(resultSet.getString(i));
                    }
                    lines.add(line);
                }
                result.setResultLines(lines);
            }

            // continue the loop - next result

            // getMoreResult should close it, but just to remove the warning
            if (resultSet != null) {
                resultSet.close();
            }
            boolean moreResults = statement.getMoreResults();
            resultSet = statement.getResultSet();
            count = statement.getUpdateCount();
        }
        return resultList;
    }
}
