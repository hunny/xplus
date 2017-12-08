package com.example.bootweb.security.about;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.jar.Manifest;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 应用程序“关于”信息。
 * <p>
 * 
 * 应用程序开发者通过调用{@link #get(Class)}方法，即可取得对象： <blockquote><code>
 * ApplicationAbout myAppAbout = ApplicationAbout.get(MyAppClass.class);
 * </code></blockquote> 上述方法中的传入参数为类，用于指示代表应用程序的类，调用者需要确保该类来自于所指向的应用程序。
 * 按照上述方法取得ApplicationAbout对象，就可以从中取得当前版本号、应用程序名称、站点以及版本声明等相关信息。
 * <p>
 * 
 * 开发者可以通过以下两种方式指定上述信息的具体取值：一、通过修改清单文件META-INF/MANIFEST.MF；二、通过提供
 * .properties资源文件方式。采用前者的好处在于使用方便，但是其不支持国际化。而采用后者则提供了国际化的能力。
 * 当两者同时采用的时候，则以后者优先级更高。
 * <p>
 * 
 * 通过修改清单文件META-INF/MANIFEST.MF方式设置应用程序“关于”信息，以下是一个典型的清单文件：<blockquote><code>
 * Implementation-Version: 1.0-SNAPSHOT<br>
 * application_title: MyCompany® MyApp™<br>
 * application_locale_title: 我的应用<br>
 * application_site: http://www.myapp.com<br>
 * application_copyright: 版权所有©，我的有限公司，2017-2020，所有权利保留。</code>
 * </blockquote> 如果采用了Apache Maven作为构建工具，可以通过修改maven-jar-plugin配置实现：<blockquote>
 * <code>
 * &lt;plugin><br>
 * &nbsp;&nbsp;&lt;groupId>org.apache.maven.plugins&lt;/groupId><br>
 * &nbsp;&nbsp;&lt;artifactId>maven-jar-plugin&lt;/artifactId><br>
 * &nbsp;&nbsp;&lt;configuration><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;archive><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;manifestEntries><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;application_title>MyCompany® MyApp™&lt;/application_title><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;application_locale_title>我的应用&lt;/application_locale_title><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;application_site>http://www.myapp.com&lt;/application_site><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;application_copyright>版权所有©，我的有限公司，2010-2016，所有权利保留&lt;/application_copyright><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/manifestEntries><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/archive><br>
 * &nbsp;&nbsp;&lt;/configuration><br>
 * &lt;/plugin>
 * </code> </blockquote>
 * <p>
 * 
 * 对象将根据通过参数传入的指示类，按照以下方式定位描述应用程序“关于”信息的.properties资源文件：查找指示类相同目录下同名资源文件，
 * 例如指示类为com .mycompany.MyAppClass，对应资源文件为com/mycompany/MyAppClass.properties。
 * <blockquote><code>
 * title=MyCompany® MyApp™<br>
 * localeTitle=我的应用<br>
 * site=http://www.myapp.com<br>
 * copyright=版权所有©，某有限公司，2017-2020，所有权利保留。
 * </code></blockquote>
 * <p>
 * 
 * 对于版本号的取得，只提供从清单文件中取得，不支持使用资源文件。
 * <p>
 */
public class ApplicationAbout {

  private static final Map<Class, ApplicationAbout> abouts = new HashMap<Class, ApplicationAbout>();

  /**
   * 取得应用程序关于对象。
   * 
   * @param indicatoryClass
   *          作为应用程序的指向性类。禁止传入null。
   * @throws IllegalArgumentException
   *           当参数indicatoryClass为null时抛出。
   * @since 1.16
   */
  public static ApplicationAbout get(Class indicatoryClass) throws IllegalArgumentException {
    Assert.notNull(indicatoryClass, "indicatoryClass");
    ApplicationAbout about = abouts.get(indicatoryClass);
    if (about != null) {
      return about;
    }
    if (ApplicationAbout.class.isAssignableFrom(indicatoryClass)) {
      try {
        about = (ApplicationAbout) indicatoryClass.newInstance();
      } catch (Exception e) {
        about = null;
      }
    }
    if (about == null) {
      about = new ApplicationAbout(indicatoryClass);
    }
    abouts.put(indicatoryClass, about);
    return about;
  }

  public static final String MF_TITLE = "application_title";
  public static final String MF_COMMON_TITLE = "application_common_title";
  public static final String MF_LOCALE_TITLE = "application_locale_title";
  public static final String MF_SITE = "application_site";
  public static final String MF_COPYRIGHT = "application_copyright";
  public static final String MF_BUILD = "Implementation-Build";

  public static final String RESOURCE_TITLE = "title";
  public static final String RESOURCE_COMMON_TITLE = "commonTitle";
  public static final String RESOURCE_LOCALE_TITLE = "localeTitle";
  public static final String RESOURCE_SITE = "site";
  public static final String RESOURCE_COPYRIGHT = "copyright";

  private static final String DEFAULT_SITE = "http://www.foobar.com";

  protected ApplicationAbout() {
    this(null);
  }

  /**
   * @param indicatoryClass
   *          作为应用程序的指向性类。传入null等价于传入当前类。
   */
  protected ApplicationAbout(Class<?> indicatoryClass) {
    Class icls = indicatoryClass == null ? getClass() : indicatoryClass;

    loadFromManifest(icls);
    loadFromResources(icls);
  }

  private String title;
  private String commonTitle;
  private String localeTitle;
  private Version version;
  private String build;
  private String site = DEFAULT_SITE;
  private String copyright;

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * 应用程序通用标题，可能含“®”与“™”字符。<br>
   * 对应资源名{@link #RESOURCE_TITLE}。
   */
  public String getTitle() {
    return title;
  }

  /**
   * 应用程序通用名称。<br>
   * 对应资源名{@link #RESOURCE_COMMON_TITLE}。
   */
  public String getCommonTitle() {
    if (commonTitle == null) {
      commonTitle = toCommonString(title);
    }
    return commonTitle;
  }

  /**
   * 应用程序本地化标题。<br>
   * 对应资源名{@link #RESOURCE_LOCALE_TITLE}。
   */
  public String getLocaleTitle() {
    if (localeTitle == null) {
      return title;
    } else {
      return localeTitle;
    }
  }

  /**
   * 版本号。
   */
  public Version getVersion() {
    return version;
  }

  /**
   * 构建序号。
   */
  public String getBuild() {
    return build;
  }

  /**
   * 产品官方网站URL地址。
   */
  public String getSite() {
    return site;
  }

  /**
   * 版权声明。<br>
   * 对应资源名{@link #RESOURCE_COPYRIGHT}。
   */
  public String getCopyright() {
    return copyright;
  }

  private void loadFromManifest(Class icls) {
    assert icls != null;
    try {
      Manifest mf = ManifestUtil.getManifest(icls);
      if (mf == null) {
        return;
      }
      version = ManifestUtil.getVersion(mf);

      String title = mf.getMainAttributes().getValue(MF_TITLE);
      if (title != null && "".equals(title.trim()) == false) {
        this.title = title;
      }
      String commonTitle = mf.getMainAttributes().getValue(MF_COMMON_TITLE);
      if (commonTitle != null && "".equals(commonTitle.trim()) == false) {
        this.commonTitle = commonTitle;
      }
      String localeTitle = mf.getMainAttributes().getValue(MF_LOCALE_TITLE);
      if (localeTitle != null && "".equals(localeTitle.trim()) == false) {
        this.localeTitle = localeTitle;
      }
      String site = mf.getMainAttributes().getValue(MF_SITE);
      if (site != null && "".equals(site.trim()) == false) {
        this.site = site;
      }
      String copyright = mf.getMainAttributes().getValue(MF_COPYRIGHT);
      if (copyright != null && "".equals(copyright.trim()) == false) {
        this.copyright = copyright;
      }
      String build = mf.getMainAttributes().getValue(MF_BUILD);
      if (build != null && "".equals(build.trim()) == false) {
        this.build = build;
      }
    } catch (Exception e) {
      logger.warn(MessageFormat.format("Fail to load from Manifest: {0}", icls), e);
    }
  }

  private void loadFromResources(Class icls) {
    assert icls != null;
    try {
      ResourceBundle res = ResourceBundleUtil.getBundle(icls, icls.getSimpleName());
      title = res.getString(RESOURCE_TITLE);
      if (res.containsKey(RESOURCE_COMMON_TITLE)) {
        commonTitle = toCommonString(res.getString(RESOURCE_COMMON_TITLE));
      }
      if (res.containsKey(RESOURCE_LOCALE_TITLE)) {
        localeTitle = res.getString(RESOURCE_LOCALE_TITLE);
      }
      if (res.containsKey(RESOURCE_SITE)) {
        site = res.getString(RESOURCE_SITE);
      }
      if (res.containsKey(RESOURCE_COPYRIGHT)) {
        copyright = res.getString(RESOURCE_COPYRIGHT);
      }
    } catch (MissingResourceException e) {
      return;
    } catch (Exception e) {
      logger.warn(MessageFormat.format("Fail to load from resource: {0}", icls), e);
    }
  }

  /** 去除输入字符串中的非ascii字符。 */
  private static String toCommonString(String instr) {
    if (instr == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (char ch : instr.toCharArray()) {
      if (CharUtils.isAscii(ch)) {
        sb.append(ch);
      }
    }
    return sb.toString();
  }

}
