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

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

class FixedIndexTextColumn extends TextColumn<List<String>> {

    private final int columnIndex;

    public FixedIndexTextColumn(final int columnIndex, HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
        this.columnIndex = columnIndex;
        setHorizontalAlignment(alignment);
    }

    @Override
    public String getValue(final List<String> line) {
        return line.get(this.columnIndex);
    }


}
