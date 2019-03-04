# ---- builder ----

FROM openjdk:11-slim as builder
ENV DEBIAN_FRONTEND noninteractive

COPY ./aggregator /opt/sloth/aggregator
RUN ./opt/sloth/aggregator/gradlew -p /opt/sloth/aggregator build

# --- production ----

FROM openjdk:11-jre-slim as production
LABEL maintainer="pejman@ghorbanzade.com"
ENV DEBIAN_FRONTEND noninteractive

COPY --from=builder /opt/sloth/aggregator/build/libs /opt/sloth/aggregator/build/libs

RUN apt-get update && apt-get install -y --no-install-recommends \
  librxtx-java \
&& rm -rf /var/lib/apt/lists/*

RUN groupadd -r sloth && useradd --no-log-init -r -g sloth sloth
RUN chown -v -R sloth:sloth /opt/sloth
USER sloth

ENTRYPOINT [ "java", "-cp", "/opt/sloth/aggregator/build/libs/aggregator-0.3.jar", "com.ghorbanzade.sloth.SlothMain" ]
