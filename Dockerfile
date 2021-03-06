FROM openjdk:11-jre-slim
MAINTAINER Evan "Hippy" Slatis <hippyod@yahoo.com>

VOLUME /tmp
ARG JAR_FILE

ENV _JAVA_OPTIONS "-Xms512m -Xmx1024m -Djava.awt.headless=true"

COPY ${JAR_FILE} /opt/app.jar
    
EXPOSE 80

WORKDIR /opt
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/app.jar"]