package com.realestate.propertyapp.property.dto;

import java.time.LocalDateTime;

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
}
