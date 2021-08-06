FROM maven:3.8.1-adoptopenjdk-11
MAINTAINER Goudham Suresh

RUN apt-get update && apt-get install -y \
    gpg \
    tightvncserver
RUN vncserver -y
RUN vncserver -kill :1