package org.example.application;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.example.cache.GeoLocationCache;
import org.example.configuration.GeoLocationConfiguration;
import org.example.controller.GeoLocationController;
import org.example.dao.GeoLocationRepository;
import org.example.domain.GeoLocation;
import org.example.service.GeoLocationService;
import org.example.service.GeoLocationServiceImpl;

import javax.ws.rs.client.Client;


public class GeoLocationApplicationForHilton extends Application<GeoLocationConfiguration> {

    public GeoLocationApplicationForHilton() {
    }

    public static void main(String[] args) throws Exception {
        (new GeoLocationApplicationForHilton()).run(args);
    }

    public String getName() {
        return "Hilton";
    }
    private final HibernateBundle<GeoLocationConfiguration> hibernate = new HibernateBundle<GeoLocationConfiguration>(GeoLocation.class) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(GeoLocationConfiguration geolocationConfiguration) {
            return geolocationConfiguration.getDataSourceFactory();
        }
    };
    public void initialize(Bootstrap<GeoLocationConfiguration> bootstrap) {
        bootstrap.addBundle(this.hibernate);
    }

    public void run(GeoLocationConfiguration geolocationConfiguration, Environment environment) throws Exception {


        final GeoLocationRepository geoLocationRepository = new GeoLocationRepository(this.hibernate.getSessionFactory());
        final Client client = (new JerseyClientBuilder(environment)).using(new JerseyClientConfiguration()).build(this.getName());
       final GeoLocationService geolocationService = new GeoLocationServiceImpl(geoLocationRepository, client);
        final GeoLocationCache cacheInstance = GeoLocationCache.getInstance(geolocationService);
        final  GeoLocationController controller = new GeoLocationController(geolocationService);
        environment.jersey().register(controller);

    }

}
