package com.example.bootweb.security.about;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.CharUtils;

/**
 * 版本号对象。
 * <p>
 * 
 * <h3>版本号的文本形式</h3>
 * 
 * 本类能够支持大多数常见版本号形式，示例：<br>
 * <blockquote> 2.0<br>
 * 2.1.13<br>
 * 2.2b4<br>
 * 2.4.5.1-build-32769<br>
 * 2.3.4rc6<br>
 * 2.3.4rc6-build-17803</blockquote>
 * 
 * 使用者只需要将包含上述内容的字符串，通过构造方法参数传入即可，对象将会自动进行解析。
 * <p>
 * 
 * 版本号文本中只允许出现数字、字母以及字符“.”和“-”，其余字符都将被自动过滤。版本号最多可能包含三个部分：<br>
 * <blockquote><i>主体 [ 里程碑 ] [ 构建序号 ]</i></blockquote>
 * 其中只有主体是必须存在的。以“2.3.4rc6-build-17803”为例： “2.3.4”被称为主体 （major）部分；
 * “rc6”被称为里程碑（milestone）部分； “-build-17803”则被称为构建序号（build）部分。 对于一个版本号至少应包含主体部分。
 * <p>
 * 
 * 使用者可以通过调用{@link #get(int)}取得版本号的各个有效部分，被称为版本位。所有版本位的计数即为版本号的长度。
 * 例如“1.2.17b3-build-3045”的长度为7，各版本位分别为：<br>
 * <blockquote><code>1, 2, 17, b, 3, build, 3045</code></blockquote>
 * <p>
 * 
 * 本类能够自动识别部分常见的的里程碑版本号，例如“rc”表示发布候选版本，“b”通常表示beta测试版，并引入了一套近似规则，用于版本比较运算，
 * 可以做到在多数情况下能够返回与常识一致的结果。
 * <p>
 * 
 * <h3>版本号的比较</h3>
 * 
 * 该类还提供版本号之间进行比较的功能{@link #compareTo(Version)}，需要特别说明的是当出现里程碑版本号比较时，遵循以下规则：<br>
 * <blockquote>
 * <code>SNAPSHOT &lt; build &lt; alpha(a) &lt; beta(b) &lt; gamma &lt; delta &lt; rc &lt; ga &lt; <i>release = 空字符串</i> &lt; <i>数字</i></code>
 * </blockquote>
 * <p>
 * 
 * 需要注意的是，compareTo() == 0与equals() == true两者的含义并不相同。compareTo()仅从版本号的含义角度出发，
 * 只关心版本号中版本位的比较；而equals()方法则可以简单地理解为版本号字符串比较。
 * <p>
 * 
 */
public class Version implements Serializable, Comparable<Version> {

  private static final long serialVersionUID = -6970061387853603773L;

  /** 版本号分隔字符“.”。 */
  public static final char SEPARATOR_DOT = '.';
  /** 版本号分隔字符“-”。 */
  public static final char SEPARATOR_HYPHEN = '-';
  /** 版本号默认分隔字符。 */
  public static final char SEPARATOR_DEFAULT = SEPARATOR_DOT;
  /** 构建序号保留字。 */
  public static final String BUILD = "build";
  /** 里程碑版本号保留字，表示快照版本。 */
  public static final String MILESTONE_SNAPSHOT = "SNAPSHOT";
  /** 里程碑版本号保留字，表示Alpha测试版本，长格式。 */
  public static final String MILESTONE_ALPHA_LONG = "alpha";
  /** 里程碑版本号保留字，表示Alpha测试版本，短格式。 */
  public static final String MILESTONE_ALPHA_SHORT = "a";
  /** 里程碑版本号保留字，表示Beta测试版本，长格式。 */
  public static final String MILESTONE_BETA_LONG = "beta";
  /** 里程碑版本号保留字，表示Beta测试版本，短格式。 */
  public static final String MILESTONE_BETA_SHORT = "b";
  /** 里程碑版本号保留字，表示Gamma测试版本，长格式。 */
  public static final String MILESTONE_GAMMA_LONG = "gamma";
  /** 里程碑版本号保留字，表示Gamma测试版本，短格式。 */
  public static final String MILESTONE_GAMMA_SHORT = "g";
  /** 里程碑版本号保留字，表示Delta测试版本，长格式。 */
  public static final String MILESTONE_DELTA_LONG = "delta";
  /** 里程碑版本号保留字，表示发布候选版本。 */
  public static final String MILESTONE_RELEASE_CANDIDATE_SHORT = "rc";
  /** 里程碑版本号保留字，表示一般可用版本，或首次发行稳定版本。 */
  public static final String MILESTONE_GENERAL_AVALIABILITY_SHORT = "ga";
  /** 里程碑版本号保留字，表示正式发布版本。 */
  public static final String MILESTONE_RELEASE_LONG = "release";

