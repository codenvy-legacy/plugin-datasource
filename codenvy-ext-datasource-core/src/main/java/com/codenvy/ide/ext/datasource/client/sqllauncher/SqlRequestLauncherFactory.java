package com.codenvy.ide.ext.datasource.client.sqllauncher;

public interface SqlRequestLauncherFactory {

    SqlRequestLauncherPresenter createSqlRequestLauncherPresenter();

    SqlRequestLauncherAdapter createSqlRequestLauncherAdapter();
}
