package com.example.bootweb.swagger;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("swagger")
public class SwaggerProperty {

  /** 标题 **/
  private String title = "";
  /** 描述 **/
  private String description = "";
  /** 版本 **/
  private String version = "";
  /** 许可证 **/
  private String license = "";
  /** 许可证URL **/
  private String licenseUrl = "";
  /** 服务条款URL **/
  private String termsOfServiceUrl = "";

  private Contact contact = new Contact();

  /** swagger会解析的包路径 **/
  private String basePackage = "";

  /** swagger会解析的url规则 **/
  private final List<String> basePath = new ArrayList<>();
  /** 在basePath基础上需要排除的url规则 **/
  private final List<String> excludePath = new ArrayList<>();

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public String getLicenseUrl() {
    return licenseUrl;
  }

  public void setLicenseUrl(String licenseUrl) {
    this.licenseUrl = licenseUrl;
  }

  public String getTermsOfServiceUrl() {
    return termsOfServiceUrl;
  }

  public void setTermsOfServiceUrl(String termsOfServiceUrl) {
    this.termsOfServiceUrl = termsOfServiceUrl;
  }

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  public List<String> getBasePath() {
    return basePath;
  }

  public void setBasePath(List<String> basePath) {
    this.basePath.clear();
    if (null != basePath) {
      this.basePath.addAll(basePath);
    }
  }

  public List<String> getExcludePath() {
    return excludePath;
  }

  public void setExcludePath(List<String> excludePath) {
    this.excludePath.clear();
    if (null != excludePath) {
      this.excludePath.addAll(excludePath);
    }
  }

  public static class Contact {

    /** 联系人 **/
    private String name = "";
    /** 联系人url **/
    private String url = "";
    /** 联系人email **/
    private String email = "";

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

  }

}
