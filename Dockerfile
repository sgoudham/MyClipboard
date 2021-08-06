FROM maven:3.8.1-adoptopenjdk-11
MAINTAINER Goudham Suresh

RUN apt-get update && apt-get install -y \
    gpg \
    xvfb \
    libxrender1:i386 libxtst6:i386 libxi6:i386
RUN export MAVEN_OPTS="-Djava.awt.headless=true"
RUN /usr/bin/Xvfb :99 &
RUN export DISPLAY=:99