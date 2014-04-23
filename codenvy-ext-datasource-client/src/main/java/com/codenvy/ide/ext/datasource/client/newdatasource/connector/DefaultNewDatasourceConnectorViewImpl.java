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
package com.codenvy.ide.ext.datasource.client.newdatasource.connector;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;

import javax.annotation.Nullable;
import java.util.Collection;


public class DefaultNewDatasourceConnectorViewImpl extends Composite
                                                                    implements DefaultNewDatasourceConnectorView {
    private String saveProjectListSelection;

    interface NewDatasourceViewImplUiBinder extends UiBinder<Widget, DefaultNewDatasourceConnectorViewImpl> {
    }

    @UiField
    TextBox                hostField;

    @UiField
    TextBox                portField;

    @UiField
    TextBox                dbName;

    @UiField
    TextBox                usernameField;

    @UiField
    PasswordTextBox        passwordField;

    @UiField
    Button                 testConnectionButton;

    @UiField
    CheckBox               useSSL;

    @UiField
    CheckBox               verifyServerCertificate;

    @UiField
    ListBox                projectList;

    private ActionDelegate delegate;
    private final ProjectServiceClient projectService;
    private final Collection<String> projectNames = null;


    @Inject
    public DefaultNewDatasourceConnectorViewImpl(NewDatasourceViewImplUiBinder uiBinder,
                                                 @Nullable ProjectServiceClient projectService) {
        initWidget(uiBinder.createAndBindUi(this));
        hostField.setText("localhost");
        this.projectService = projectService;
        projectNames.add("User Preferences");
        projectNames.add("Project x");
        projectNames.add("Project y");
        projectNames.add("Project z");
        setProjectList(projectNames);

    }

    public void setDelegate(DefaultNewDatasourceConnectorView.ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getDatabaseName() {
        return dbName.getText();
    }

    @Override
    public String getHostname() {
        return hostField.getText();
    }

    @Override
    public int getPort() {
        return Integer.parseInt(portField.getText());
    }

    @Override
    public String getUsername() {
        return usernameField.getText();
    }

    @Override
    public String getPassword() {
        return passwordField.getText();
    }

    @Override
    public void setPort(int port) {
        portField.setText(Integer.toString(port));
    }

    @Override
    public boolean getUseSSL() {
        if (useSSL.getValue() != null) {
            return useSSL.getValue();
        } else {
            return false;
        }
    }

    @Override
    public boolean getVerifyServerCertificate() {
        if (verifyServerCertificate.getValue() != null) {
            return verifyServerCertificate.getValue();
        } else {
            return false;
        }
    }

    @Override
    public void setDatabaseName(final String databaseName) {
        this.dbName.setValue(databaseName);
    }

    @Override
    public void setHostName(final String hostName) {
        this.hostField.setValue(hostName);
    }

    @Override
    public void setUseSSL(final boolean useSSL) {
        this.useSSL.setValue(useSSL);
    }

    @Override
    public void setVerifyServerCertificate(final boolean verifyServerCertificate) {
        this.verifyServerCertificate.setValue(verifyServerCertificate);
    }

    @Override
    public void setUsername(final String username) {
        this.usernameField.setValue(username);
    }

    @Override
    public void setPassword(final String password) {
        this.passwordField.setValue(password);
    }

    @UiHandler("testConnectionButton")
    void handleClick(ClickEvent e) {
        delegate.onClickTestConnectionButton();
    }

    /**
     * Fills the project list widget with the known project names.
     */
    /* private void setupProjectList() {
        final Collection<String> projectNames = null;
        if (this.projectService != null) {
            this.projectService.getProjects(new AsyncRequestCallback<Array<ProjectReference>>() {
                @Override
                protected void onSuccess(Array<ProjectReference> projectReferenceArray) {
                   for (ProjectReference projectReference: projectReferenceArray.asIterable()){
                       projectNames.add(projectReference.getName());
                   }
                }

                @Override
                protected void onFailure(Throwable throwable) {
                  System.out.println("failure loading projects names");
                }
            });
        }
        this.setProjectList(projectNames);
    }*/

    @Override
    public void setProjectList(final Collection<String> projectNames) {
        projectList.clear();
        if (projectNames != null) {
            for (String dsIds : projectNames) {
                projectList.addItem(dsIds);
            }
        }
    }

   /* public String getSelectedId() {
        int index = this.projectList.getSelectedIndex();
        if (index != -1) {
            return this.projectList.getValue(index);
        } else {
            return null;
        }
    }
   */
    @UiHandler("projectList")
    void onProjectListChange(final ChangeEvent event) {
        int newSelection = this.projectList.getSelectedIndex();
        this.projectList.setSelectedIndex(newSelection);
    }

}
