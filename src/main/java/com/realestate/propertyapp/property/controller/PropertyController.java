package com.realestate.propertyapp.property.controller;

import com.realestate.propertyapp.aws.service.S3StorageService;
import com.realestate.propertyapp.property.dto.PropertyRequest;
import com.realestate.propertyapp.property.dto.PropertyResponse;
import com.realestate.propertyapp.property.entity.Property;
import com.realestate.propertyapp.property.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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

    @GetMapping({"", "/"})
    public Page<PropertyResponse> list(@PageableDefault(size = 10) Pageable pageable) {
        return service.list(pageable).map(this::toResponse);
    }

    @GetMapping("/{id}")
    public PropertyResponse get(@PathVariable Long id) {
        return toResponse(service.get(id));
    }

    private PropertyResponse toResponse(Property p) {
        PropertyResponse r = new PropertyResponse();
        r.id = p.getId();
        r.title = p.getTitle();
        r.description = p.getDescription();
        r.price = p.getPrice();
        r.type = p.getType();
        r.bedrooms = p.getBedrooms();
        r.bathrooms = p.getBathrooms();
        r.area = p.getArea();
        r.hasPanorama = p.getHasPanorama();
        r.createdAt = p.getCreatedAt();
        r.agentId = p.getAgent().getId();
        r.agentName = p.getAgent().getFullname();
        r.imageUrl = p.getImageUrl();
        return r;
    }

    @PostMapping("{id}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        String imageUrl = service.uploadPropertyImage(id, file);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }
}
