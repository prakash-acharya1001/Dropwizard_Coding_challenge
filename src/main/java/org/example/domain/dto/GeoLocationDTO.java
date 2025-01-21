package org.example.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeoLocationDTO {
    private String query;
    private String status;
    private String countryCode;
    private String country;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String timeZone;
    private float lon;
    private float lat;
    private String isp;
    private String org;
    private String as;
}
