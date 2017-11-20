package com.example.bootweb.map.web;

import java.io.Serializable;
import java.math.BigDecimal;

public class GPS implements Serializable {

  private static final long serialVersionUID = 8544076814330807582L;

  private BigDecimal longitude; // 经度
  private BigDecimal latitude; // 纬度

  /** 经度 */
  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  /** 纬度 */
  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

}
