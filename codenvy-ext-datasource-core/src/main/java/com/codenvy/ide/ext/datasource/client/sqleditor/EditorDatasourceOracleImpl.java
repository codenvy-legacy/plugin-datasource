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
    private Map<String, String> fileDatasourcesMap;

    @Override
    public String getSelectedDatasourceId(final String editorInputFileId) {
        if (this.fileDatasourcesMap == null) {
            return null;
        }
        return fileDatasourcesMap.get(editorInputFileId);
    }

    @Override
    public void setSelectedDatasourceId(final String editorInputFileId, final String datasourceId) {
        if (this.fileDatasourcesMap == null) {
            this.fileDatasourcesMap = new HashMap<String, String>();
        }
        fileDatasourcesMap.put(editorInputFileId, datasourceId);
    }

    @Override
    public void forgetEditor(final String editorInputFileId) {
        if (this.fileDatasourcesMap != null) {
            this.fileDatasourcesMap.remove(editorInputFileId);
        }
    }
}
