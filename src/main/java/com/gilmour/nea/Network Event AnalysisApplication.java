package com.gilmour.nea;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class Network Event AnalysisApplication extends Application<Network Event AnalysisConfiguration> {

    public static void main(final String[] args) throws Exception {
        new Network Event AnalysisApplication().run(args);
    }

    @Override
    public String getName() {
        return "Network Event Analysis";
    }

    @Override
    public void initialize(final Bootstrap<Network Event AnalysisConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final Network Event AnalysisConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
