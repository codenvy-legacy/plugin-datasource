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

import java.util.List;

import javax.validation.constraints.NotNull;

import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * {@link CellTable} for SQL request results display.
 * 
 * @author "Mickaël Leduque"
 */
public class ResultCellTable extends CellTable<List<String>> {

    @Inject
    public ResultCellTable(final @Assisted int pageSize,
                           final @NotNull CellTableResourcesQueryResults cellTableResources,
                           final @NotNull SqlRequestLauncherConstants constants) {
        super(pageSize, cellTableResources);

        final InlineLabel emptyWidget = new InlineLabel(constants.emptyResult());
        setEmptyTableWidget(emptyWidget);
        emptyWidget.setStyleName(cellTableResources.cellTableStyle().emptyTableWidget());

        addCellPreviewHandler(new CellPreviewEvent.Handler<List<String>>() {
            @Override
            public void onCellPreview(CellPreviewEvent<List<String>> event) {
                if ("click".equals(event.getNativeEvent().getType())) {
                    TableCellElement cellElement = getRowElement(event.getIndex()).getCells().getItem(event.getColumn());
                    cellElement.setTitle(cellElement.getInnerText());
                }
            }
        });
    }

}
