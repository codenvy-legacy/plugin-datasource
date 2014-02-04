package com.codenvy.ide.ext.datasource.client.common;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.api.editor.EditorInitException;
import com.codenvy.ide.api.editor.EditorInput;
import com.codenvy.ide.api.editor.EditorPartPresenter;
import com.codenvy.ide.api.selection.Selection;
import com.codenvy.ide.api.ui.workspace.PartPresenter;
import com.codenvy.ide.api.ui.workspace.PartStack;
import com.codenvy.ide.api.ui.workspace.PropertyListener;
import com.codenvy.ide.util.loging.Log;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

public class SinglePartPresenter implements PartStack, EditorPartPresenter {

    private final SimpleLayoutPanel panel = new SimpleLayoutPanel();

    private HasEditorPartPresenter  part;

    public SinglePartPresenter(final @NotNull HasEditorPartPresenter part) {
        this.part = part;
    }

    @Override
    public void go(final AcceptsOneWidget container) {
        this.part.go(this.panel);
        container.setWidget(this.panel);
    }

    @Override
    public void setFocus(boolean focused) {
        Log.warn(SinglePartPresenter.class, "SinglePartPresenter.setFocus - not allowed.");
        // do nothing
    }

    @Override
    public void addPart(final PartPresenter part) {
        Log.error(SinglePartPresenter.class,
                  "This PartStack implementation doesn't allow adding parts.");
    }

    @Override
    public boolean containsPart(PartPresenter part) {
        return this.part.equals(part);
    }

    @Override
    public int getNumberOfParts() {
        return 1;
    }

    @Override
    public PartPresenter getActivePart() {
        return this.part;
    }

    @Override
    public void setActivePart(final PartPresenter part) {
        Log.warn(SinglePartPresenter.class, "SinglePartPresenter.setActivePart - not allowed.");
        // do nothing
    }

    @Override
    public void hidePart(final PartPresenter part) {
        Log.warn(SinglePartPresenter.class, "SinglePartPresenter.hidePart - not allowed.");
        // do nothing
    }

    @Override
    public void removePart(final PartPresenter part) {
        Log.error(SinglePartPresenter.class,
                  "This PartStack implementation doesn't allow removing parts.");
    }

    @Override
    public String getTitle() {
        return this.part.getEditorPartPresenter().getTitle();
    }

    @Override
    public ImageResource getTitleImage() {
        return this.part.getEditorPartPresenter().getTitleImage();
    }

    @Override
    public String getTitleToolTip() {
        return this.part.getEditorPartPresenter().getTitleToolTip();
    }

    @Override
    public int getSize() {
        return this.part.getEditorPartPresenter().getSize();
    }

    @Override
    public void onOpen() {
        this.part.getEditorPartPresenter().onOpen();
    }

    @Override
    public boolean onClose() {
        return this.part.getEditorPartPresenter().onClose();
    }

    @Override
    public void addPropertyListener(final PropertyListener listener) {
        this.part.getEditorPartPresenter().addPropertyListener(listener);
    }

    @Override
    public Selection< ? > getSelection() {
        return this.part.getEditorPartPresenter().getSelection();
    }

    @Override
    public void removePropertyListener(final PropertyListener listener) {
        this.part.removePropertyListener(listener);
    }

    @Override
    public void init(final EditorInput input) throws EditorInitException {
        this.part.getEditorPartPresenter().init(input);
    }

    @Override
    public EditorInput getEditorInput() {
        return this.part.getEditorPartPresenter().getEditorInput();
    }

    @Override
    public void doSave() {
        this.part.getEditorPartPresenter().doSave();
    }

    @Override
    public void doSaveAs() {
        this.part.getEditorPartPresenter().doSaveAs();
    }

    @Override
    public void onFileChanged() {
        this.part.getEditorPartPresenter().onFileChanged();
    }

    @Override
    public boolean isDirty() {
        return this.part.getEditorPartPresenter().isDirty();
    }

    @Override
    public void addCloseHandler(final EditorPartCloseHandler closeHandler) {
        this.part.getEditorPartPresenter().addCloseHandler(closeHandler);
    }

    @Override
    public void activate() {
        this.part.getEditorPartPresenter().activate();
    }

    public interface HasEditorPartPresenter extends PartPresenter {
        EditorPartPresenter getEditorPartPresenter();
    }
}
