package com.realestate.propertyapp.property.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

public class PropertyResponse {
    public Long id;
    public String title;
    public String description;
    public Long price;
    public String type;
    public Integer bedrooms;
    public Integer bathrooms;
    public Integer area;
    public Boolean hasPanorama;
    public LocalDateTime createdAt;
    public String imageUrl;

    // Agent Information (flattened)
    public Long agentId;
    public String agentName;

    public AddressResponse address;

    // -- Tidak Dibutuhkan, Untuk Listing PropertyImages, dari table PropertyImages
    // -- turunkan ke imageUrl di PorpertyResponse
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PropertyImageResponse> propertyImages;
}
