# Merge upstream project
## About
`codenvy-ext-datasource-packaging-standalone-tomcat`, `codenvy-ext-datasource-application-war` has originally been copied from codenvy-ide3 project and adapted.

`codenvy-ext-datasource-api-war` will patch `codenvy-api-war` at build time, adding and copying dependencies to the war

## Merging latest change from codenvy-ide3
When working against codenvy-ide3 SNAPSHOT, if one of `codenvy-packaging-standalone-tomcat` `codenvy-application-war` is being modified, it may result some compatibility issues in datasource ones so, changes need to be merge in datasource plugin:


In codenvy-ide3 master:

    git diff revision assembly-ide > revision.patch
    git diff revision assembly-sdk >> revision.patch
    sed -i 's/assembly-ide/codenvy-ext-datasource-application-war/g' revision.patch
    sed -i 's/assembly-sdk/codenvy-ext-datasource-packaging-standalone-tomcat/g' revision.patch

In codenvy-datasource-plugin:

   patch -p1 < revision.patch

Check that the patch is ok, commit and push



