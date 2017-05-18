package com.xplus.commons.util.network.address;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.StringTokenizer;

/**
 * @author huzexiong
 *
 */
public class NetworkAddressUtil {

  private final static int MACADDR_LENGTH = 17;
  private final static String WIN_OSNAME = "Windows";
  private final static String LINUX_OSNAME = "Linux";
  private final static String OSX_OSNAME = "Mac OS X";
  private final static String MACADDR_REG_EXP = "^[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}-[0-9A-F]{2}$";
  private final static String WIN_MACADDR_EXEC = "ipconfig /all";
  private final static String LINUX_MACADDR_EXEC = "ifconfig";
  private final static String OSX_MACADDR_EXEC = "ifconfig";

  public final static String getMacAddress() throws IOException {
    String os = System.getProperty("os.name");
    try {
      if (os.startsWith(WIN_OSNAME)) {
        return windowsParseMacAddress(windowsIpConfigCommand());
      } else if (os.startsWith(LINUX_OSNAME)) {
        return linuxParseMacAddress(linuxRunIfConfigCommand());
      } else if (os.startsWith(OSX_OSNAME)) {
        return osxParseMacAddress(osxRunIfConfigCommand());
      } else {
        throw new IOException("OS not supported : " + os);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new IOException(e.getMessage());
    }
  }

  /**
   * OSX stuff
   */
  private final static String osxParseMacAddress(String ipConfigOutput) throws ParseException {
    String localHost = null;
    try {
      localHost = InetAddress.getLocalHost().getHostAddress();
    } catch (java.net.UnknownHostException ex) {
      ex.printStackTrace();
      throw new ParseException(ex.getMessage(), 0);
    }
    StringTokenizer tokenizer = new StringTokenizer(ipConfigOutput, "\n");
    String lastMacAddress = null;
    while (tokenizer.hasMoreTokens()) {
      String line = tokenizer.nextToken().trim();
      boolean containsLocalHost = line.indexOf(localHost) >= 0;
      // see if line contains IP address
      if (containsLocalHost && lastMacAddress != null) {
        return lastMacAddress;
      }
      // see if line contains MAC address
      int macAddressPosition = line.indexOf("ether");
      if (macAddressPosition != 0) {
        continue;
      }
      String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
      if (osxIsMacAddress(macAddressCandidate)) {
        lastMacAddress = macAddressCandidate;
        continue;
      }
    }
    ParseException ex = new ParseException(
        "cannot read MAC address for " + localHost + " from [" + ipConfigOutput + "]", 0);
    ex.printStackTrace();
    throw ex;

  }

  private final static boolean osxIsMacAddress(String macAddressCandidate) {
    if (macAddressCandidate.length() != MACADDR_LENGTH) {
      return false;
    }
    if (!macAddressCandidate.matches(MACADDR_REG_EXP)) {
      return false;
    }
    return true;
  }

  private final static String osxRunIfConfigCommand() throws IOException {
    Process p = Runtime.getRuntime().exec(OSX_MACADDR_EXEC);
    InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
    StringBuffer buffer = new StringBuffer();
    for (;;) {
      int c = stdoutStream.read();
      if (c == -1) {
        break;
      }
      buffer.append((char) c);
    }
    String outputText = buffer.toString();
    stdoutStream.close();
    return outputText;
  }

  /**
   * Linux stuff
   */
  private final static String linuxParseMacAddress(String ipConfigOutput) throws ParseException {
    String localHost = null;
    try {
      localHost = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      throw new ParseException(e.getMessage(), 0);
    }
    StringTokenizer tokenizer = new StringTokenizer(ipConfigOutput, "\n");
    String lastMacAddress = null;
    while (tokenizer.hasMoreTokens()) {
      String line = tokenizer.nextToken().trim();
      boolean containsLocalHost = line.indexOf(localHost) >= 0;
      // see if line contains IP address
      if (containsLocalHost && lastMacAddress != null) {
        return lastMacAddress;
      }
      // see if line contains MAC address
      int macAddressPosition = line.indexOf("HWaddr");
      if (macAddressPosition <= 0) {
        continue;
      }
      String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
      if (linuxIsMacAddress(macAddressCandidate)) {
        lastMacAddress = macAddressCandidate;
        continue;
      }
    }
    ParseException ex = new ParseException(
        "cannot read MAC address for " + localHost + " from [" + ipConfigOutput + "]", 0);
    ex.printStackTrace();
    throw ex;
  }

  private final static boolean linuxIsMacAddress(String macAddressCandidate) {
    if (macAddressCandidate.length() != MACADDR_LENGTH) {
      return false;
    }
    if (!macAddressCandidate.matches(MACADDR_REG_EXP)) {
      return false;
    }
    return true;
  }

  private final static String linuxRunIfConfigCommand() throws IOException {
    Process p = Runtime.getRuntime().exec(LINUX_MACADDR_EXEC);
    InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
    StringBuffer buffer = new StringBuffer();
    for (;;) {
      int c = stdoutStream.read();
      if (c == -1) {
        break;
      }
      buffer.append((char) c);
    }
    String outputText = buffer.toString();
    stdoutStream.close();
    return outputText;
  }

  /**
   * Windows stuff
   */
  private final static String windowsParseMacAddress(String ipConfigOutput) throws ParseException {
    String localHost = null;
    try {
      localHost = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      throw new ParseException(e.getMessage(), 0);
    }
    StringTokenizer tokenizer = new StringTokenizer(ipConfigOutput, "\n");
    String lastMacAddress = null;
    while (tokenizer.hasMoreTokens()) {
      String line = tokenizer.nextToken().trim();
      // see if line contains IP address
      if (line.endsWith(localHost) && lastMacAddress != null) {
        return lastMacAddress;
      }
      // see if line contains MAC address
      int macAddressPosition = line.indexOf(":");
      if (macAddressPosition <= 0) {
        continue;
      }
      String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
      if (windowsIsMacAddress(macAddressCandidate)) {
        lastMacAddress = macAddressCandidate;
        continue;
      }
    }
    ParseException ex = new ParseException("cannot read MAC address from [" + ipConfigOutput + "]",
        0);
    ex.printStackTrace();
    throw ex;
  }

  private final static boolean windowsIsMacAddress(String macAddressCandidate) {
    if (macAddressCandidate.length() != MACADDR_LENGTH) {
      return false;
    }
    if (!macAddressCandidate.matches(MACADDR_REG_EXP)) {
      return false;
    }
    return true;
  }

  private final static String windowsIpConfigCommand() throws IOException {
    Process p = Runtime.getRuntime().exec(WIN_MACADDR_EXEC);
    InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
    StringBuffer buffer = new StringBuffer();
    for (;;) {
      int c = stdoutStream.read();
      if (c == -1) {
        break;
      }
      buffer.append((char) c);
    }
    String outputText = buffer.toString();
    stdoutStream.close();
    return outputText;
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    try {

      System.out.println(" MAC ADDRESS");
      System.out.println(" OS       :" + System.getProperty("os.name"));
      System.out.println(" IP/Localhost : " + InetAddress.getLocalHost().getHostAddress());
      System.out.println(" MAC Address : " + getMacAddress().replaceAll("-", ""));
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }
}
