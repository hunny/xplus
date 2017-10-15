FROM hunnyhu/eureka-server:hunnyhu

LABEL PROJECT="eureka-server" \
      AUTHOR="hunnyhu" \
      COMPANY="Shanghai HunnyHu Co., Ltd."

WORKDIR /tmp

VOLUME /tmp

ADD /eureka-server-0.0.1-SNAPSHOT.jar app.jar

#RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8761