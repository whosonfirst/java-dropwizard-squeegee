FROM maven:alpine AS build-env

WORKDIR /opt/dropwizard

COPY pom.xml /opt/dropwizard

RUN mvn clean install

FROM openjdk:alpine

WORKDIR /opt/dropwizard

COPY config.yml /opt/dropwizard/

# what... ?
# Step 8/10 : COPY --from=build-env build/libs/docker-dropwizard-application-standalone.jar /opt/dropwizard/
# COPY failed: stat /var/lib/docker/overlay2/73e140b011a8965126bf360998acb8d1cee53c2b5e87b2b9e7ba5bd43e86aed7/merged/build/libs/docker-dropwizard-application-standalone.jar: no such file or directory

COPY --from=build-env build/libs/docker-dropwizard-application-standalone.jar /opt/dropwizard/

EXPOSE 8080

CMD ["java", "-jar", "-Done-jar.silent=true", "/opt/dropwizard/docker-dropwizard-application-standalone.jar", "server", "/opt/dropwizard/config.yml"]
