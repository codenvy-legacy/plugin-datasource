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
package com.codenvy.ide.ext.datasource;

import static com.codenvy.ide.api.ui.workspace.PartStackType.EDITING;

import com.codenvy.ide.api.extension.Extension;
import com.codenvy.ide.api.ui.workspace.WorkspaceAgent;
import com.codenvy.ide.ext.datasource.part.DatasourcePresenter;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
@Extension(title = "Datasource Extension", version = "1.0.0")
public class DatasourceExtension {
	public static boolean SHOW_ITEM = true;

	@Inject
	public DatasourceExtension(WorkspaceAgent workspaceAgent,
			DatasourcePresenter howToPresenter) {
		workspaceAgent.openPart(howToPresenter, EDITING);
	}
}