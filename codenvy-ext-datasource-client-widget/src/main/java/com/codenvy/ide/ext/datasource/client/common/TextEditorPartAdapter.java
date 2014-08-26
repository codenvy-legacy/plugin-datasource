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
package com.codenvy.ide.ext.datasource.client.common;

import com.codenvy.ide.api.action.Constraints;
import com.codenvy.ide.api.editor.DocumentProvider;
import com.codenvy.ide.api.editor.EditorInitException;
import com.codenvy.ide.api.editor.EditorInput;
import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.editor.SelectionProvider;
import com.codenvy.ide.api.editor.TextEditorPartPresenter;
import com.codenvy.ide.api.event.FileEvent;
import com.codenvy.ide.api.event.FileEventHandler;
import com.codenvy.ide.api.parts.PartPresenter;
import com.codenvy.ide.api.parts.PartStack;
import com.codenvy.ide.api.parts.PropertyListener;
import com.codenvy.ide.api.parts.WorkspaceAgent;
import com.codenvy.ide.api.projecttree.generic.FileNode;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.api.text.Document;
import com.codenvy.ide.api.texteditor.outline.OutlinePresenter;
import com.codenvy.ide.util.ListenerManager;
import com.codenvy.ide.util.ListenerManager.Dispatcher;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.web.bindery.event.shared.EventBus;

