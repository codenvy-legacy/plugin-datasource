Datasource-Plug-In
==================

Datassource plug-In for IDE3

Bundled JDBC drivers
--------------------

The datasource plugin relies on JDBC driver.
Some JDBC drivers can't be bundled with the tomcat instance that is packaged by the codenvy-ext-datasource-packaging-standalone
module.

The following drivers are bundled by default :

- postgres
- jtds (for MS SQLServer)
- mysql

Is the other drivers are needed, you must either :

- be manually added to the packaged tomcat, inside the tomcat-ide/lib directory
- force their inclusion by activating the relevant profile at compilation

To force the bundling at compilation the driver must be present in you maven local repository or be available in a
repository where your maven installation can find it.

Available JDBC profiles
-----------------------

At the moment, the followind JDBC profiles are available :

- postgres ; enabled by default ; can be disabled with -DdisablePostgres at compile time
- jtds ; enabled by default ; can be disabled with -DdisableJtds at compile time
- mysql ; enabled by default ; can be disabled with -DdisableMysql at compile time

- oracle ; disabled by default ; can be enabled with -DenableOracle at compile time
- nuodb ; disabled by default ; can be enabled with -DenableNuodb at compile time

For example, you can create a tomcat ide instance with the following maven command :

```
    mvn clean install -DenableOracle
```

How to add Oracle JDBC Driver in your Maven local repository:
-------------------------------------------------------------

1. Get the appropriate oracle JDBC Driver: 2 ways:
  - Get it from wwww.oracle.com
    For example: You can get Oracle Database 11g Release 2 JDBC Drivers from:
http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-112010-090769.html
  - Get it from Oracle database installed folder, for example, `{ORACLE_HOME}\jdbc\lib\ojdbc6.jar`


2. Install it:
To install your Oracle jdbc driver, issue following command :


        mvn install:install-file -Dfile={path/to/your/ojdbc.jar} -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar

 
 
How to add the NuoDB Driver in your maven local repository:
----------------------------------------------------------

1. Retrieve the driver jar from a NuoDB installation. it should be in jar/nuodbjdbc.jar.

2. Install it :

```
        mvn install:install-file -Dfile={path/to/your/nuodbjdbc.jar} -DgroupId=com.nuodb.jdbc -DartifactId=nuodb-jdbc -Dversion=2.0.3 -Dpackaging=jar
```