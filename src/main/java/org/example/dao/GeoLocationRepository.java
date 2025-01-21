package org.example.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.example.domain.GeoLocation;
import org.hibernate.SessionFactory;

import javax.persistence.Query;

public class GeoLocationRepository extends AbstractDAO<GeoLocation> {
    public GeoLocationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public GeoLocation findByIpAddress(String ipAddress) {
      return (GeoLocation)this.get(ipAddress);
    }

    public void save(GeoLocation geolocation) {
        this.persist(geolocation);
    }


}
