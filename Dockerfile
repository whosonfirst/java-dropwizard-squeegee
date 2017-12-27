# docker build -t squeegee .

# $> docker run -it -p 6161:8080 squeegee

FROM maven:alpine AS build-env

ADD . /build

RUN cd /build; mvn clean install

FROM openjdk:alpine

WORKDIR /opt/dropwizard

COPY --from=build-env /build/config.yml /opt/dropwizard/
COPY --from=build-env /build/target/squeegee-0.1.jar /opt/dropwizard/

EXPOSE 8080

CMD ["java", "-jar", "-Done-jar.silent=true", "/opt/dropwizard/squeegee-0.1.jar", "server", "/opt/dropwizard/config.yml"]
