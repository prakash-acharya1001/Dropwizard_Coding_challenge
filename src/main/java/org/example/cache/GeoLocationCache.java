package org.example.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import org.example.domain.dto.GeoLocationDTO;
import org.example.service.GeoLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GeoLocationCache {
    private static final Logger logger = LoggerFactory.getLogger(GeoLocationCache.class);
    private  static  GeoLocationCache geoLocationCache ;
    private static LoadingCache<String, GeoLocationDTO> geoLoadingCache;


    private  GeoLocationCache(GeoLocationService geoLocationService){

        if(geoLoadingCache==null){
            geoLoadingCache= CacheBuilder.newBuilder()
                    .concurrencyLevel(10)
                    .maximumSize(100)                             // maximum 100 records can be cached
                    .expireAfterAccess(30, TimeUnit.MINUTES)// cache will expire after 30 minutes of access
                    .recordStats()
                    .build(new CacheLoader<String, GeoLocationDTO>() {  // build the cacheloader

                        @Override
                        public GeoLocationDTO load(String ipAddress) throws Exception {
                            //make the expensive call
                            // return getFromDatabase(empId);
                            return  geoLocationService.getGeoLocationFromAPI(ipAddress);
                        }
                    });


        }


    }
    public static GeoLocationCache getInstance(GeoLocationService geoLocationService) {
        if (geoLocationCache==null) geoLocationCache=new GeoLocationCache(geoLocationService);
        return geoLocationCache ;
    }

    public GeoLocationDTO getGeolocationDataFromCache(String ip) {
        try {
            CacheStats cacheStats = geoLoadingCache.stats();
            logger.info("CacheStats = {} ", cacheStats);
            return geoLoadingCache.get(ip);
        } catch (ExecutionException e) {
            logger.error("Error Retrieving Elements from the GeoLocation  Cache"
                    + e.getMessage());
        }
        return null;
    }

}
