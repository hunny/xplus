[TOC]

# SpringBoot之Actuator

标签（空格分隔）： Actuator

---

Spring Boot有四个实用工具，分别是auto-configuration、starters、cli、actuator，本文主要讲actuator。actuator是spring boot提供的对应用系统的自省和监控的集成功能，可以对应用系统进行配置查看、相关功能统计等。

## 使用Actuator

- 添加依赖

```
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
```

- 主要外露的功能

| HTTP方法 | 路径 | 描述 | 鉴权 |
| --- | --- | --- | --- |
| GET | /autoconfig | 查看自动配置的使用情况 | true |
| GET | /configprops | 查看配置属性，包括默认配置 | true | |
| GET | /beans | 查看bean及其关系列表 | true |
| GET | /dump | 打印线程栈 | true |
| GET | /env | 查看所有环境变量 | true | |
| GET | /env/{name} | 查看具体变量值 | true |
| GET | /health | 查看应用健康指标 | false |
| GET | /info | 查看应用信息 | false |
| GET | /mappings | 查看所有url映射 | true |
| GET | /metrics | 查看应用基本指标 | true |
| GET | /metrics/{name} | 查看具体指标 | true |
| POST | /shutdown | 关闭应用 | true |
| GET | /trace | 查看基本追踪信息 | true |

### /autoconfig

```
{
    "positiveMatches": {
        "AuditAutoConfiguration.AuditEventRepositoryConfiguration": [
            {
                "condition": "OnBeanCondition", 
                "message": "@ConditionalOnMissingBean (types: org.springframework.boot.actuate.audit.AuditEventRepository; SearchStrategy: all) found no beans"
            }
        ]
    }, 
    "negativeMatches": {
        "CacheStatisticsAutoConfiguration": [
            {
                "condition": "OnBeanCondition", 
                "message": "@ConditionalOnBean (types: org.springframework.cache.CacheManager; SearchStrategy: all) found no beans"
            }
        ]
    }
}
```

### /configprops

```
{
    "management.health.status.CONFIGURATION_PROPERTIES": {
        "prefix": "management.health.status", 
        "properties": {
            "order": null
        }
    }, 
    "multipart.CONFIGURATION_PROPERTIES": {
        "prefix": "multipart", 
        "properties": {
            "enabled": false, 
            "maxRequestSize": "10Mb", 
            "location": null, 
            "fileSizeThreshold": "0", 
            "maxFileSize": "1Mb"
        }
    }, 
    "environmentEndpoint": {
        "prefix": "endpoints.env", 
        "properties": {
            "id": "env", 
            "enabled": true, 
            "sensitive": true
        }
    }
}
```

### /beans
```
[
    {
        "context": "application:8080", 
        "parent": null, 
        "beans": [
            {
                "bean": "appMain", 
                "scope": "singleton", 
                "type": "com.xixicat.AppMain$$EnhancerBySpringCGLIB$$29382b14", 
                "resource": "null", 
                "dependencies": [ ]
            }, 
            {
                "bean": "videoInfoMapper", 
                "scope": "singleton", 
                "type": "com.xixicat.dao.VideoInfoMapper", 
                "resource": "file [/Users/xixicat/workspace/video-uber/target/classes/com/xixicat/dao/VideoInfoMapper.class]", 
                "dependencies": [
                    "sqlSessionFactory"
                ]
            }
        ]
    }
]
```

### /dump
```
[
    {
        "threadName": "Signal Dispatcher", 
        "threadId": 4, 
        "blockedTime": -1, 
        "blockedCount": 0, 
        "waitedTime": -1, 
        "waitedCount": 0, 
        "lockName": null, 
        "lockOwnerId": -1, 
        "lockOwnerName": null, 
        "inNative": false, 
        "suspended": false, 
        "threadState": "RUNNABLE", 
        "stackTrace": [ ], 
        "lockedMonitors": [ ], 
        "lockedSynchronizers": [ ], 
        "lockInfo": null
    }, 
    {
        "threadName": "Reference Handler", 
        "threadId": 2, 
        "blockedTime": -1, 
        "blockedCount": 217, 
        "waitedTime": -1, 
        "waitedCount": 9, 
        "lockName": "java.lang.ref.Reference$Lock@45de945", 
        "lockOwnerId": -1, 
        "lockOwnerName": null, 
        "inNative": false, 
        "suspended": false, 
        "threadState": "WAITING", 
        "stackTrace": [
            {
                "methodName": "wait", 
                "fileName": "Object.java", 
                "lineNumber": -2, 
                "className": "java.lang.Object", 
                "nativeMethod": true
            }, 
            {
                "methodName": "wait", 
                "fileName": "Object.java", 
                "lineNumber": 503, 
                "className": "java.lang.Object", 
                "nativeMethod": false
            }, 
            {
                "methodName": "run", 
                "fileName": "Reference.java", 
                "lineNumber": 133, 
                "className": "java.lang.ref.Reference$ReferenceHandler", 
                "nativeMethod": false
            }
        ], 
        "lockedMonitors": [ ], 
        "lockedSynchronizers": [ ], 
        "lockInfo": {
            "className": "java.lang.ref.Reference$Lock", 
            "identityHashCode": 73263429
        }
    }
]
```

