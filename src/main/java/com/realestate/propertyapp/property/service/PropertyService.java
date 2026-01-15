package com.realestate.propertyapp.property.service;

import com.realestate.propertyapp.property.dto.PropertyRequest;
import com.realestate.propertyapp.property.entity.Property;
import com.realestate.propertyapp.property.repository.PropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    private final PropertyRepository repository;

    public PropertyService(PropertyRepository repository) {
        this.repository = repository;
    }

    public Property create(PropertyRequest request) {
        Property p = new Property();
        p.setTitle(request.title);
        p.setDescription(request.description);
        p.setPrice(request.price);
        p.setType(request.type);
        p.setBedrooms(request.bedrooms);
        p.setBathrooms(request.bathrooms);
        p.setArea(request.area);
        return repository.save(p);
    }

    public Page<Property> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Property get(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Property not found"));
    }
}
