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

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Interface to the request result header.
 * 
 * @author "Mickaël Leduque"
 */
public interface RequestResultHeader extends IsWidget {
    /**
     * Sets the delegate that will handle actions.
     * 
     * @param delegate the delegate
     */
    void setOpenCloseDelegate(OpenCloseDelegate delegate);

    /**
     * Change view to reflext opened/closed state.
     * 
     * @param open state
     */
    void setOpen(boolean open);

    /**
     * The interface for the action delegate.
     * 
     * @author "Mickaël Leduque"
     */
    public interface OpenCloseDelegate {
        /** Handles the open/close action. */
        void onOpenClose();
    }
}
