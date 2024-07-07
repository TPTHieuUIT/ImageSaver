package com.bezkoder.spring.thymeleaf.image.upload.service;

import com.bezkoder.spring.thymeleaf.image.upload.model.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<ImageInfo, Long> {
    ImageInfo findByfilename(String imageName);
}