  private static final char[] SEPARATORS = new char[] {
      SEPARATOR_DOT, SEPARATOR_HYPHEN };

  /**
   * 创建对象。
   * <p>
   * 
   * 以下是几种等价的创建对象方法：<br>
   * <blockquote><code>
   * new Version("3.2.4b2");<br>
   * new Version("3", "2", "4", "b", "2");<br>
   * new Version("3.2", ".4b2");
   * </code></blockquote>
   * 
   * @param texts
   *          版本的文本形式数组。将忽略所有不是字母、数字以及分隔符的字符，传入null也将将被忽略。
   */
  public Version(String... texts) {
    words = decode(texts);
  }

  /** 包含版本号中所有位的列表。 */
  private List<Word> words;

  /**
   * 取得是否是空版本号，即长度为0的版本号。
   */
  public boolean isEmpty() {
    return words.isEmpty();
  }

  /**
   * 取得版本号的长度。
   */
  public int length() {
    return words.size();
  }

  /**
   * 取得指定索引版本位文本。
   * 
   * @param index
   *          表示版本位的索引，从左往右从0开始计数。
   * @throws IndexOutOfBoundsException
   *           当指定的索引不在其中范围内，则将抛出异常。
   */
  public String get(int index) throws IndexOutOfBoundsException {
    return words.get(index).getText();
  }

  /**
   * 取得包含所有版本位文本的数组。
   * 
   * @return 当空版本号时返回null。
   */
  public String[] gets() {
    String[] array = new String[words.size()];
    for (int index = 0; index < words.size(); index++) {
      Word word = words.get(index);
      array[index] = word.getText();
    }
    return array;
  }

  /**
   * 返回文本形式的版本号。
   * 
   * @return 当空版本号时返回null。
   */
  public String getText() {
    StringBuffer sb = new StringBuffer();
    for (Word word : words) {
      sb.append(word.toString());
    }
    return sb.toString();
  }

  /**
   * 取得版本号的主体部分。
   * 
   * @return 返回包含所有主题部分版本位的数组。当空版本号时返回null。
   */
  public String[] getMajor() {
    if (words.isEmpty()) {
      return null;
    }
    List<String> list = new ArrayList<String>();
    for (Word word : words) {
      if (word.isMilestone() || word.isBuild()) {
        break;
      }
      list.add(word.getText());
    }
    return list.toArray(new String[] {});
  }

  /**
   * 取得版本号的主体部分的文本形式。
   * 
   * @return 当空版本号时返回null。
   */
  public String getMajorText() {
    if (words.isEmpty()) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    for (Word word : words) {
      if (word.isMilestone() || word.isBuild()) {
        break;
      }
      sb.append(word.toString());
    }
    return sb.toString();
  }

  /**
   * 取得版本号的里程碑部分。
   * 
   * @return 返回包含所有里程碑部分版本位的数组。若没有里程碑部分将返回null。
   */
  public String[] getMilestone() {
    int index = 0;
    while (index < words.size()) {
      Word word = words.get(index);
      if (word.isMilestone()) {
        break;
      }
      index++;
    }
    if (index >= words.size()) {
      return null;
    }

    List<String> list = new ArrayList<String>();
    while (index < words.size()) {
      Word word = words.get(index);
      if (word.isBuild()) {
        break;
      }
      list.add(word.getText());
      index++;
    }

    if (list.isEmpty()) {
      return null;
    } else {
      return list.toArray(new String[] {});
    }
  }

