package com.codenvy.ide.ext.datasource.client.sqllauncher;


import com.codenvy.ide.util.loging.Log;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

class InfoHeader extends HTML implements RequiresResize {

    private Resizer resizer;

    public InfoHeader(final SafeHtml safeHtml) {
        super(safeHtml);
    }

    @Override
    public void onResize() {
        Log.info(InfoHeader.class, "InfoHeader resize");
        if (this.resizer != null) {
            Log.info(InfoHeader.class, "Set size to " + this.resizer.getSize());
            this.setWidth(Integer.toString(this.resizer.getSize()) + "px");
        }
    }

    public void setResizer(final Resizer resizer) {
        this.resizer = resizer;
    }

    public static class Resizer {
        private final Widget resizeOrigin;

        public Resizer(final Widget resizeOrigin) {
            this.resizeOrigin = resizeOrigin;
        }

        public int getSize() {
            return this.resizeOrigin.getOffsetWidth();
        }
    }

}
