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
package com.codenvy.ide.ext.datasource.client.explorer;

import java.util.Collection;

import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ext.datasource.shared.ColumnDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.ext.datasource.shared.DatabaseMetadataEntityDTO;
import com.codenvy.ide.ext.datasource.shared.SchemaDTO;
import com.codenvy.ide.ext.datasource.shared.TableDTO;
import com.codenvy.ide.ui.tree.NodeDataAdapter;
import com.codenvy.ide.ui.tree.TreeNodeElement;

public class DatabaseMetadataEntityDTODataAdapter implements
                                                 NodeDataAdapter<DatabaseMetadataEntityDTO> {

    @Override
    public int compare(DatabaseMetadataEntityDTO a, DatabaseMetadataEntityDTO b) {
        if(a == null){
            return 0;
        }
        if(b == null){
            return 1;
        }
        return a.getName().compareTo(b.getName());
    }

    @Override
    public boolean hasChildren(DatabaseMetadataEntityDTO data) {
        if (data instanceof ColumnDTO) {
            return false;
        }
        return true;
    }

    @Override
    public Array<DatabaseMetadataEntityDTO> getChildren(
                                                        DatabaseMetadataEntityDTO data) {
        if (data instanceof ColumnDTO) {
            return null;
        }

        Array<DatabaseMetadataEntityDTO> children = Collections
                                                               .<DatabaseMetadataEntityDTO> createArray();
        if (data instanceof DatabaseDTO) {
            Collection<SchemaDTO> schemas = ((DatabaseDTO)data).getSchemas()
                                                               .values();
            for (SchemaDTO schemaDTO : schemas) {
                children.add(schemaDTO);
            }
        }
        if (data instanceof SchemaDTO) {
            Collection<TableDTO> tables = ((SchemaDTO)data).getTables()
                                                           .values();
            for (TableDTO tableDTO : tables) {
                children.add(tableDTO);
            }
        }
        if (data instanceof TableDTO) {
            Collection<ColumnDTO> columns = ((TableDTO)data).getColumns()
                                                            .values();
            for (ColumnDTO columnDTO : columns) {
                children.add(columnDTO);
            }
        }

        return children;
    }

    @Override
    public String getNodeId(DatabaseMetadataEntityDTO data) {
        return data.getName(); // TODO make it something unique
    }

    @Override
    public String getNodeName(DatabaseMetadataEntityDTO data) {
        return data.getName();
    }

    @Override
    public DatabaseMetadataEntityDTO getParent(DatabaseMetadataEntityDTO data) {
        return null;
    }

    @Override
    public TreeNodeElement<DatabaseMetadataEntityDTO> getRenderedTreeNode(
                                                                          DatabaseMetadataEntityDTO data) {
        return null;
    }

    @Override
    public void setNodeName(DatabaseMetadataEntityDTO data, String name) {

    }

    @Override
    public void setRenderedTreeNode(DatabaseMetadataEntityDTO data,
                                    TreeNodeElement<DatabaseMetadataEntityDTO> renderedNode) {
    }

    @Override
    public DatabaseMetadataEntityDTO getDragDropTarget(
                                                       DatabaseMetadataEntityDTO data) {
        return null;
    }

    @Override
    public Array<String> getNodePath(DatabaseMetadataEntityDTO data) {
        return null;
    }

    @Override
    public DatabaseMetadataEntityDTO getNodeByPath(
                                                   DatabaseMetadataEntityDTO root, Array<String> relativeNodePath) {
        return null;
    }

}
