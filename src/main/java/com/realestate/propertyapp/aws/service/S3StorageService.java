package com.realestate.propertyapp.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3StorageService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String uploadPropertyImage(MultipartFile file, Long propertyId) {
        try {
//            String key = "properties%d%s".formatted(propertyId, UUID.randomUUID() + "-" + file.getOriginalFilename());
            String key = "properties/%d/%s-%s".formatted(propertyId, UUID.randomUUID(), file.getOriginalFilename());

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return "https://%s.s3.amazonaws.com/%s".formatted(bucket, key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
