package com.realestate.propertyapp.property.controller;

import com.realestate.propertyapp.property.dto.PropertyRequest;
import com.realestate.propertyapp.property.dto.PropertyResponse;
import com.realestate.propertyapp.property.entity.Property;
import com.realestate.propertyapp.property.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyService service;

    public PropertyController(PropertyService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public PropertyResponse create(@RequestBody PropertyRequest request) {
        return toResponse(service.create(request));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping()
    public Page<PropertyResponse> list(@PageableDefault(size = 10) Pageable pageable) {
        return service.list(pageable).map(this::toResponse);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public PropertyResponse get(@PathVariable Long id) {
        return toResponse(service.get(id));
    }

    private PropertyResponse toResponse(Property p) {
        PropertyResponse r = new PropertyResponse();
        r.id = p.getId();
        r.title = p.getTitle();
        r.price = p.getPrice();
        r.type = p.getType();
        r.hasPanorama = p.getHasPanorama();
        return r;
    }
}
