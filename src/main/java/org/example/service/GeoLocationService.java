package org.example.service;
import org.example.domain.GeoLocation;
import org.example.domain.dto.*;
public interface GeoLocationService {
    GeoLocationDTO getGeoLocationFromAPI(String ipAddress);

    void saveGeoLocation(String ipAddress, GeoLocation geoLocation);

    GeoLocation getGeoLocationFromDB(String ipAddress);
}
