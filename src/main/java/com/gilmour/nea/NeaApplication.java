package com.gilmour.nea;

import com.gilmour.nea.core.ProtocolNumberConverter;
import com.gilmour.nea.model.ConnectionSummary;
import com.gilmour.nea.resources.ParquetResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class NeaApplication extends Application<NeaConfiguration> {

    public static void main(final String[] args) throws Exception {
        new NeaApplication().run(args);
    }


    private final HibernateBundle<NeaConfiguration> hibernate = new HibernateBundle<NeaConfiguration>(ConnectionSummary.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(NeaConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    @Override
    public String getName() {
        return "Network Event Analysis";
    }

    @Override
    public void initialize(final Bootstrap<NeaConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);

    }

    @Override
    public void run(final NeaConfiguration configuration,
                    final Environment environment) {

        ProtocolNumberConverter.getInstance().init();

        // Enabling MultiPartFeature, injects necessary message body readers, writers for Jersey 2 application.
        environment.jersey().register(MultiPartFeature.class);

        // Register rest endpoints
        environment.jersey().register(new ParquetResource(configuration.getUploadLocation()));

    }

}
