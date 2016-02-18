FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER "Kyle Allan"

RUN adduser -h /home/fmaas -s /bin/sh -D fmaas
USER fmaas

COPY target/uberjar/fmaas-0.1.0-SNAPSHOT-standalone.jar /home/fmaas/fmaas-uber.jar

ENTRYPOINT ["java", "-jar", "/home/fmaas/fmaas-uber.jar"]
