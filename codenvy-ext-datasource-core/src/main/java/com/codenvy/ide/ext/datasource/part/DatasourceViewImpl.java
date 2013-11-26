/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 * [2012] - [2013] Codenvy, S.A. 
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
package com.codenvy.ide.ext.datasource.part;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DatasourceViewImpl extends Composite implements DatasourceView {
	interface TutorialHowToViewImplUiBinder extends
			UiBinder<Widget, DatasourceViewImpl> {
	}

	private static TutorialHowToViewImplUiBinder ourUiBinder = GWT
			.create(TutorialHowToViewImplUiBinder.class);

	@Inject
	public DatasourceViewImpl() {
		initWidget(ourUiBinder.createAndBindUi(this));
	}

	/** {@inheritDoc} */
	@Override
	public void setDelegate(ActionDelegate delegate) {
		// do nothing
	}
}