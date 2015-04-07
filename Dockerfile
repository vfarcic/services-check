# Version: 0.1
FROM ubuntu:14.04
MAINTAINER Viktor Farcic "viktor@farcic.com"

# Packages
RUN apt-get update && \
    apt-get -y install --no-install-recommends openjdk-7-jdk wget && \
    rm -rf /var/lib/apt/lists/*
RUN wget -q http://downloads.typesafe.com/scala/2.11.5/scala-2.11.5.deb && \
    dpkg -i scala-2.11.5.deb && \
    rm scala-2.11.5.deb
RUN wget -q https://dl.bintray.com/sbt/debian/sbt-0.13.7.deb && \
    dpkg -i sbt-0.13.7.deb && \
    rm sbt-0.13.7.deb

RUN mkdir /app
WORKDIR /app
ADD src /app/src
ADD project /app/project
ADD application.conf /app/application.conf
ADD build.sbt /app/build.sbt
ADD run.sh /app/run.sh

RUN sbt test:compile

ENV DOMAIN http://172.17.42.1
ENV CONN_TIMEOUT_MS 1000
ENV READ_TIMEOUT_MS 5000

VOLUME ["/app/application.conf"]

CMD ["/app/run.sh"]