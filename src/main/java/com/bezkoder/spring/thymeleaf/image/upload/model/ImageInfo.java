package com.bezkoder.spring.thymeleaf.image.upload.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="metadata")
public class ImageInfo {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "filename")
  private String filename;


  @Column(name = "dateadd")
  private Date dateadd;

  @Column(name = "url")
  private String url;

  public String getFileName() {
    return this.filename;
  }

  public void setFileName(String name) {
    this.filename = name;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDateadd() {
    return dateadd.toString();
  }

  public void setDateadd(Date dateadd) {
    this.dateadd = dateadd;
  }

}
