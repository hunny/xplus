log4j.rootLogger=DEBUG, CA, FILE, ERRFILE
# ConsoleAppender
log4j.appender.CA=org.apache.log4j.ConsoleAppender
log4j.appender.CA.layout=org.apache.log4j.PatternLayout
log4j.appender.CA.layout.ConversionPattern=%d [%t] %-5p %c %x (%F:%L) - %m%n

log4j.appender.FILE=org.apache.log4j.DailyRollingFileAppender
log4j.appender.FILE.File=${log4jPath}/${log4jName}.log
log4j.appender.FILE.layout=org.apache.log4j.PatternLayout
log4j.appender.FILE.layout.ConversionPattern=%d [%t] %p %c - %m%n
log4j.appender.FILE.DatePattern='.'yyyy-MM-dd
log4j.appender.FILE.MaxFileSize=50MB

log4j.appender.ERRFILE=org.apache.log4j.DailyRollingFileAppender
log4j.appender.ERRFILE.File=${log4jPath}/${log4jName}-error.log
log4j.appender.ERRFILE.layout=org.apache.log4j.PatternLayout
log4j.appender.ERRFILE.layout.ConversionPattern=%d [%t] %p [%c] - %m%n
log4j.appender.ERRFILE.Threshold=ERROR
log4j.appender.ERRFILE.DatePattern='.'yyyy-MM-dd
log4j.appender.ERRFILE.MaxFileSize=50MB

log4j.logger.${packageName}=DEBUG