### /env
```
{
  profiles: [],
  server.ports: {
    local.server.port: 8080
  },
  servletContextInitParams: {},
  systemProperties: {
    java.runtime.name: "Java(TM) SE Runtime Environment",
      sun.boot.library.path: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib",
      java.vm.version: "24.79-b02",
      gopherProxySet: "false",
      maven.multiModuleProjectDirectory: "/Users/xixicat/workspace/video-uber",
      java.vm.vendor: "Oracle Corporation",
      java.vendor.url: "http://java.oracle.com/",
      guice.disable.misplaced.annotation.check: "true",
      path.separator: ":",
      java.vm.name: "Java HotSpot(TM) 64-Bit Server VM",
      file.encoding.pkg: "sun.io",
      user.country: "CN",
      sun.java.launcher: "SUN_STANDARD",
      sun.os.patch.level: "unknown",
      PID: "763",
      java.vm.specification.name: "Java Virtual Machine Specification",
      user.dir: "/Users/xixicat/workspace/video-uber",
      java.runtime.version: "1.7.0_79-b15",
      java.awt.graphicsenv: "sun.awt.CGraphicsEnvironment",
      java.endorsed.dirs: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/endorsed",
      os.arch: "x86_64",
      java.io.tmpdir: "/var/folders/tl/xkf4nr61033gd6lk5d3llz080000gn/T/",
      line.separator: " ",
      java.vm.specification.vendor: "Oracle Corporation",
      os.name: "Mac OS X",
      classworlds.conf: "/Users/xixicat/devtool/maven-3.3.3/bin/m2.conf",
      sun.jnu.encoding: "UTF-8",
      spring.beaninfo.ignore: "true",
      java.library.path: "/Users/xixicat/Library/Java/Extensions:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java:.",
      java.specification.name: "Java Platform API Specification",
      java.class.version: "51.0",
      sun.management.compiler: "HotSpot 64-Bit Tiered Compilers",
      os.version: "10.10.5",
      user.home: "/Users/xixicat",
      user.timezone: "Asia/Shanghai",
      java.awt.printerjob: "sun.lwawt.macosx.CPrinterJob",
      file.encoding: "UTF-8",
      java.specification.version: "1.7",
      java.class.path: "/Users/xixicat/devtool/maven-3.3.3/boot/plexus-classworlds-2.5.2.jar",
      user.name: "xixicat",
      java.vm.specification.version: "1.7",
      sun.java.command: "org.codehaus.plexus.classworlds.launcher.Launcher spring-boot:run",
      java.home: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre",
      sun.arch.data.model: "64",
      user.language: "zh",
      java.specification.vendor: "Oracle Corporation",
      awt.toolkit: "sun.lwawt.macosx.LWCToolkit",
      java.vm.info: "mixed mode",
      java.version: "1.7.0_79",
      java.ext.dirs: "/Users/xixicat/Library/Java/Extensions:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/ext:/Library/Java/Extensions:/Network/Library/Java/Extensions:/System/Library/Java/Extensions:/usr/lib/java",
      sun.boot.class.path: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/sunrsasign.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home/jre/classes",
      java.awt.headless: "true",
      java.vendor: "Oracle Corporation",
      maven.home: "/Users/xixicat/devtool/maven-3.3.3",
      file.separator: "/",
      LOG_EXCEPTION_CONVERSION_WORD: "%wEx",
      java.vendor.url.bug: "http://bugreport.sun.com/bugreport/",
      sun.io.unicode.encoding: "UnicodeBig",
      sun.cpu.endian: "little",
      sun.cpu.isalist: ""
  },
  systemEnvironment: {
    TERM: "xterm-256color",
    ZSH: "/Users/xixicat/.oh-my-zsh",
    GVM_BROKER_SERVICE: "http://release.gvm.io",
    GRIFFON_HOME: "/Users/xixicat/.gvm/griffon/current",
    JAVA_MAIN_CLASS_763: "org.codehaus.plexus.classworlds.launcher.Launcher",
    JAVA_HOME: "/Library/Java/JavaVirtualMachines/jdk1.7.0_79.jdk/Contents/Home",
    SHLVL: "1",
    MAVEN_CMD_LINE_ARGS: " spring-boot:run",
    __CF_USER_TEXT_ENCODING: "0x1F5:0x19:0x34",
    GROOVY_HOME: "/Users/xixicat/.gvm/groovy/current",
    XPC_FLAGS: "0x0",
    GVM_INIT: "true",
    JBAKE_HOME: "/Users/xixicat/.gvm/jbake/current",
    PWD: "/Users/xixicat/workspace/video-uber",
    GVM_DIR: "/Users/xixicat/.gvm",
    GVM_VERSION: "2.4.3",
    MAVEN_PROJECTBASEDIR: "/Users/xixicat/workspace/video-uber",
    LOGNAME: "xixicat",
    SSH_AUTH_SOCK: "/private/tmp/com.apple.launchd.93xr1duECQ/Listeners",
    SPRINGBOOT_HOME: "/Users/xixicat/.gvm/springboot/current",
    GAIDEN_HOME: "/Users/xixicat/.gvm/gaiden/current",
    LAZYBONES_HOME: "/Users/xixicat/.gvm/lazybones/current",
    OLDPWD: "/Users/xixicat/workspace/video-uber",
    SHELL: "/bin/zsh",
    JBOSSFORGE_HOME: "/Users/xixicat/.gvm/jbossforge/current",
    LC_CTYPE: "zh_CN.UTF-8",
    TMPDIR: "/var/folders/tl/xkf4nr61033gd6lk5d3llz080000gn/T/",
    GVM_SERVICE: "http://api.gvmtool.net",
    GVM_PLATFORM: "Darwin",
    CLASSPATH: ".:/Users/xixicat/.m2/repository/co/paralleluniverse/quasar-core/0.7.2/quasar-core-0.7.2.jar",
    GLIDE_HOME: "/Users/xixicat/.gvm/glide/current",
    PATH: "/Users/xixicat/.gvm/vertx/current/bin:/Users/xixicat/.gvm/springboot/current/bin:/Users/xixicat/.gvm/lazybones/current/bin:/Users/xixicat/.gvm/jbossforge/current/bin:/Users/xixicat/.gvm/jbake/current/bin:/Users/xixicat/.gvm/groovyserv/current/bin:/Users/xixicat/.gvm/groovy/current/bin:/Users/xixicat/.gvm/griffon/current/bin:/Users/xixicat/.gvm/grails/current/bin:/Users/xixicat/.gvm/gradle/current/bin:/Users/xixicat/.gvm/glide/current/bin:/Users/xixicat/.gvm/gaiden/current/bin:/Users/xixicat/.gvm/crash/current/bin:/Users/xixicat/.gvm/asciidoctorj/current/bin:/Users/xixicat/bin:/usr/local/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/git/bin:/Users/xixicat/devtool/maven-3.3.3/bin:/Users/xixicat/devtool/gradle-2.6/bin:/Users/xixicat/devtool/android-sdk/platform-tools",
    GRADLE_HOME: "/Users/xixicat/.gvm/gradle/current",
    GROOVYSERV_HOME: "/Users/xixicat/.gvm/groovyserv/current",
    GRAILS_HOME: "/Users/xixicat/.gvm/grails/current",
    USER: "xixicat",
    LESS: "-R",
    PAGER: "less",
    HOME: "/Users/xixicat",
    CRASH_HOME: "/Users/xixicat/.gvm/crash/current",
    XPC_SERVICE_NAME: "0",
    VERTX_HOME: "/Users/xixicat/.gvm/vertx/current",
    GVM_BROADCAST_SERVICE: "http://cast.gvm.io",
    Apple_PubSub_Socket_Render: "/private/tmp/com.apple.launchd.y6fNwP8Sk6/Render",
    LSCOLORS: "Gxfxcxdxbxegedabagacad",
    ASCIIDOCTORJ_HOME: "/Users/xixicat/.gvm/asciidoctorj/current"
  },
  applicationConfig: [classpath: /application.properties]: {
    pool.acquireIncrement: "1",
    pool.minPoolSize: "5",
    pool.initialPoolSize: "1",
    database.username: "root",
    pool.maxIdleTime: "60",
    database.url: "jdbc:mysql://127.0.0.1:3307/video_uber?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
    spring.jackson.dateFormat: "yyyy-MM-dd'T'HH:mm:ss",
    database.slave.username: "root",
    spring.jackson.serialization.write - dates - as - timestamps: "false",
    pool.idleTimeout: "30000",
    database.slave.url: "jdbc:mysql://127.0.0.1:3307/demo?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
    server.port: "8080",
    database.slave.password: "******",
    database.password: "******",
    database.driverClassName: "com.mysql.jdbc.Driver",
    pool.maxPoolSize: "50",
    database.dataSourceClassName: "com.mysql.jdbc.jdbc2.optional.MysqlDataSource"
  }
}
```
### /health
```
{
  status: "UP",
  diskSpace: {
    status: "UP",
    total: 249779191808,
    free: 193741590528,
    threshold: 10485760
  },
  db: {
    status: "UP",
    database: "MySQL",
    hello: 1
  }
}
```
### /info
需要自己在application.properties里头添加信息，比如
```
info:
  contact:
    email: test@gmail.com
    phone: 021-12345678
```
然后请求就可以出来了
```
{
  "contact": {
     "phone": "021-12345678",
     "email": "test@gmail.com"
  }
}
```
### /mappings
```
     {
        {
          [/metrics || /metrics.json], methods = [GET], produces = [application / json]
        }: {
          bean: "endpointHandlerMapping",
          method: "public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()"
        }, {
          [/beans || /beans.json], methods = [GET], produces = [application / json]
        }: {
          bean: "endpointHandlerMapping",
          method: "public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()"
        }, {
          [/health || /health.json], produces = [application / json]
        }: {
          bean: "endpointHandlerMapping",
          method: "public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.HealthMvcEndpoint.invoke(java.security.Principal)"
        }, {
          [/info || /info.json], methods = [GET], produces = [application / json]
        }: {
          bean: "endpointHandlerMapping",
          method: "public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()"
        }, {
          [/trace || /trace.json], methods = [GET], produces = [application / json]
        }: {
          bean: "endpointHandlerMapping",
          method: "public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()"
        }, {
          [/autoconfig || /autoconfig.json], methods = [GET], produces = [application / json]
        }: {
          bean: "endpointHandlerMapping",
          method: "public java.lang.Object org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter.invoke()"
        }
      }
```

