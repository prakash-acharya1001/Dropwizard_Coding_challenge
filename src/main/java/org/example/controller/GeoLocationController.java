package org.example.controller;
//import caching.GeolocationCaching;
import com.codahale.metrics.annotation.Timed;
//import domain.dto.ApiResponse;
//import domain.dto.GeolocationDTO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.caching.CacheControl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.example.cache.GeoLocationCache;
import org.example.domain.GeoLocation;
import org.example.domain.dto.GeoLocationDTO;
import org.example.service.GeoLocationService;

@Path("/geolocation")
@Produces({"application/json"})
public class GeoLocationController {
    private final GeoLocationService geolocationService;

    public GeoLocationController(GeoLocationService geolocationService) {
        this.geolocationService = geolocationService;
    }

    @GET
    @Timed
    @Path("/{ipAddress}")
    @UnitOfWork
    @CacheControl(
            maxAge = 1,
            maxAgeUnit = TimeUnit.MINUTES
    )
    public GeoLocationDTO getGeolocation(@PathParam("ipAddress") @Pattern(
            regexp = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$",
            message = "IP address must be in the format: 0.0.0.0 to 255.255.255.255"
    ) String ipAddress) {
        GeoLocationDTO geolocationDTO = GeoLocationCache.getInstance(geolocationService).getGeolocationDataFromCache(ipAddress);

               return geolocationDTO;
    }



    @GET
    @Path("/fromapi/{ipAddress}")
    @UnitOfWork

    public GeoLocationDTO getGeoLocationFromAPI(@PathParam("ipAddress") String ipAddress) {
        return geolocationService.getGeoLocationFromAPI(ipAddress);
    }
    @GET
    @Path("/fromdb/{ipAddress}")
    @UnitOfWork
    public GeoLocation getGeoLocationFromDB(@PathParam("ipAddress") String ipAddress) {
        return geolocationService.getGeoLocationFromDB(ipAddress);
    }

}
