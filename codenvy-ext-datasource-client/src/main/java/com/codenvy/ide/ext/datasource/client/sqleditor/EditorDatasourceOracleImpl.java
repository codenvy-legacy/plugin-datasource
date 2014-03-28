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
