package com.bezkoder.spring.thymeleaf.image.upload.controller;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import com.bezkoder.spring.thymeleaf.image.upload.service.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bezkoder.spring.thymeleaf.image.upload.model.ImageInfo;
import com.bezkoder.spring.thymeleaf.image.upload.service.FilesStorageService;

@Controller
public class ImageController {


  public ImageController(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  private final ImageRepository imageRepository;


  @Autowired
  FilesStorageService storageService;

  @GetMapping("/")
  public String homepage() {
    return "redirect:/images";
  }

  @GetMapping("/images/new")
  public String newImage(Model model) {
    return "upload_form";
  }

  @PostMapping("/images/upload")
  public String uploadImage(Model model, @RequestParam("file") MultipartFile file, @RequestParam("name") String filename) {
    String message = "";

    try {
      String fileNameWithExt = filename + "." + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
      storageService.saveAndRename(file, fileNameWithExt);
      ImageInfo imageInfo = new ImageInfo();
      imageInfo.setFileName(fileNameWithExt);
      imageInfo.setDateadd(Date.from(Instant.now()));
      imageInfo.setUrl("images/" + fileNameWithExt);
      //      imageInfo.setUrl(MvcUriComponentsBuilder
//              .fromMethodName(ImageController.class, "getImage", fileNameWithExt).build().toString());
//      storageService.loadAll().
      imageRepository.save(imageInfo);
      message = "Uploaded the image successfully: " + filename;
      model.addAttribute("message", message);
    } catch (Exception e) {
      message = "Could not upload the image: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
      model.addAttribute("message", message);
    }

    return "upload_form";
  }

  @GetMapping("/images")
  public String getListImages(Model model) {
      List<ImageInfo> imageInfos = imageRepository.findAll();
      model.addAttribute("images", imageInfos);
      return "images";
  }

  @GetMapping("/images/{filename:.+}")
  public ResponseEntity<Resource> getImage(@PathVariable String filename) {
    Resource file = storageService.load(filename);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @GetMapping("/images/delete/{filename:.+}")
  public String deleteImage(@PathVariable String filename, Model model, RedirectAttributes redirectAttributes) {
    try {
      boolean existed = storageService.delete(filename);

      if (existed) {
        redirectAttributes.addFlashAttribute("message", "Delete the image successfully: " + filename);
        ImageInfo imageInfo = imageRepository.findByfilename(filename);
        if (imageInfo != null) {
          imageRepository.delete(imageInfo);
        }
      } else {
        redirectAttributes.addFlashAttribute("message", "The image does not exist!");
      }
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message",
          "Could not delete the image: " + filename + ". Error: " + e.getMessage());
    }

    return "redirect:/images";
  }
}
