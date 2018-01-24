package com.example.bootweb.accessory.druid;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * <code>-Dspring.druid.disable=true</code> to disable druid datasource.
 * <p>
 * <code>-Dspring.druid.disable=false</code> to enable druid datasource.
 * <p>
 * default to enable.
 * 
 */
@ConfigurationProperties(prefix = "spring.datasource")
@Component
@ConditionalOnProperty(havingValue = "false", //
    prefix = "spring.druid", //
    name = "disable", //
    matchIfMissing = true //
) //
public class DruidConfigValue {

  private String url;

  private String username;

  private String password;

  private String driverClassName;

  // 初始化大小
  private int initialSize = 5;

  // 最小
  private int minIdle = 5;

  // 最大
  private int maxActive = 20;

  // 配置获取连接等待超时的时间
  private int maxWait = 60000;

  // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
  private int timeBetweenEvictionRunsMillis = 60000;

  // 配置一个连接在池中最小生存的时间，单位是毫秒
  private int minEvictableIdleTimeMillis = 300000;

  private String validationQuery = "SELECT 1 FROM DUAL";

  private boolean testWhileIdle = true;
  private boolean testOnBorrow = false;

  private boolean testOnReturn = false;

  private boolean poolPreparedStatements = true;

  private int maxPoolPreparedStatementPerConnectionSize = 20;

  // 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
  private String filters = "stat,wall,log4j";

  // 通过connectProperties属性来打开mergeSql功能；慢SQL记录
  private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getDriverClassName() {
    return driverClassName;
  }

  public void setDriverClassName(String driverClassName) {
    this.driverClassName = driverClassName;
  }

  public int getInitialSize() {
    return initialSize;
  }

  public void setInitialSize(int initialSize) {
    this.initialSize = initialSize;
  }

  public int getMinIdle() {
    return minIdle;
  }

  public void setMinIdle(int minIdle) {
    this.minIdle = minIdle;
  }

  public int getMaxActive() {
    return maxActive;
  }

  public void setMaxActive(int maxActive) {
    this.maxActive = maxActive;
  }

  public int getMaxWait() {
    return maxWait;
  }

  public void setMaxWait(int maxWait) {
    this.maxWait = maxWait;
  }

  public int getTimeBetweenEvictionRunsMillis() {
    return timeBetweenEvictionRunsMillis;
  }

  public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
    this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
  }

  public int getMinEvictableIdleTimeMillis() {
    return minEvictableIdleTimeMillis;
  }

  public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
    this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
  }

  public String getValidationQuery() {
    return validationQuery;
  }

  public void setValidationQuery(String validationQuery) {
    this.validationQuery = validationQuery;
  }

  public boolean isTestWhileIdle() {
    return testWhileIdle;
  }

  public void setTestWhileIdle(boolean testWhileIdle) {
    this.testWhileIdle = testWhileIdle;
  }

  public boolean isTestOnBorrow() {
    return testOnBorrow;
  }

  public void setTestOnBorrow(boolean testOnBorrow) {
    this.testOnBorrow = testOnBorrow;
  }

  public boolean isTestOnReturn() {
    return testOnReturn;
  }

  public void setTestOnReturn(boolean testOnReturn) {
    this.testOnReturn = testOnReturn;
  }

  public boolean isPoolPreparedStatements() {
    return poolPreparedStatements;
  }

  public void setPoolPreparedStatements(boolean poolPreparedStatements) {
    this.poolPreparedStatements = poolPreparedStatements;
  }

  public int getMaxPoolPreparedStatementPerConnectionSize() {
    return maxPoolPreparedStatementPerConnectionSize;
  }

  public void setMaxPoolPreparedStatementPerConnectionSize(
      int maxPoolPreparedStatementPerConnectionSize) {
    this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
  }

  public String getFilters() {
    return filters;
  }

  public void setFilters(String filters) {
    this.filters = filters;
  }

  public String getConnectionProperties() {
    return connectionProperties;
  }

  public void setConnectionProperties(String connectionProperties) {
    this.connectionProperties = connectionProperties;
  }

}
