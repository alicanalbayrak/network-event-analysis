package com.gilmour.nea;

import com.gilmour.nea.resources.ParquetResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class NeaApplication extends Application<NeaConfiguration> {

    public static void main(final String[] args) throws Exception {
        new NeaApplication().run(args);
    }

    @Override
    public String getName() {
        return "Network Event Analysis";
    }

    @Override
    public void initialize(final Bootstrap<NeaConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final NeaConfiguration configuration,
                    final Environment environment) {

        // Enabling MultiPartFeature, injects necessary message body readers, writers for Jersey 2 application.
        environment.jersey().register(MultiPartFeature.class);

        // Register rest endpoints
        environment.jersey().register(new ParquetResource(configuration.getUploadLocation()));

    }

}
