package com.mapzen.whosonfirst.squeegee;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.forms.MultiPartBundle;

import java.net.URL;

import com.mapzen.whosonfirst.squeegee.squeegeeConfiguration;

public class squeegeeApplication extends Application<squeegeeConfiguration> {

    public static void main(final String[] args) throws Exception {
        new squeegeeApplication().run(args);
    }

    @Override
    public String getName() {
        return "squeegee";
    }

    @Override
    public void initialize(final Bootstrap<squeegeeConfiguration> bootstrap) {
	bootstrap.addBundle(new MultiPartBundle());	
    }

    @Override
    public void run(final squeegeeConfiguration configuration,
                    final Environment environment) {

	final BatikResource batik = new BatikResource();	
	environment.jersey().register(batik);

	// URL healthcheck_url = new URL("http://collection.cooperhewitt.org/objects/random/");
	// environment.addHealthCheck(new InternetsHealthCheck(healthcheck_url));
    }

}
