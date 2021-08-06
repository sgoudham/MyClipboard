FROM maven:3.8.1-adoptopenjdk-11
MAINTAINER Goudham Suresh

RUN apt-get update && apt-get install -y \
    gpg \
    xvfb
RUN export MAVEN_OPTS="-Djava.awt.headless=true"
RUN /usr/bin/Xvfb :99 &
RUN export DISPLAY=:99