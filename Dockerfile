FROM --platform=linux/arm64/v8 ubuntu:20.04

ARG DEBIAN_FRONTEND=noninteractive
EXPOSE 8080

RUN apt update \
    && apt upgrade -y \
    && apt install -y openjdk-17-jre git \
    && apt clean

COPY target/mancala-0.0.1-SNAPSHOT.jar mancala-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/mancala-0.0.1-SNAPSHOT.jar"]
