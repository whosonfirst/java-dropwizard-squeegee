# squeegee

`squeegee` is a simple [Dropwizard]() HTTP pony to convert SVG documents in to PNG files using the [Batik]() SVG transcoder.

## Caveats

You should not try to use this yet. It does not work.

## How to start the squeegee application

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/squeegee.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

## Health Check

To see your applications health enter url `http://localhost:8081/healthcheck`

## See also

* https://xmlgraphics.apache.org/batik/using/transcoder.html