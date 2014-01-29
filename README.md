Datasource-Plug-In
==================

Datassource plug-In for IDE3

How to add Oracle JDBC Driver in your Maven local Repository:
-------------------------------------------------------------

1.Get the appropriate oracle JDBC Driver: 2 ways:

- Get it from wwww.oracle.com
For example: You can get Oracle Database 11g Release 2 JDBC Drivers from: http://www.oracle.com/technetwork/database/enterprise-edition/jdbc-112010-090769.html

- Get it from Oracle database installed folder, for example, “{ORACLE_HOME}\jdbc\lib\ojdbc6.jar“

2.Install It:
To install your Oracle jdbc driver, issue following command :

mvn install:install-file -Dfile={Path/to/your/ojdbc.jar} -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar

3.pom.xml:
You reference it by declares following Oracle details in your pom.xml.

		<!-- ORACLE database driver -->
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>ojdbc6</artifactId>
			<version>11.2.0</version>
		</dependency>
 
 
How to add SQL server JDBC Driver in your Maven local Repository:
-----------------------------------------------------------------

1.Download the driver JAR from: 
http://www.microsoft.com/en-us/download/details.aspx?displaylang=en&id=11774

2.Unpack the file

3.Install the jar: 

mvn install:install-file -Dfile=sqljdbc4.jar -DgroupId=com.microsoft.sqlserver 
-DartifactId=sqljdbc4 -Dversion=3.0 -Dpackaging=jar

4.Add dependency to the pom.xml:
	
	  <!-- SQL server database driver -->
    <dependency>
      <groupId>com.microsoft.sqlserver</groupId>
      <artifactId>sqljdbc4</artifactId>
      <version>3.0</version>
    </dependency>

