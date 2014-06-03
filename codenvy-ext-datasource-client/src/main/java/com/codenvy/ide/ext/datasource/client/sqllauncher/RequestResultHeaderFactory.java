/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.ide.ext.datasource.client.sqllauncher;

import com.codenvy.ide.ext.datasource.client.sqllauncher.RequestResultHeaderImpl.RequestResultDelegate;
import com.codenvy.ide.ext.datasource.shared.request.RequestResultDTO;

/**
 * Factory for {@link RequestResultHeaderImpl}.
 * 
 * @author "Mickaël Leduque"
 */
public interface RequestResultHeaderFactory {

    /**
     * Creates an instance of {@link RequestResultHeaderImpl}.
     * 
     * @param delegate the action delegate
     * @return a {@link RequestResultDelegate}
     */
    RequestResultHeader createRequestResultHeader(RequestResultDelegate delegate, String query);

    /**
     * Creates an instance of {@link RequestResultHeaderImpl} with a CSV export button.
     * 
     * @param delegate the action delegate
     * @return a {@link RequestResultDelegate}
     */
    RequestResultHeader createRequestResultHeaderWithExport(RequestResultDelegate delegate, RequestResultDTO result);
}
