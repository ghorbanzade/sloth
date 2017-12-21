FROM openjdk:8-slim
MAINTAINER Pejman Ghorbanzade <pejman@ghorbanzade.com>

COPY . /sloth
WORKDIR /sloth

CMD /bin/bash