  /**
   * 取得版本号里程碑部分的文本形式。
   * 
   * @return 若没有里程碑部分将返回null。
   */
  public String getMilestoneText() {
    int index = 0;
    while (index < words.size()) {
      Word word = words.get(index);
      if (word.isMilestone()) {
        break;
      }
      index++;
    }
    if (index >= words.size()) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    while (index < words.size()) {
      Word word = words.get(index);
      if (word.isBuild()) {
        break;
      }
      sb.append(word.toString());
      index++;
    }

    return sb.length() == 0 ? null : sb.toString();
  }

  /**
   * 取得版本号的构建序号部分。
   * 
   * @return 返回包含所有构建序号部分版本位的数组。
   */
  public String[] getBuild() {
    int index = 0;
    while (index < words.size()) {
      Word word = words.get(index);
      if (word.isBuild()) {
        break;
      }
      index++;
    }
    if (index >= words.size()) {
      return null;
    }

    List<String> list = new ArrayList<String>();
    while (index < words.size()) {
      Word word = words.get(index);
      list.add(word.getText());
      index++;
    }

    if (list.isEmpty()) {
      return null;
    } else {
      return list.toArray(new String[] {});
    }
  }

  public String getBuildText() {
    int index = 0;
    while (index < words.size()) {
      Word word = words.get(index);
      if (word.isBuild()) {
        break;
      }
      index++;
    }
    if (index >= words.size()) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    while (index < words.size()) {
      Word word = words.get(index);
      sb.append(word.toString());
      index++;
    }

    return sb.length() == 0 ? null : sb.toString();
  }

