# squeegee

`squeegee` is a simple [Dropwizard-based](http://www.dropwizard.io/) HTTP pony to convert SVG documents in to PNG files using the [Batik](https://xmlgraphics.apache.org/batik) SVG transcoder.

## Caveats

You should not try to use this yet. It does not work.

## How to start the squeegee application

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/squeegee.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

## Usage

```
curl -s -v -X POST -F svg=@example.svg http://localhost:8080
```

## Health Check

To see your applications health enter url `http://localhost:8081/healthcheck`

## See also

* https://xmlgraphics.apache.org/batik/using/transcoder.html
* http://www.dropwizard.io/1.0.0/docs/index.html
* https://github.com/straup/java-ws-raster/blob/master/src/info/aaronland/svg/WsRaster.java