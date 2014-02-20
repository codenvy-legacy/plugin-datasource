package com.codenvy.ide.ext.datasource.client.explorer;

import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.Messages;

@DefaultLocale("en")
public interface DatasourceExplorerConstants extends Messages {

    @DefaultMessage("Explore")
    String exploreButtonLabel();

    @DefaultMessage("Fetching database metadata...")
    String notificationFetchStart();

    @DefaultMessage("Succesfully fetched database metadata")
    String notificationFetchSuccess();

    @DefaultMessage("Failed fetching database metadata")
    String notificationFetchFailure();
}
