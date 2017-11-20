package com.example.bootweb.map.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.example.bootweb.map.web.GPS;

/**
 * WGS－84原始坐标系：
 * <p>
 * 一般用国际GPS纪录仪记录下来的经纬度，通过GPS定位拿到的原始经纬度，Google和高德地图定位的的经纬度（国外）都是基于WGS－84坐标系的；但是在国内是不允许直接用WGS84坐标系标注的，必须经过加密后才能使用；
 * <p>
 * GCJ－02坐标系：
 * <p>
 * 又名“火星坐标系”，是我国国测局独创的坐标体系，由WGS－84加密而成，在国内，必须至少使用GCJ－02坐标系，或者使用在GCJ－02加密后再进行加密的坐标系，如百度坐标系。高德和Google在国内都是使用GCJ－02坐标系，可以说，GCJ－02是国内最广泛使用的坐标系；
 * <p>
 * bd-09百度坐标系：
 * <p>
 * bd-09，百度坐标系是在GCJ－02坐标系的基础上再次加密偏移后形成的坐标系，只适用于百度地图。(目前百度API提供了从其它坐标系转换为百度坐标系的API，但却没有从百度坐标系转为其他坐标系的API)。
 * 
 * @author huzexiong
 *
 */
@Service
public class GPSService {

  public static final BigDecimal X_PI = BigDecimal.valueOf(3.14159265358979324) //
      .multiply(BigDecimal.valueOf(3000.0)) //
      .divide(BigDecimal.valueOf(180.0), 10, RoundingMode.HALF_UP);

  /**
   * GCJ－02坐标系转换为BD-09百度坐标系
   * 
   * @param GCJ－02坐标系
   * @return BD-09百度坐标系
   */
  public GPS GCJ02toBD09(GPS gcj02) {
    GPS bd09 = new GPS();
    double x = gcj02.getLongitude().doubleValue();
    double y = gcj02.getLatitude().doubleValue();
    double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI.doubleValue());
    double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI.doubleValue());
    bd09.setLongitude(BigDecimal.valueOf(z * Math.cos(theta) + 0.0065));
    bd09.setLatitude(BigDecimal.valueOf(z * Math.sin(theta) + 0.006));
    return bd09;
  }

  /**
   * 将BD-09坐标转换成GCJ-02坐标
   * 
   * @param BD-09坐标
   * @return GCJ-02坐标
   */
  public GPS BD09toGCJ02(GPS bd09) {
    GPS gcj02 = new GPS();
    double x = bd09.getLongitude().doubleValue() - 0.0065;
    double y = bd09.getLatitude().doubleValue() - 0.006;
    double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI.doubleValue());
    double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI.doubleValue());
    gcj02.setLongitude(BigDecimal.valueOf(z * Math.cos(theta)));
    gcj02.setLatitude(BigDecimal.valueOf(z * Math.sin(theta)));
    return gcj02;
  }

}
