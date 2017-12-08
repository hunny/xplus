package com.example.bootweb.security.about;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * 提供与{@link java.util.jar.Manifest}有关工具。
 * 
 */
public class ManifestUtil {

  /**
   * 取得指定类所在jar对应的Manifest对象。<br>
   * 如果参数clazz位于某个jar包中，则将尝试查找该jar包内“/META-INF/MANIFEST.MF”，将其返回。
   * 但对于未能打成jar包的形式则将采取如下策略进行搜索
   * ：首先搜索参数clazz所在路径，是否存在“META-INF/MANIFEST.MF”，如果不存在则尝试搜索其上级路径。
   * 
   * @param clazz
   *          作为标志的类，传入null将导致返回null。
   * @return 若找不到将返回null。
   * @throws IOException
   */
  public static Manifest getManifest(Class clazz) throws IOException {
    if (clazz == null) {
      return null;
    }

    Manifest mf = getManifestFromJar(clazz);
    if (mf != null) {
      return mf;
    }
    mf = getManifestFromFile(clazz);

    return mf;
  }

  /**
   * 尝试从jar中取得Manifest。
   * 
   * @return 若取不到，例如指定类不位于jar中，将返回null。
   * @throws IOException
   */
  private static Manifest getManifestFromJar(Class clazz) throws IOException {
    assert clazz != null;
    String resource = "/" + clazz.getName().replace('.', '/') + ".class";
    URL url = clazz.getResource(resource);
    URLConnection conn = url.openConnection();
    if (conn instanceof JarURLConnection) {
      JarURLConnection jconn = (JarURLConnection) conn;
      return jconn.getManifest();
    } else {
      return null;
    }
  }

  /**
   * 尝试搜索文件，以取得Manifest。
   * 
   * @return 若找不到将返回null。
   * @throws IOException
   */
  private static Manifest getManifestFromFile(Class clazz) throws IOException {
    assert clazz != null;
    String resource = "/" + clazz.getName().replace('.', '/') + ".class";
    URL url = clazz.getResource(resource);
    // 将url转换为文件路径。
    File baseFile = null;
    try {
      baseFile = new File(url.toURI());
    } catch (Exception e) {
      // 转换失败，意味着无法使用文件方式
      return null;
    }

    // 逐级向上查找
    while (baseFile != null) {
      File file = new File(baseFile.getParent(), "META-INF/MANIFEST.MF");
      if (file.exists()) {
        return new Manifest(new FileInputStream(file));
      }
      baseFile = baseFile.getParentFile();
    }
    return null;
  }

  /**
   * 从指定Manifest中取得版本号。<br>
   * 具体读取时，将首先尝试读取Attributes.Name.IMPLEMENTATION_VERSION，若读取不到将尝试读取Attributes.
   * Name.SPECIFICATION_VERSION。
   * 
   * @param manifest
   *          传入null将导致返回null。
   * @return 返回版本号对象，若传入的Manifest对象中没有包含版本信息时将返回null。
   */
  public static Version getVersion(Manifest manifest) {
    if (manifest == null) {
      return null;
    }
    Attributes attrs = manifest.getMainAttributes();
    String versionText = attrs.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    if (versionText == null || "".equals(versionText.trim())) {
      versionText = attrs.getValue(Attributes.Name.SPECIFICATION_VERSION);
    }
    if (versionText == null || "".equals(versionText.trim())) {
      return null;
    } else {
      return new Version(versionText);
    }
  }

}
