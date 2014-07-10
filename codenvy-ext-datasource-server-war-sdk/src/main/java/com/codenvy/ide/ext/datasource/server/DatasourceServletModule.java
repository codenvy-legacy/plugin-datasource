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
package com.codenvy.ide.ext.datasource.server;

import javax.inject.Singleton;

import org.everrest.guice.servlet.GuiceEverrestServlet;
import com.codenvy.ide.env.SingleEnvironmentFilter;
import com.codenvy.inject.DynaModule;
import com.google.inject.servlet.ServletModule;

/**
 * {@link ServletModule} definition for the server-side part of the datasource plugin.
 *
 * @author "Mickaël Leduque"
 */
@DynaModule
public class DatasourceServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        bind(SingleEnvironmentFilter.class).in(Singleton.class);
        filter("/*").through(SingleEnvironmentFilter.class);
        serve("/*").with(GuiceEverrestServlet.class);
    }
}
