package com.bezkoder.spring.thymeleaf.image.upload.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file);

  public void saveAndRename(MultipartFile file, String filename);

  public Resource load(String filename);

  public boolean delete(String filename);
  
  public void deleteAll();

  public Stream<Path> loadAll();
}