### /metrics
```
{
mem: 499404,
mem.free: 257591,
processors: 8,
instance.uptime: 4284997,
uptime: 4294909,
systemload.average: 1.84521484375,
heap.committed: 437248,
heap.init: 262144,
heap.used: 179656,
heap: 3728384,
nonheap.committed: 62848,
nonheap.init: 24000,
nonheap.used: 62156,
nonheap: 133120,
threads.peak: 18,
threads.daemon: 6,
threads.totalStarted: 176,
threads: 16,
classes: 10294,
classes.loaded: 10294,
classes.unloaded: 0,
gc.ps_scavenge.count: 11,
gc.ps_scavenge.time: 405,
gc.ps_marksweep.count: 0,
gc.ps_marksweep.time: 0,
datasource.primary.active: 0,
datasource.primary.usage: 0,
counter.status.200.autoconfig: 1,
counter.status.200.beans: 1,
counter.status.200.configprops: 1,
counter.status.200.dump: 1,
counter.status.200.env: 1,
counter.status.200.health: 1,
counter.status.200.info: 1,
counter.status.200.mappings: 1,
gauge.response.autoconfig: 81,
gauge.response.beans: 15,
gauge.response.configprops: 105,
gauge.response.dump: 76,
gauge.response.env: 4,
gauge.response.health: 43,
gauge.response.info: 1,
gauge.response.mappings: 4
}
```
### /shutdown
要真正生效，得配置文件开启
```
endpoints.shutdown.enabled: true
```
### /trace
记录最近100个请求的信息
```
[{
  "timestamp": 1452955704922,
  "info": {
    "method": "GET",
    "path": "/metrics",
    "headers": {
      "request": {
        "Accept - Encoding": "gzip, deflate, sdch",
          "Upgrade - Insecure - Requests": "1",
          "Accept - Language": "zh-CN,zh;q=0.8,en;q=0.6",
          "User - Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36",
          "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
          "Connection": "keep-alive",
          "Host": "localhost:8080"
      },
      "response": {
        "Content - Type": "application/json; charset=UTF-8",
          "X - Application - Context": "application:8080",
          "Date": "Sat, 16 Jan 2016 14:48:24 GMT",
          "status": "200"
      }
    }
  }
}, {
  "timestamp": 1452951489549,
  "info": {
    "method": "GET",
    "path": "/autoconfig",
    "headers": {
      "request": {
        "Accept - Encoding": "gzip, deflate, sdch",
          "Upgrade - Insecure - Requests": "1",
          "Accept - Language": "zh-CN,zh;q=0.8,en;q=0.6",
          "User - Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36",
          "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
          "Connection": "keep-alive",
          "Host": "localhost:8080"
      },
      "response": {
        "Content - Type": "application/json; charset=UTF-8",
          "X - Application - Context": "application:8080",
          "Date": "Sat, 16 Jan 2016 13:38:09 GMT",
          "status": "200"
      }
    }
  }
}]
```