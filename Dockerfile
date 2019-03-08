FROM openjdk:11-jre-slim
MAINTAINER Evan "Hippy" Slatis <hippyod@yahoo.com>

VOLUME /tmp
ARG JAR_FILE

ENV _JAVA_OPTIONS "-Xms512m -Xmx1024m -Djava.awt.headless=true"

COPY ${JAR_FILE} app.jar

RUN useradd -ms /bin/bash bootapp
    
EXPOSE 8080

USER bootapp
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
