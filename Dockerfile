FROM maven:alpine AS build-env
# FROM java:8-jre

WORKDIR /opt/dropwizard

COPY pom.xml /opt/dropwizard

RUN mvn clean install

FROM openjdk:alpine

WORKDIR /opt/dropwizard

COPY config.yml /opt/dropwizard/
COPY --from=build-env build/libs/docker-dropwizard-application-standalone.jar /opt/dropwizard/

EXPOSE 8080

CMD ["java", "-jar", "-Done-jar.silent=true", "/opt/dropwizard/docker-dropwizard-application-standalone.jar", "server", "/opt/dropwizard/config.yml"]