import org.vectomatic.dom.svg.ui.SVGResource;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class TextEditorPartAdapter<T extends TextEditorPartPresenter> implements PartStack, TextEditorPartPresenter, FileEventHandler {

    private final SimpleLayoutPanel panel;

    private final T editor;

    private final ListenerManager<PropertyListener> manager;

    private final List<EditorPartCloseHandler> closeHandlers;

    private final PropertyListener relayPropertyListener;

    private final EditorPartCloseHandler relayCloseHandler;

    private final WorkspaceAgent workspaceAgent;

    public TextEditorPartAdapter(final @NotNull T editor,
                                 final @NotNull WorkspaceAgent workspaceAgent,
                                 final @NotNull EventBus eventBus) {
        this.editor = editor;
        this.workspaceAgent = workspaceAgent;

        this.panel = new SimpleLayoutPanel();
        this.panel.setSize("100%", "100%");
        this.manager = ListenerManager.create();
        this.closeHandlers = new ArrayList<EditorPartCloseHandler>();

        this.relayPropertyListener = new PropertyListener() {

            @Override
            public void propertyChanged(final PartPresenter source,
                                        final int propId) {
                if (TextEditorPartAdapter.this.editor.equals(source)) {
                    Log.debug(TextEditorPartAdapter.class,
                              "PropertyChanged event relayed - propId=" + propId);
                    firePropertyChange(propId);
                } else {
                    Log.warn(TextEditorPartAdapter.class,
                             "PropertyChanged event with unknown origin - won't relay.");
                }
            }
        };
        this.relayCloseHandler = new EditorPartCloseHandler() {

            @Override
            public void onClose(final EditorPartPresenter editor) {
                if (TextEditorPartAdapter.this.editor.equals(editor)) {
                    Log.debug(TextEditorPartAdapter.class, "Editor close event relayed");
                    handleClose();
                } else {
                    Log.warn(TextEditorPartAdapter.class,
                             "Editor close event with unknown source - not relaying");
                }

            }
        };
        eventBus.addHandler(FileEvent.TYPE, this);
    }

    public T getEditor() {
        return this.editor;
    }

    // Methods form PartStack
    @Override
    public void go(final AcceptsOneWidget container) {
        this.editor.go(this.panel);
        container.setWidget(this.panel);
    }

    @Override
    public void setFocus(boolean focused) {
        Log.warn(TextEditorPartAdapter.class, "SinglePartPresenter.setFocus - not allowed.");
        // do nothing
    }

    /** {@inheritDoc} */
    @Override
    public void addPart (final PartPresenter part, Constraints constraint){
        //TODO need to add sorting
        addPart(part);
    }

    @Override
    public void addPart(final PartPresenter part) {
        Log.error(TextEditorPartAdapter.class,
                  "This PartStack implementation doesn't allow adding parts.");
    }

    @Override
    public boolean containsPart(PartPresenter part) {
        return this.editor.equals(part);
    }

    @Override
    public int getNumberOfParts() {
        return 1;
    }

    @Override
    public PartPresenter getActivePart() {
        return this.editor;
    }

    @Override
    public void setActivePart(final PartPresenter part) {
        Log.warn(TextEditorPartAdapter.class, "SinglePartPresenter.setActivePart - not allowed.");
        // do nothing
    }

    @Override
    public void hidePart(final PartPresenter part) {
        Log.warn(TextEditorPartAdapter.class, "SinglePartPresenter.hidePart - not allowed.");
        // do nothing
    }

    @Override
    public void removePart(final PartPresenter part) {
        Log.error(TextEditorPartAdapter.class,
                  "This PartStack implementation doesn't allow removing parts.");
    }

    // Methods from PartPresenter
    @Override
    public String getTitle() {
        final String result = this.editor.getTitle();
        Log.debug(TextEditorPartAdapter.class, "Adapter part asked for its title - " + result);
        return result;
    }

    @Override
    public ImageResource getTitleImage() {
        return this.editor.getTitleImage();
    }

    @Override
    public String getTitleToolTip() {
        return this.editor.getTitleToolTip();
    }
    
    @Override
    public IsWidget getTitleWidget() {
        return this.editor.getTitleWidget();
    }

    @Override
    public int getSize() {
        return this.editor.getSize();
    }

    @Override
    public void onOpen() {
        Log.debug(TextEditorPartAdapter.class, "Opening editor adapter instance.");
        this.editor.onOpen();
    }

    @Override
    public boolean onClose() {
        Log.debug(TextEditorPartAdapter.class, "Trying to close editor adapter instance.");
        final boolean result = this.editor.onClose();
        Log.debug(TextEditorPartAdapter.class, "-- Closing success: " + result);
        return result;
    }

    @Override
    public void addPropertyListener(final PropertyListener listener) {
        this.manager.add(listener);
        this.editor.addPropertyListener(this.relayPropertyListener);
    }

    @Override
    public void removePropertyListener(final PropertyListener listener) {
        Log.debug(TextEditorPartAdapter.class, "Removing property listener : " + listener);
        this.manager.remove(listener);
    }

    private void firePropertyChange(final int propId) {
        Log.debug(TextEditorPartAdapter.class, "Dispatching property change event to " + manager.getCount() + " listeners.");
        manager.dispatch(new Dispatcher<PropertyListener>() {

            @Override
            public void dispatch(final PropertyListener listener) {
                Log.debug(TextEditorPartAdapter.class, "-- listener " + listener);
                listener.propertyChanged(TextEditorPartAdapter.this, propId);
            }
        });
    }

    @Override
    public Selection< ? > getSelection() {
        return this.editor.getSelection();
    }

    // Methods from EditorPartPresenter
    @Override
    public void init(final EditorInput input) throws EditorInitException {
        this.editor.init(input);
    }

    @Override
    public EditorInput getEditorInput() {
        return this.editor.getEditorInput();
    }

    @Override
    public void doSave() {
        this.editor.doSave();
    }

    @Override
    public void doSave(@NotNull AsyncCallback<EditorInput> callback) {
        this.editor.doSave(callback);
    }

    @Override
    public void doSaveAs() {
        this.editor.doSaveAs();
    }

    @Override
    public void onFileChanged() {
        this.editor.onFileChanged();
    }

    @Override
    public boolean isDirty() {
        return this.editor.isDirty();
    }

    @Override
    public void addCloseHandler(final EditorPartCloseHandler closeHandler) {
        if (!this.closeHandlers.contains(closeHandler)) {
            this.closeHandlers.add(closeHandler);
        }
        this.editor.addCloseHandler(this.relayCloseHandler);
    }

    private void handleClose() {
        for (EditorPartCloseHandler handler : closeHandlers) {
            handler.onClose(this);
        }
    }

    @Override
    public void activate() {
        this.editor.activate();
    }

    // Methods from TextEditorPartPresenter
    @Override
    public DocumentProvider getDocumentProvider() {
        return this.editor.getDocumentProvider();
    }

    @Override
    public void close(boolean save) {
        this.editor.removePropertyListener(this.relayPropertyListener);
        this.editor.close(save);
    }

    @Override
    public boolean isEditable() {
        return this.editor.isEditable();
    }

    @Override
    public void doRevertToSaved() {
        this.editor.doRevertToSaved();
    }

    @Override
    public SelectionProvider getSelectionProvider() {
        return this.editor.getSelectionProvider();
    }

    @Override
    public OutlinePresenter getOutline() {
        return this.editor.getOutline();
    }

    // methods from FileEventHandler
    @Override
    public void onFileOperation(final FileEvent event) {
        if (event.getOperationType() != FileEvent.FileOperation.CLOSE) {
            return;
        }

        FileNode eventFile = event.getFile();
        FileNode file = this.editor.getEditorInput().getFile();
        if (file.equals(eventFile)) {
            workspaceAgent.removePart(this);
        }
    }

    @Override
    public Document getDocument() {
        return editor.getDocument();
    }

    @Nullable
    @Override
    public SVGResource getTitleSVGImage() {
        return null;
    }
}
