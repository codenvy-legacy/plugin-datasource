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
package com.codenvy.ide.ext.datasource.client.newdatasource.view;

import java.util.HashMap;
import java.util.Map;

import com.codenvy.ide.api.mvp.Presenter;
import com.codenvy.ide.ui.window.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class NewDatasourceWizardHeadViewImpl extends Window implements NewDatasourceWizardHeadView {

    private static NewDatasourceWizardHeadViewImplUiBinder ourUiBinder = GWT.create(NewDatasourceWizardHeadViewImplUiBinder.class);
    @UiField
    TextBox                                                datasourceName;
    @UiField
    SimplePanel                                            datasourcesPanel;
    @UiField
    SimplePanel                                            settingsPanel;
    @UiField
    Style                                                  style;
    @UiField
    Button                                                 saveButton;
    private ActionDelegate                                 delegate;
    private Map<Presenter, Widget>                         pageCache   = new HashMap<>();

    @Inject
    public NewDatasourceWizardHeadViewImpl(com.codenvy.ide.Resources resources) {
        super(true);
        setTitle("Datasource Configuration");
        ensureDebugId("newdatasource");
        setWidget(ourUiBinder.createAndBindUi(this));
    }

    @UiHandler("datasourceName")
    void onDatasourceNameChanged(KeyUpEvent event) {
        delegate.datasourceNameChanged(datasourceName.getText());
    }

    @UiHandler("saveButton")
    void saveClick(ClickEvent event) {
        delegate.onSaveClicked();
    }

    @Override
    public void showPage(Presenter presenter, String place) {
        switch (place) {
            case "categories":
                datasourcesPanel.clear();
                break;
            case "settings":
                settingsPanel.clear();
                break;
        }
        if (pageCache.containsKey(presenter)) {
            switch (place) {
                case "categories":
                    datasourcesPanel.add(pageCache.get(presenter));
                    break;
                case "settings":
                    settingsPanel.add(pageCache.get(presenter));
                    break;
            }
        } else {
            switch (place) {
                case "categories":
                    presenter.go(datasourcesPanel);
                    pageCache.put(presenter, datasourcesPanel.getWidget());
                    break;
                case "settings":
                    presenter.go(settingsPanel);
                    pageCache.put(presenter, settingsPanel.getWidget());
                    break;
            }
        }
    }

    @Override
    public void cleanPage(String place) {
        switch (place) {
            case "categories":
                datasourcesPanel.clear();
                break;
            case "settings":
                settingsPanel.clear();
                break;
        }
    }

    @Override
    public void showDialog() {
        show();
    }

    @Override
    public void setEnabledAnimation(boolean enabled) {

    }

    @Override
    public void close() {
        hide();
        cleanPage("categories");
        cleanPage("settings");
        pageCache.clear();
    }

    @Override
    public void setFinishButtonEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
    }

    @Override
    public void reset() {
        datasourceName.setText("");
        changeEnabledState(true);
    }

    @Override
    public void enableInput() {
        changeEnabledState(true);
    }

    @Override
    public void disableInput() {
        changeEnabledState(false);
    }

    @Override
    public void setName(String name) {
        datasourceName.setValue(name, true);
    }

    @Override
    public void removeNameError() {
        datasourceName.removeStyleName(style.inputError());
    }

    @Override
    public void showNameError() {
        datasourceName.addStyleName(style.inputError());
    }

    private void changeEnabledState(boolean enabled) {
        datasourceName.setEnabled(enabled);
    }

    @Override
    public void setDelegate(ActionDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public Widget asWidget() {
        return null;
    }

    @Override
    protected void onClose() {
        pageCache.clear();
        delegate.onCancelClicked();
    }

    interface Style extends CssResource {

        String datasource();

        String datasourceNamePosition();

        String topPanel();

        String namePanel();

        String namePanelRight();

        String rootPanel();

        String tab();

        String blueButton();

        String disabled();

        String inputError();
    }

    interface NewDatasourceWizardHeadViewImplUiBinder
                                                     extends UiBinder<FlowPanel, NewDatasourceWizardHeadViewImpl> {
    }
}
