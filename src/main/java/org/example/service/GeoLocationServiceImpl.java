package org.example.service;

import org.example.dao.GeoLocationRepository;
import org.example.domain.GeoLocation;
import org.example.domain.dto.GeoLocationDTO;
import org.example.exceptions.ClientApiException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import java.time.LocalDateTime;

public class GeoLocationServiceImpl implements GeoLocationService {
    private final String getURL = "http://ip-api.com/json/";
    private final GeoLocationRepository geoLocationRepository;
    private Client client;

    private final  ModelMapper modelMaper =new ModelMapper();
    private static Logger log = LoggerFactory.getLogger(GeoLocationService.class);

    public GeoLocationServiceImpl(GeoLocationRepository geoLocationRepository, Client client ) {
        this.geoLocationRepository = geoLocationRepository;
        this.client = client;

    }

    public GeoLocationDTO getGeoLocationFromAPI(String ipAddress) {
        GeoLocationDTO geolocationDTO;
        GeoLocation geoLocation;
        try {
            log.info("Call form getGeoLocationFromAPI's try block  for IpAddress  : " + ipAddress);
            geoLocation = this.getGeoLocationFromDB(ipAddress);
            if (LocalDateTime.now().minusMinutes(5L).isAfter(geoLocation.getUpdateTime())) {
                geoLocation = (GeoLocation)this.client.target(getURL + ipAddress).request().get().readEntity(GeoLocation.class);
            }

            geolocationDTO = modelMaper.map(geoLocation,GeoLocationDTO.class);
        } catch (ClientApiException ex) {
            log.info("External API is called from getGeoLocationFromAPI's catch block as data is not available in database  : " + ipAddress);
            geoLocation = (GeoLocation)this.client.target(getURL + ipAddress).request().get().readEntity(GeoLocation.class);
            if (geoLocation.getStatus().equalsIgnoreCase("success")) {
                this.saveGeoLocation(ipAddress, geoLocation);
            }

            geolocationDTO = modelMaper.map(geoLocation,GeoLocationDTO.class);
        }

        return geolocationDTO;
    }

    public void saveGeoLocation(String ipAddress, GeoLocation geolocation) {
        log.info("Geolocation data received from External API is saved  in database with ip address: " + ipAddress);
        geolocation.setIpAddress(ipAddress);
        this.geoLocationRepository.save(geolocation);
    }

    public GeoLocation getGeoLocationFromDB(String ipAddress) {
        log.info("getGeoLocationFromDB is executing to retrieve geo data from Database: " + ipAddress);
        GeoLocation geoLocation = this.geoLocationRepository.findByIpAddress(ipAddress);
        if (geoLocation != null) {
            log.info("GeoLocation data is available in database:"+ipAddress);
            return geoLocation;
        } else {
              throw new ClientApiException("GeoLocation data is not available in database: Hence Throwing runtime exception to external API ");
        }
    }
}
