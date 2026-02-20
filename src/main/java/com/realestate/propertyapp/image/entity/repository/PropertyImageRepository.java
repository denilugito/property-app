package com.realestate.propertyapp.image.entity.repository;

import com.realestate.propertyapp.image.entity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {

}
