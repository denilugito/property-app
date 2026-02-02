package com.realestate.propertyapp.property.dto;

import lombok.Data;

@Data
public class PropertySearchRequest {

    private String keyword;

    private String type; // Sale / Rent
    private Long minPrice;
    private Long maxPrice;

    private Integer minBedrooms;

    private String province;
    private String city;
    private String district;

    private Boolean hasPanorama;
}
