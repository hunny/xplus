FROM kurron/docker-oracle-jdk-8

LABEL PROJECT="eureka-client" \
      AUTHOR="hunnyhu" \
      COMPANY="Shanghai HunnyHu Co., Ltd."

VOLUME /tmp

COPY eureka-client-0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar", "--spring.config.name=application-docker"]

EXPOSE 8763
