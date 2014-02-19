/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2014] Codenvy, S.A.
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
package com.codenvy.ide.ext.datasource.client.sqleditor;

import java.util.HashMap;
import java.util.Map;

public class EditorDatasourceOracleImpl implements EditorDatasourceOracle {
    Map<String, String> fileDatasourcesMap = new HashMap<String, String>();

    @Override
    public String getSelectedDatasourceId(String editorInputFileId) {
        return fileDatasourcesMap.get(editorInputFileId);
    }

    @Override
    public void setSelectedDatasourceId(String editorInputFileId, String datasourceId) {
        fileDatasourcesMap.put(editorInputFileId, datasourceId);
    }
}
