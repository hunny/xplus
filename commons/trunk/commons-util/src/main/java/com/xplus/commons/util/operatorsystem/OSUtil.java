package com.xplus.commons.util.operatorsystem;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * 操作系统工具
 * 
 * @author huzexiong
 *
 */
public class OSUtil {

  /**
   * 操作系统种类
   * 
   * @author huzexiong
   *
   */
  public static enum OSType {
    LINUX, WINDOWS, MAC_OS, FREE_BSD, OTHER_BSD, SOLARIS, AIX, OTHER_UNIX, OTHER_OS
  };
  
  public static boolean isWindowsLike() {
    return getOSType() == OSType.WINDOWS;
  }
  
  public static boolean isUnixLike() {
    return isUnixLike(getOSType());
  }
  
  public static boolean isHardUnix() {
    return isHardUnix(getOSType());
  }

  public static boolean isUnixLike(OSType os) {
    return os == OSType.LINUX || os == OSType.MAC_OS || os == OSType.FREE_BSD
        || os == OSType.OTHER_BSD || os == OSType.SOLARIS || os == OSType.AIX
        || os == OSType.OTHER_UNIX;
  }

  public static boolean isHardUnix(OSType os) {
    return os == OSType.FREE_BSD || os == OSType.OTHER_BSD || os == OSType.SOLARIS
        || os == OSType.AIX || os == OSType.OTHER_UNIX;
  }

  public static OSType getOSType() {
    String name = System.getProperty("os.name").toLowerCase(Locale.ROOT);

    if (name.contains("linux")) {
      return OSType.LINUX;
    } else if (name.contains("windows")) {
      return OSType.WINDOWS;
    } else if (name.contains("sunos") || name.contains("solaris")) {
      return OSType.SOLARIS;
    } else if (name.contains("darwin") || name.contains("mac os") || name.contains("macos")) {
      return OSType.MAC_OS;
    } else if (name.contains("free") && name.contains("bsd")) {
      return OSType.FREE_BSD;
    } else if ((name.contains("open") || name.contains("net")) && name.contains("bsd")) {
      return OSType.OTHER_BSD;
    } else if (name.contains("aix")) {
      return OSType.AIX;
    } else if (name.contains("unix")) {
      return OSType.OTHER_UNIX;
    } else {
      return OSType.OTHER_OS;
    }
  }

  // // Returns the directory that the wallet program was started from
  // public static String getProgramDirectory() throws IOException {
  // not
  // // work
  // // if program is repackaged as different JAR!
  // final String JAR_NAME = "ZCashSwingWalletUI.jar";
  // String cp = System.getProperty("java.class.path");
  // if ((cp != null) && (cp.indexOf(File.pathSeparator) == -1) &&
  // (cp.endsWith(JAR_NAME))) {
  // File pd = new File(cp.substring(0, cp.length() - JAR_NAME.length()));
  //
  // if (pd.exists() && pd.isDirectory()) {
  // return pd.getCanonicalPath();
  // }
  // }
  //
  // // Current dir of the running JVM (expected)
  // String userDir = System.getProperty("user.dir");
  // if (userDir != null) {
  // File ud = new File(userDir);
  //
  // if (ud.exists() && ud.isDirectory()) {
  // return ud.getCanonicalPath();
  // }
  // }
  //
  // return new File(".").getCanonicalPath();
  // }

  public static File getUserHomeDirectory() throws IOException {
    return new File(System.getProperty("user.home"));
  }

  // public static String getBlockchainDirectory() throws IOException {
  // OSType os = getOSType();
  //
  // if (os == OSType.MAC_OS) {
  // return new File(System.getProperty("user.home") + "/Library/Application
  // Support/Zcash")
  // .getCanonicalPath();
  // } else if (os == OSType.WINDOWS) {
  // return new File(System.getenv("APPDATA") + "\\Zcash").getCanonicalPath();
  // } else {
  // return new File(System.getProperty("user.home") +
  // "/.zcash").getCanonicalPath();
  // }
  // }

  // // Directory with program settings to store
  // public static String getSettingsDirectory() throws IOException {
  // File userHome = new File(System.getProperty("user.home"));
  // File dir;
  // OSType os = getOSType();
  //
  // if (os == OSType.MAC_OS) {
  // dir = new File(userHome, "Library/Application Support/ZCashSwingWalletUI");
  // } else if (os == OSType.WINDOWS) {
  // dir = new File(System.getenv("LOCALAPPDATA") + "\\ZCashSwingWalletUI");
  // } else {
  // dir = new File(userHome.getCanonicalPath() + File.separator +
  // ".ZCashSwingWalletUI");
  // }
  //
  // if (!dir.exists()) {
  // if (!dir.mkdirs()) {
  // System.out
  // .println("WARNING: Could not create settings directory: " +
  // dir.getCanonicalPath());
  // }
  // }
  //
  // return dir.getCanonicalPath();
  // }

  public static String getSystemInfo() throws IOException, InterruptedException {
    OSType os = getOSType();

    if (os == OSType.MAC_OS) {
      CommandExecutor uname = new CommandExecutor(new String[] {
          "uname", "-sr" });
      return uname.execute() + "; " + System.getProperty("os.name") + " "
          + System.getProperty("os.version");
    } else if (os == OSType.WINDOWS) {
      return System.getProperty("os.name");
    } else {
      CommandExecutor uname = new CommandExecutor(new String[] {
          "uname", "-srv" });
      return uname.execute();
    }
  }

  // // Can be used to find zcashd/zcash-cli if it is not found in the same
  // place
  // // as the wallet JAR
  // // Null if not found
  // public static File findZCashCommand(String command) throws IOException {
  // File f;
  //
  // // Try with system property zcash.location.dir - may be specified by caller
  // String ZCashLocationDir = System.getProperty("zcash.location.dir");
  // if ((ZCashLocationDir != null) && (ZCashLocationDir.trim().length() > 0)) {
  // f = new File(ZCashLocationDir + File.separator + command);
  // if (f.exists() && f.isFile()) {
  // return f.getCanonicalFile();
  // }
  // }
  //
  // OSType os = getOSType();
  //
  // if (isUnixLike(os)) {
  // // The following search directories apply to UNIX-like systems only
  // final String dirs[] = new String[] {
  // "/usr/bin/", // Typical Ubuntu
  // "/bin/", "/usr/local/bin/", "/usr/local/zcash/bin/", "/usr/lib/zcash/bin/",
  // "/opt/local/bin/", "/opt/local/zcash/bin/", "/opt/zcash/bin/" };
  //
  // for (String d : dirs) {
  // f = new File(d + command);
  // if (f.exists()) {
  // return f;
  // }
  // }
  //
  // } else if (os == OSType.WINDOWS) {
  // // A probable Windows directory is a ZCash dir in Program Files
  // String programFiles = System.getenv("PROGRAMFILES");
  // if ((programFiles != null) && (!programFiles.isEmpty())) {
  // File pf = new File(programFiles);
  // if (pf.exists() && pf.isDirectory()) {
  // File ZDir = new File(pf, "Zcash");
  // if (ZDir.exists() && ZDir.isDirectory()) {
  // File cf = new File(ZDir, command);
  // if (cf.exists() && cf.isFile()) {
  // return cf;
  // }
  // }
  // }
  // }
  // }
  //
  // // Try in the current directory
  // f = new File("." + File.separator + command);
  // if (f.exists() && f.isFile()) {
  // return f.getCanonicalFile();
  // }
  //
  //
  // return null;
  // }
}
