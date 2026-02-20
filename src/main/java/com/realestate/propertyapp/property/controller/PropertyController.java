package com.realestate.propertyapp.property.controller;

import com.realestate.propertyapp.address.entity.Address;
import com.realestate.propertyapp.aws.service.S3StorageService;
import com.realestate.propertyapp.image.dto.PropertyImageDTO;
import com.realestate.propertyapp.image.entity.PropertyImage;
import com.realestate.propertyapp.property.dto.*;
import com.realestate.propertyapp.property.entity.Property;
import com.realestate.propertyapp.property.service.PropertyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Page<PropertyResponse> list(@PageableDefault(
            size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return service.list(pageable).map(this::toResponse);
    }

    // -- Handling Get Property Detail
    @GetMapping("/{id}")
    public PropertyResponse get(@PathVariable Long id) {
        Property p = service.get(id);
        PropertyResponse r = toResponse(p);

        r.propertyImages = mapPropertyImage(p.getImages());
        return r;
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
        r.imageUrl = p.getImages().stream().filter(PropertyImage::getPrimary).findFirst()
                .map(PropertyImage::getImageUrl).orElse(null);

        r.address = mapAddress(p.getAddress());
        //r.propertyImages = mapPropertyImages(p.getImages());
        return r;
    }

    private AddressResponse mapAddress(Address address) {
        if (address == null) return null;

        AddressResponse dto = new AddressResponse();
        dto.province = address.getProvince();
        dto.city = address.getCity();
        dto.district = address.getDistrict();
        dto.subDistrict = address.getSubDistrict();
        dto.postalCode = address.getPostalCode();
        return dto;
    }

    private List<PropertyImageResponse> mapPropertyImage(List<PropertyImage> propImagesInput) {
        if (CollectionUtils.isEmpty(propImagesInput)) {
            return List.of();
        }

        return propImagesInput.stream()
                .sorted(Comparator.comparing(
                        PropertyImage::getDisplayOrder,
                        Comparator.nullsLast(Integer::compareTo)
                ))
                .map(img -> {
                            PropertyImageResponse r = new PropertyImageResponse();
                            r.imageUrl = img.getImageUrl();
                            r.isPrimary = img.getPrimary();
                            r.displayOrder = img.getDisplayOrder();
                            return r;
                        })
                        .toList();
    }

    private PropertyImageDTO mapPropertyToDTO(PropertyImage propertyImage) {
        if (propertyImage == null) return null;

        PropertyImageDTO propRes = new PropertyImageDTO();
        propRes.propertyId = propertyImage.getProperty().getId();
        propRes.imageUrl = propertyImage.getImageUrl();
        return propRes;
    }

    @PostMapping("{id}/image")
    public ResponseEntity<?> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        String imageUrl = service.uploadPropertyImage(id, file);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }

    // -- Upload non primary images (add other images0
    @PostMapping("{id}/addImages")
    public List<PropertyImageDTO> addImages(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files) {

        return files.stream()
                .map(eachFile -> service.addImagesProperty(id, eachFile))
                .map(this::mapPropertyToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/search")
    public Page<PropertyResponse> search(
            @RequestBody PropertySearchRequest req,
            Pageable pageable
    ) {
        return service.search(req, pageable).map(this::toResponse);
    }
}
