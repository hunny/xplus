package com.example.bootweb.security.about;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 提供与ResourceBundle有关的工具。
 */
public class ResourceBundleUtil {

  /**
   * 取得指定语言环境下的ResourceBundle对象。
   * 
   * @param baseClass
   *          类，提供对baseName的定位，以及ClassLoader。允许传入null。意味着从classpath根路径下搜索资源，
   *          并且使用默认的ClassLoader。
   * @param baseName
   *          资源包的基本名称，如果以字符
   *          "\"开始，则被认为是相对classpath根路径，否则被认为是baseClass指定的相对路径。禁止传入null。
   * @throws MissingResourceException
   *           如果未找到指定名称的资源包
   */
  public static ResourceBundle getBundle(Class<?> baseClass, String baseName)
      throws MissingResourceException {
    return getBundle(baseClass, baseName, null);
  }

  /**
   * 取得指定语言环境下的ResourceBundle对象。
   * 
   * @param baseClass
   *          类，提供对baseName的定位，以及ClassLoader。允许传入null。意味着从classpath根路径下搜索资源，
   *          并且使用默认的ClassLoader。
   * @param baseName
   *          资源包的基本名称，如果以字符
   *          "\"开始，则被认为是相对classpath根路径，否则被认为是baseClass指定的相对路径。禁止传入null。
   * @param locale
   *          语言环境对象，传入null表示取得默认语言环境。
   * @throws MissingResourceException
   *           如果未找到指定名称的资源包
   */
  public static ResourceBundle getBundle(Class<?> baseClass, String baseName, Locale locale)
      throws MissingResourceException {
    if (baseName == null) {
      throw new MissingResourceException("baseName", ResourceBundleUtil.class.getName(), baseName);
    }
    String fullName = baseName;
    if (fullName.charAt(0) != '/') {
      if (baseClass == null) {
        fullName = '/' + fullName;
      } else {
        fullName = baseClass.getPackage().getName().replace('.', '/') + '/' + fullName;
      }
    }
    ClassLoader classLoader = ResourceBundleUtil.class.getClassLoader();
    if (baseClass != null) {
      classLoader = baseClass.getClassLoader();
    }
    Locale aLocale = locale == null ? Locale.getDefault() : locale;
    return ResourceBundle.getBundle(fullName, aLocale, classLoader);
  }
}
