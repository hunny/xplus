FROM kurron/docker-oracle-jdk-8

LABEL PROJECT="eureka-server" \
      AUTHOR="hunnyhu" \
      COMPANY="Shanghai HunnyHu Co., Ltd."

VOLUME /tmp

COPY eureka-server-0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

EXPOSE 8761
