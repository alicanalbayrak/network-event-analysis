package com.gilmour.nea;

import com.gilmour.nea.core.ConnectionSummaryDaoProxy;
import com.gilmour.nea.core.ProtocolNumberConverter;
import com.gilmour.nea.db.ConnectionSummaryDAO;
import com.gilmour.nea.model.ConnectionSummary;
import com.gilmour.nea.resources.ConnectionSummaryResource;
import com.gilmour.nea.resources.FileUploadResource;
import com.gilmour.nea.service.ParquetService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeaApplication extends Application<NeaConfiguration> {

    private final Logger logger = LoggerFactory.getLogger(NeaApplication.class);

    public static void main(final String[] args) throws Exception {
        new NeaApplication().run(args);
    }


    private final HibernateBundle<NeaConfiguration> hibernateBundle = new HibernateBundle<NeaConfiguration>(ConnectionSummary.class) {
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

        bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
        bootstrap.addBundle(hibernateBundle);

    }

    @Override
    public void run(final NeaConfiguration configuration,
                    final Environment environment) {

        logger.info("running Nea");

        final ConnectionSummaryDAO connectionSummaryDAO = new ConnectionSummaryDAO(hibernateBundle.getSessionFactory());
        ConnectionSummaryDaoProxy connectionSummaryDaoProxy = new UnitOfWorkAwareProxyFactory(hibernateBundle)
                .create(ConnectionSummaryDaoProxy.class, ConnectionSummaryDAO.class, connectionSummaryDAO);

        ProtocolNumberConverter.getInstance().init();
        ParquetService.getInstance().init(connectionSummaryDaoProxy);

        // Enabling MultiPartFeature, injects necessary message body readers, writers for Jersey 2 application.
        environment.jersey().register(MultiPartFeature.class);

        // Register rest endpoints
        environment.jersey().register(new FileUploadResource(configuration.getUploadLocation()));
        environment.jersey().register(new ConnectionSummaryResource(connectionSummaryDAO));

    }

}
