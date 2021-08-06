FROM maven:3.8.1-adoptopenjdk-11
MAINTAINER Goudham Suresh

RUN apt-get update && apt-get install -y \
    gpg \
    xvfb \
    libxrender1 libxtst6 libxi6 libxext6
RUN /usr/bin/Xvfb :99 &
RUN export DISPLAY=:99