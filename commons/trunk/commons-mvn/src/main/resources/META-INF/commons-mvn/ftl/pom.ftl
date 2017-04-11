<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright(C), Xplus Co., Ltd, 2016. All rights reserved. -->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <#if parent??><parent>
    <groupId>${parent.groupId}</groupId>
    <artifactId>${parent.artifactId}</artifactId>
    <version>${parent.version}</version>
  </parent></#if>
  <#if artifactId??><artifactId>${artifactId}</artifactId></#if>
  <#if name??><name>Maven Tools</name></#if>
  <#if description??><description>Maven Tools.</description></#if>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
        <configuration>
          <doctitle><![CDATA[${r"${app_name}"} - Maven (${r"${project.version}"})]]></doctitle>
          <quiet>true</quiet>
          <detectJavaApiLink>true</detectJavaApiLink>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <executions>
          <execution>
            <phase>package</phase>
            <goals>
              <goal>jar-no-fork</goal>
            </goals>
            <configuration>
            </configuration>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>

  <reporting>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <configuration>
          <doctitle><![CDATA[${r"${app_name}"} - Maven (${r"${project.version}"})]]></doctitle>
          <quiet>true</quiet>
          <detectJavaApiLink>true</detectJavaApiLink>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-project-info-reports-plugin</artifactId>
        <reportSets>
          <reportSet>
            <reports>
              <report>index</report>
              <report>summary</report>
              <report>dependencies</report>
              <report>plugins</report>
            </reports>
          </reportSet>
        </reportSets>
      </plugin>
    </plugins>
  </reporting>
</project>