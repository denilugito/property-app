package com.realestate.propertyapp.property.service;

import com.realestate.propertyapp.aws.service.S3StorageService;
import com.realestate.propertyapp.property.dto.PropertyRequest;
import com.realestate.propertyapp.property.dto.PropertySearchRequest;
import com.realestate.propertyapp.property.entity.Property;
import com.realestate.propertyapp.property.repository.PropertyRepository;
import com.realestate.propertyapp.user.entity.User;
import com.realestate.propertyapp.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class PropertyService {

    private final PropertyRepository repository;
    private final UserRepository userRepo;
    private final S3StorageService storageService;

    public PropertyService(PropertyRepository repository, S3StorageService storageService, UserRepository userRepo) {
        this.repository = repository;
        this.storageService = storageService;
        this.userRepo = userRepo;
    }

    public Property create(PropertyRequest request) {
        Optional<User> agentPropHardCode = userRepo.findById(1L);

        Property p = new Property();
        p.setTitle(request.title);
        p.setDescription(request.description);
        p.setPrice(request.price);
        p.setType(request.type);
        p.setBedrooms(request.bedrooms);
        p.setBathrooms(request.bathrooms);
        p.setArea(request.area);

        agentPropHardCode.ifPresent(p::setAgent);
        return repository.save(p);
    }

    public Page<Property> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Property get(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Property not found"));
    }

    public String uploadPropertyImage(Long propertyId, MultipartFile file) {
        Property property = get(propertyId);
        String imageUrl = storageService.uploadPropertyImage(file, propertyId);

        property.setImageUrl(imageUrl);
        repository.save(property);

        return imageUrl;
    }

    public Page<Property> search(
            PropertySearchRequest propReq, Pageable pageable
    ) {
        return repository.findAll(PropertySpecification.searchSpec(propReq),
                pageable);
    }
}
