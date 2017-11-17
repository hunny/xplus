# SpringBoot H2 Integration

## Website

[http://www.h2database.com/](http://www.h2database.com/)


## Add Maven Dependency

```
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
``

## Add Connection Configuration

* Add to `application.properties` for example:

```
# 配置h2数据库的连接地址(内存)
spring.datasource.url=jdbc:h2:mem:test
#h2本地化：指定一个绝对的路径
#spring.datasource.url = jdbc:h2:file:D:\\test\\db\\testdb
#h2本地化：使用~标明位置
#spring.datasource.url = jdbc:h2:file:~/.h2/testdb
# 配置JDBC Driver
spring.datasource.driver-class-name=org.h2.Driver
# 配置数据库用户名
spring.datasource.username=root
# 配置数据库密码
spring.datasource.password=123456
```

* SpringBoot Default:

```
# H2 Web Console (H2ConsoleProperties)
spring.h2.console.enabled=false # Enable the console.
spring.h2.console.path=/h2-console # Path at which the console will be available.
spring.h2.console.settings.trace=false # Enable trace output.
spring.h2.console.settings.web-allow-others=false # Enable remote access.
```

## Database Data Init

* Add to `application.properties` for example:

```
# 启动程序，程序都会运行resources/db/schema.sql文件，对数据库的结构进行操作。
spring.datasource.schema=classpath:db/schema.sql
# 启动程序，程序都会运行resources/db/data.sql文件，对数据库的数据操作。
spring.datasource.data=classpath:db/data.sql
```

## View H2 Database Info

* 要知道H2数据的连接的URL，默认情况下，SpringBoot的Hibernate打印的是Info级别的信息，其是查看不到H2数据连接相关的信息。
* 需要把SpringBoot的Hibernate的debug信息打开。
* 创建一个application.properties文件，然后在其文件里面添加下面一行。

```
logging.level.org.hibernate=DEBUG
```

可看到输出日志:

```
2017-11-14 14:17:11.887 DEBUG 9640 --- [           main] o.hibernate.jpa.internal.util.LogHelper  : PersistenceUnitInfo [
	name: default
	persistence provider classname: null
	classloader: sun.misc.Launcher$AppClassLoader@18b4aac2
	excludeUnlistedClasses: true
	JTA datasource: null
	Non JTA datasource: org.apache.tomcat.jdbc.pool.DataSource@261db982{ConnectionPool[defaultAutoCommit=null; defaultReadOnly=null; defaultTransactionIsolation=-1; defaultCatalog=null; driverClassName=org.h2.Driver; maxActive=100; maxIdle=100; minIdle=10; initialSize=10; maxWait=30000; testOnBorrow=true; testOnReturn=false; timeBetweenEvictionRunsMillis=5000; numTestsPerEvictionRun=0; minEvictableIdleTimeMillis=60000; testWhileIdle=false; testOnConnect=false; password=********; url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE; username=sa; validationQuery=SELECT 1; validationQueryTimeout=-1; validatorClassName=null; validationInterval=3000; accessToUnderlyingConnectionAllowed=true; removeAbandoned=false; removeAbandonedTimeout=60; logAbandoned=false; connectionProperties=null; initSQL=null; jdbcInterceptors=null; jmxEnabled=true; fairQueue=true; useEquals=true; abandonWhenPercentageFull=0; maxAge=0; useLock=false; dataSource=null; dataSourceJNDI=null; suspectTimeout=0; alternateUsernameAllowed=false; commitOnReturn=false; rollbackOnReturn=false; useDisposableConnectionFacade=true; logValidationErrors=false; propagateInterruptState=false; ignoreExceptionOnPreLoad=false; useStatementFacade=true; }
	Transaction type: RESOURCE_LOCAL
	PU root URL: file:/C:/work/xplus/bootweb/trunk/bootweb-qzdynh2/target/classes/
	Shared Cache Mode: UNSPECIFIED
	Validation Mode: AUTO
	Jar files URLs []
	Managed classes names [
		com.example.bootweb.qzdynh2.entity.QuartzConfig]
	Mapping files names []
	Properties []
```

中显示即为数据库默认连接：
```
url=jdbc:h2:mem:testdb
```

## Connecting to a Database using JDBC

```
import java.sql.*;
public class Test {
    public static void main(String[] a)
            throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.
            getConnection("jdbc:h2:~/test", "sa", "");
        // add application code here
        conn.close();
    }
}
```

## Configuration H2 Web Consloe

* h2 web consloe是一个数据库GUI管理应用，程序运行时，会自动启动h2 web consloe。

```
# 进行该配置，程序开启时就会启动h2 web consloe。
spring.h2.console.enabled=true
# 进行该配置后，h2 web consloe就可以在远程访问了。否则只能在本机访问。
spring.h2.console.settings.web-allow-others=true
# 进行该配置，你就可以通过YOUR_URL/h2-console访问h2 web consloe。YOUR_URL是你程序的访问URl。
spring.h2.console.path=/h2-console
```
