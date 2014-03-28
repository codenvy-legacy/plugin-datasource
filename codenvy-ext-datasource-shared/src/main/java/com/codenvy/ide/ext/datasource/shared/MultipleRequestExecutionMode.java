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
package com.codenvy.ide.ext.datasource.shared;

public enum MultipleRequestExecutionMode {

    /** All SQL requests are executed, continue after error. */
    ONE_BY_ONE,
    /** All SQL requests are executed, but stop at first error. */
    STOP_AT_FIRST_ERROR,
    /** All requests are executed or none. If one fails, stop and rollback. */
    TRANSACTIONAL
}
