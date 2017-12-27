FROM maven:alpine AS build-env

WORKDIR /opt/dropwizard

COPY pom.xml /opt/dropwizard

RUN mvn clean install

FROM openjdk:alpine

WORKDIR /opt/dropwizard

COPY config.yml /opt/dropwizard/

COPY --from=build-env target/squeegee-0.1.jar /opt/dropwizard/

EXPOSE 8080

CMD ["java", "-jar", "-Done-jar.silent=true", "/opt/dropwizard/squeegee-0.1.jar", "server", "/opt/dropwizard/config.yml"]