  @Override
  public int compareTo(Version another) {
    int index = 0;
    while (index < words.size() || index < another.words.size()) {
      Word word = null;
      if (index < words.size()) {
        word = words.get(index);
      }

      Word anotherWord = null;
      if (index < another.words.size()) {
        anotherWord = another.words.get(index);
      }

      int ret = Word.compareTo(word, anotherWord);
      if (ret != 0) {
        return ret;
      }

      index++;
    }
    return 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((words == null) ? 0 : words.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Version other = (Version) obj;
    if (words == null) {
      if (other.words != null)
        return false;
    } else if (!words.equals(other.words))
      return false;
    return true;
  }

  /**
   * 在当前版本号的后面再添加新的内容，形成新的版本号。
   * 
   * @param texts
   *          版本的文本形式数组。将忽略所有不是字母、数字以及分隔符的字符，传入null也将将被忽略。
   * @return 返回新的版本号。
   */
  public Version extend(String... texts) {
    Version another = clone();
    List<Word> list = decode(texts);
    if (list.isEmpty() == false) {
      // 判断是否需要添加分隔字符。
      if (another.isEmpty() == false) {
        Word first = list.get(0);
        Word last = another.words.get(another.length() - 1);
        if (first.getPrefix() == null) {
          if ((first.isNumeric() && last.isNumeric())
              || (first.isMilestone() && last.isMilestone())) {
            first.setPrefix(Character.valueOf(SEPARATOR_DEFAULT));
          }
        }
      }
      another.words.addAll(list);
    }
    return another;
  }

  /**
   * 仅保留当前版本号的主题部分的版本号。
   * 
   * @return 返回新的版本号。
   */
  public Version createMajor() {
    return truncateToMilestone();
  }

  /**
   * 对当前版本号的前几位进行截取。
   * 
   * @param index
   *          指定版本位索引，表示从这一位开始（包括这一位）之后的部分都被截取，由此推理传入0将返回一个空的版本号。对于build部分，
   *          无论传入何值，都将被截取。如果传入负数则将只截取build部分。<br>
   *          示例：对于版本号“3.1.2rc2.build-23890”：
   *          <table>
   *          <tr>
   *          <th>参数</th>
   *          <th>结果</th>
   *          </tr>
   *          <tr>
   *          <td>0</td>
   *          <td>""</td>
   *          </tr>
   *          <tr>
   *          <td>2</td>
   *          <td>"3.1"</td>
   *          </tr>
   *          <tr>
   *          <td>4</td>
   *          <td>"3.1.2rc"</td>
   *          </tr>
   *          <tr>
   *          <td>5</td>
   *          <td>"3.1.2rc2"</td>
   *          </tr>
   *          <tr>
   *          <td>-1</td>
   *          <td>"3.1.2rc2"</td>
   *          </tr>
   *          </table>
   * @return 返回新版本号对象。
   */
  public Version truncate(int index) {
    if (index < 0) {
      return clone();
    }
    Version another = new Version();
    for (int i = 0; i < words.size(); i++) {
      if (i >= index) {
        break;
      }
      Word word = words.get(i);
      Word anotherWord = word.clone();
      another.words.add(anotherWord);
    }
    return another;
  }

  /**
   * 截取版本号到里程碑部分之前，即仅保留主体部分。
   * 
   * @return 返回新版本号对象。
   */
  public Version truncateToMilestone() {
    Version another = new Version();
    for (Word word : words) {
      if (word.isMilestone() || word.isBuild()) {
        break;
      }
      Word anotherWord = word.clone();
      another.words.add(anotherWord);
    }
    return another;
  }

  /**
   * 截取版本号到构建序号部分之前，即仅保留主体部分。
   * 
   * @return 返回新版本号对象。
   */
  public Version truncateToBuild() {
    Version another = new Version();
    for (Word word : words) {
      if (word.isBuild()) {
        break;
      }
      Word anotherWord = word.clone();
      another.words.add(anotherWord);
    }
    return another;
  }

  @Override
  public String toString() {
    return getText();
  }

  @Override
  public Version clone() {
    Version another = new Version();
    for (Word word : words) {
      another.words.add(word.clone());
    }
    return another;
  }

  private List<Word> decode(String... texts) {
    List<Word> words = new ArrayList<Word>();

    List<String> list = new ArrayList<String>();
    for (String text : texts) {
      if (text != null) {
        list.addAll(split(text));
      }
    }

    Word lastWord = null;
    Character nextSeparator = null;
    for (String let : list) {
      assert let != null;
      if (isSeparator(let)) {
        if (nextSeparator != null) {
          Word word = Word.create(nextSeparator, "");
          words.add(word);
          lastWord = word;
        }
        nextSeparator = Character.valueOf(let.charAt(0));

      } else {
        Word word = Word.create(nextSeparator, let);
        nextSeparator = null;
        if (word.getPrefix() == null && word.isNumeric()) {
          if (lastWord != null && lastWord.isNumeric()) {
            word.setPrefix(Character.valueOf(SEPARATOR_DEFAULT));
          }
        }
        words.add(word);
        lastWord = word;

      }
    }

    return words;
  }

  @SuppressWarnings("static-method")
  private List<String> split(String text) {
    assert text != null;
    final int CTYPE_ALPHA = 0;
    final int CTYPE_NUMERIC = 1;
    final int CTYPE_SEPARATOR = 2;
    final int CTYPE_OTHER = -1;

    List<String> list = new ArrayList<String>();
    int lastCType = CTYPE_OTHER;
    StringBuffer sb = new StringBuffer();
    for (char c : text.toCharArray()) {
      // 判断当前字符类型。
      int currentCType = CTYPE_OTHER;
      if (CharUtils.isAsciiAlpha(c)) {
        currentCType = CTYPE_ALPHA;
      } else if (CharUtils.isAsciiNumeric(c)) {
        currentCType = CTYPE_NUMERIC;
      } else if (ArrayUtils.contains(SEPARATORS, c)) {
        currentCType = CTYPE_SEPARATOR;
      } else {
        // 忽略所有不是字母、数字或分隔符的字符
        continue;
      }

      if (currentCType != lastCType) {
        if (sb.length() > 0) {
          list.add(sb.toString());
        }
        sb = new StringBuffer();
        sb.append(c);
        lastCType = currentCType;
      } else {
        sb.append(c);
      }
    }

    if (sb.length() > 0) {
      list.add(sb.toString());
    }

    return list;
  }

  @SuppressWarnings("static-method")
  private boolean isSeparator(String str) {
    return str != null && str.length() == 1 && ArrayUtils.contains(SEPARATORS, str.charAt(0));
  }

  /**
   * 刻画版本号中的一位。
   * 
   * @author lxm
   * 
   */
  private static class Word implements Serializable, Comparable<Word> {

    private static final long serialVersionUID = -3554903575841758140L;
    private static final Map<String, Integer> CANDIDATES = new HashMap<String, Integer>();
    private static final int RELEASE_VALUE = -1;

    public static Word create(Character prefix, String text) {
      int value = RELEASE_VALUE;
      try {
        value = Integer.parseInt(text);
      } catch (NumberFormatException e) {
        Integer i = CANDIDATES.get(text.toLowerCase());
        if (i != null) {
          value = i.intValue();
        }
      }
      return new Word(prefix, text, value);
    }

    public static int compareTo(Word w1, Word w2) {
      int value1 = w1 == null ? RELEASE_VALUE : w1.value;
      int value2 = w2 == null ? RELEASE_VALUE : w2.value;
      return value1 - value2;
    }

    private Word(Character prefix, String text, int value) {
      this.prefix = prefix;
      this.text = text;
      this.value = value;
    }

    private Character prefix;
    private String text;
    private int value;

    public Character getPrefix() {
      return prefix;
    }

    public void setPrefix(Character prefix) {
      this.prefix = prefix;
    }

    public String getText() {
      return text;
    }

    public boolean isNumeric() {
      return isBuild() == false && isMilestone() == false;
    }

    public boolean isMilestone() {
      if (isBuild()) {
        return false;
      } else {
        return value < 0;
      }
    }

    public boolean isBuild() {
      return BUILD.equalsIgnoreCase(text);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
      result = prime * result + ((text == null) ? 0 : text.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Word other = (Word) obj;
      if (prefix == null) {
        if (other.prefix != null)
          return false;
      } else if (!prefix.equals(other.prefix))
        return false;
      if (text == null) {
        if (other.text != null)
          return false;
      } else if (!text.equals(other.text))
        return false;
      return true;
    }

    @Override
    public int compareTo(Word o) {
      return value - (o == null ? RELEASE_VALUE : o.value);
    }

    @Override
    public String toString() {
      StringBuffer sb = new StringBuffer();
      if (prefix != null) {
        sb.append(prefix);
      }
      sb.append(text);
      return sb.toString();
    }

    @Override
    public Word clone() {
      return new Word(prefix, text, value);
    }

    static {
      CANDIDATES.put(MILESTONE_SNAPSHOT.toLowerCase(), new Integer(-100));
      CANDIDATES.put(BUILD.toLowerCase(), new Integer(-90));
      CANDIDATES.put(MILESTONE_ALPHA_LONG.toLowerCase(), new Integer(-60));
      CANDIDATES.put(MILESTONE_ALPHA_SHORT.toLowerCase(), new Integer(-60));
      CANDIDATES.put(MILESTONE_BETA_LONG.toLowerCase(), new Integer(-50));
      CANDIDATES.put(MILESTONE_BETA_SHORT.toLowerCase(), new Integer(-50));
      CANDIDATES.put(MILESTONE_GAMMA_LONG.toLowerCase(), new Integer(-40));
      CANDIDATES.put(MILESTONE_GAMMA_SHORT.toLowerCase(), new Integer(-40));
      CANDIDATES.put(MILESTONE_DELTA_LONG.toLowerCase(), new Integer(-30));
      CANDIDATES.put(MILESTONE_RELEASE_CANDIDATE_SHORT.toLowerCase(), new Integer(-20));
      CANDIDATES.put(MILESTONE_GENERAL_AVALIABILITY_SHORT.toLowerCase(), new Integer(-10));
      CANDIDATES.put(MILESTONE_RELEASE_LONG.toLowerCase(), new Integer(RELEASE_VALUE));
    }
  }

}
