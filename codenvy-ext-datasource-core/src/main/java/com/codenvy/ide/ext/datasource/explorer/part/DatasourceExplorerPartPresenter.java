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
package com.codenvy.ide.ext.datasource.explorer.part;

import static com.codenvy.ide.api.notification.Notification.Type.ERROR;
import static com.codenvy.ide.api.notification.Notification.Type.INFO;

import com.codenvy.ide.annotations.NotNull;
import com.codenvy.ide.api.event.ProjectActionEvent;
import com.codenvy.ide.api.event.ProjectActionHandler;
import com.codenvy.ide.api.notification.Notification;
import com.codenvy.ide.api.notification.NotificationManager;
import com.codenvy.ide.api.parts.base.BasePresenter;
import com.codenvy.ide.api.resources.FileEvent;
import com.codenvy.ide.api.resources.FileEvent.FileOperation;
import com.codenvy.ide.api.resources.ResourceProvider;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.ext.datasource.client.DatasourceClientService;
import com.codenvy.ide.ext.datasource.shared.DatabaseDTO;
import com.codenvy.ide.resources.model.File;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Resource;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.StringUnmarshaller;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Project Explorer display Project Model in a dedicated Part (view).
 * 
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
@Singleton
public class DatasourceExplorerPartPresenter extends BasePresenter implements
                                                                  DatasourceExplorerView.ActionDelegate, DatasourceExplorerPart {
    protected DatasourceExplorerView  view;
    protected EventBus                eventBus;
    private ResourceProvider          resourceProvider;
    protected DatasourceClientService service;
    protected DtoFactory              dtoFactory;
    protected NotificationManager     notificationManager;

    /**
     * Instantiates the ProjectExplorer Presenter
     * 
     * @param view
     * @param eventBus
     * @param resources
     * @param resourceProvider
     * @param contextMenuPresenter
     */
    @Inject
    public DatasourceExplorerPartPresenter(DatasourceExplorerView view,
                                           EventBus eventBus, DatasourceClientService service,
                                           DtoFactory dtoFactory, NotificationManager notificationManager) {
        this.view = view;
        this.eventBus = eventBus;
        this.service = service;
        this.dtoFactory = dtoFactory;
        this.notificationManager = notificationManager;
        this.view.setTitle("DataSource Explorer");
        bind();
    }

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
    }

    /**
     * Sets content.
     * 
     * @param resource
     */
    public void setContent() {
        try {
            service.fetchDatabase(new AsyncRequestCallback<String>(
                                                                   new StringUnmarshaller()) {
                @Override
                protected void onSuccess(String result) {
                    DatabaseDTO database = dtoFactory.createDtoFromJson(result,
                                                                        DatabaseDTO.class);
                    notificationManager.showNotification(new Notification(
                                                                          "Success getting database information", INFO));
                    view.setItems(database);

                }

                @Override
                protected void onFailure(Throwable exception) {
                    notificationManager.showNotification(new Notification(
                                                                          "Failed : " + exception.getMessage(), ERROR));
                }

            });

        } catch (RequestException e1) {
            String errorMassage = e1.getMessage() != null ? e1.getMessage()
                : "Failing getting database DTO";
            Notification notification = new Notification(errorMassage, ERROR);
            notificationManager.showNotification(notification);
        }
    }

    /** Adds behavior to view components */
    protected void bind() {
        view.setDelegate(this);
        eventBus.addHandler(ProjectActionEvent.TYPE,
                            new ProjectActionHandler() {
                                @Override
                                public void onProjectOpened(ProjectActionEvent event) {
                                    setContent();
                                }

                                @Override
                                public void onProjectDescriptionChanged(
                                                                        ProjectActionEvent event) {
                                }

                                @Override
                                public void onProjectClosed(ProjectActionEvent event) {
                                }
                            });

        // eventBus.addHandler(ResourceChangedEvent.TYPE, new
        // ResourceChangedHandler() {
        // @Override
        // public void onResourceRenamed(ResourceChangedEvent event) {
        // // TODO handle it
        // }
        //
        // @Override
        // public void onResourceMoved(ResourceChangedEvent event) {
        // // TODO handle it
        // }
        //
        // @Override
        // public void onResourceDeleted(ResourceChangedEvent event) {
        // setContent(event.getResource().getProject().getParent());
        // }
        //
        // @Override
        // public void onResourceCreated(ResourceChangedEvent event) {
        // setContent(event.getResource().getProject().getParent());
        // }
        // });
    }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return "Datasource Explorer";
    }

    /** {@inheritDoc} */
    @Override
    public ImageResource getTitleImage() {
        return null;// resources.projectExplorer();
    }

    /** {@inheritDoc} */
    @Override
    public String getTitleToolTip() {
        return "This View helps you to do basic operation with your projects. Following features are currently available:"
               + "\n\t- view project's tree"
               + "\n\t- select and open project's file";
    }

    @Override
    public void onResourceSelected(@NotNull Resource resource) {
        setSelection(new Selection<Resource>(resource));
    }

    /** {@inheritDoc} */
    @Override
    public void onResourceAction(@NotNull Resource resource) {
        // open file
        if (resource.isFile()) {
            eventBus.fireEvent(new FileEvent((File)resource,
                                             FileOperation.OPEN));
        }
        // open project
        if (resource.getResourceType().equals(Project.TYPE)
            && resourceProvider.getActiveProject() == null) {
            resourceProvider.getProject(resource.getName(),
                                        new AsyncCallback<Project>() {
                                            @Override
                                            public void onSuccess(Project result) {
                                            }

                                            @Override
                                            public void onFailure(Throwable caught) {
                                                Log.error(DatasourceExplorerPartPresenter.class,
                                                          "Can not get project", caught);
                                            }
                                        });
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onContextMenu(int mouseX, int mouseY) {
        // contextMenuPresenter.show(mouseX, mouseY);
    }
}
