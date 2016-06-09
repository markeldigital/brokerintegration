package com.markelintl.pq.devserver;

import com.markelintl.pq.devserver.resources.PolicyReferenceResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DevServer extends Application<DevServerConfiguration> {
    public static void main(String[] args) throws Exception {
        new DevServer().run(args);
    }

    @Override
    public String getName() {
       return "referral-stub";
    }

    @Override
    public void initialize(Bootstrap<DevServerConfiguration> bootstrap) {

    }

    @Override
    public void run(DevServerConfiguration devServerConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new PolicyReferenceResource());
    }
}
