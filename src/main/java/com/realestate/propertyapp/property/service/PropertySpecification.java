package com.realestate.propertyapp.property.service;

import com.realestate.propertyapp.address.entity.Address;
import com.realestate.propertyapp.property.dto.PropertySearchRequest;
import com.realestate.propertyapp.property.entity.Property;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class PropertySpecification {

    public static Specification<Property> searchSpec(PropertySearchRequest req) {
        return (root, query, cb) -> {
            if (req.getKeyword() == null || req.getKeyword().isBlank()) return null;

            List<Predicate> predicates = new ArrayList<>();
            Join<Property, Address> address = root.join("address", JoinType.LEFT);

            String like = "%" + req.getKeyword().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("description")), like),
                    cb.like(cb.lower(address.get("city")), like),
                    cb.like(cb.lower(address.get("subDistrict")), like)
            ));

            // Type
            if (req.getType() != null) {
                predicates.add(cb.equal(root.get("type"), req.getType()));
            }

            // Price range
            if (req.getMinPrice() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), req.getMinPrice()));
            }

            if (req.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), req.getMaxPrice()));
            }

            // Bedrooms
            if (req.getMinBedrooms() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("bedrooms"), req.getMinBedrooms()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
