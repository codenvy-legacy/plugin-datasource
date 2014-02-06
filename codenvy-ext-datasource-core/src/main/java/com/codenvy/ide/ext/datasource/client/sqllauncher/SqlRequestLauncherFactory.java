package com.codenvy.ide.ext.datasource.client.sqllauncher;

public interface SqlRequestLauncherFactory {

    SqlRequestLauncherView createSqlRequestLauncherView();

    SqlRequestLauncherPresenter createSqlRequestLauncherPresenter();

}
