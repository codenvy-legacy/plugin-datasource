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

package com.codenvy.ide.ext.datasource.client.common.confirmwindow;

import com.google.gwt.i18n.client.Messages;

/**
 * I18n messages interface for the confirmation window.
 * 
 * @author "Mickaël Leduque"
 */
public interface ConfirmWindowMessages extends Messages {

    @DefaultMessage("OK")
    String okButtonText();

    @DefaultMessage("Cancel")
    String cancelButtonText();
}