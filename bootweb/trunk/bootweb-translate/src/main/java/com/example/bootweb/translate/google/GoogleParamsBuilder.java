package com.example.bootweb.translate.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.util.Assert;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Params;
import com.example.bootweb.translate.api.ParamsBuilder;

/**
 * <p>
 * https://stackoverflow.com/questions/32053442/google-translate-tts-api-blocked
 * <p>
 * https://stackoverflow.com/questions/26714426/what-is-the-meaning-of-google-translate-query-params
 * <p>
 * https://www.zhanghuanglong.com/detail/google-translate-tk-generation-and-parameter-details
 * <p>
 * 
 * @author hunnyhu
 *
 */
public class GoogleParamsBuilder implements ParamsBuilder {

  private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

  private String text = null;
  private Class<? extends Lang> src;
  private Class<? extends Lang> target;
  private final List<Params> params = new ArrayList<>();
  private final Map<Class<? extends Lang>, String> LANG_MAP = new HashMap<>();

  private GoogleParamsBuilder(Class<? extends Lang> src, //
      Class<? extends Lang> target) {
    this.src = src;
    this.target = target;
    LANG_MAP.put(CN.class, "zh-CN");
    LANG_MAP.put(EN.class, "en");
  }

  public static ParamsBuilder newBuilder(Class<? extends Lang> src, //
      Class<? extends Lang> target) {
    Assert.notNull(src, "源语言不能为空。");
    Assert.notNull(target, "目标语言不能为空。");
    return new GoogleParamsBuilder(src, target);
  }

  @Override
  public ParamsBuilder setText(String text) {
    this.text = text;
    return this;
  }

  protected GoogleParamsBuilder put(String key, String value) {
    params.add(new Params(key, value));
    return this;
  }

  @Override
  public List<Params> build() {
    Assert.notNull(this.text, "翻译的文本不能为null。");
    this.put("client", "t") // translation of source text
        .put("sl", LANG_MAP.get(src)) // 源语言 (auto代表自动检测) en
        .put("tl", LANG_MAP.get(target)) // 目标语言 zh-CN
        .put("hl", "zh-CN") //
        .put("dt", "at") // alternate translations
        .put("dt", "bd") // dictionary, in case source text is one word (you get translations with
                         // articles, reverse translations, etc.)
        .put("dt", "ex") // examples
        .put("dt", "ld") //
        .put("dt", "md") // definitions of source text, if it's one word
        .put("dt", "qca") //
        .put("dt", "rw") // See also list.
        .put("dt", "rm") // transcription / transliteration of source and translated texts
        .put("dt", "ss") // synonyms of source text, if it's one word
        .put("dt", "t") //translation of source text
        .put("ie", "UTF-8") // 输入的编码 UTF-8
        .put("oe", "UTF-8") // 输出的编码 UTF-8
        .put("source", "btn") //
        .put("srcrom", "1") //seems to be present when the source text has no spelling suggestions
        .put("ssel", "0") //
        .put("tsel", "0") //
        .put("kc", "11") //
        .put("tk", tk(this.text)) // 上面js加密的结果
        .put("q", this.text); // 要翻译的文本 记得url编码一下 你好
    return params;
  }

  @SuppressWarnings("static-method")
  private String tk(String val) {
    String script = "function tk(a) {" //
        + "var TKK = ((function() {" //
        + "var a = 561666268;" //
        + "var b = 1526272306;" //
        + "return 406398 + '.' + (a + b); " //
        + "})());\n" //
        + "function b(a, b) { " //
        + "for (var d = 0; d < b.length - 2; d += 3) { " //
        + "var c = b.charAt(d + 2), " //
        + "c = 'a' <= c ? c.charCodeAt(0) - 87 : Number(c), " //
        + "c = '+' == b.charAt(d + 1) ? a >>> c : a << c; " //
        + "a = '+' == b.charAt(d) ? a + c & 4294967295 : a ^ c " //
        + "} " //
        + "return a }\n" //
        + "for (var e = TKK.split('.'), h = Number(e[0]) || 0, g = [], d = 0, f = 0; f < a.length; f++) {" //
        + "var c = a.charCodeAt(f);" //
        + "128 > c ? g[d++] = c : (2048 > c ? g[d++] = c >> 6 | 192 : (55296 == (c & 64512) && f + 1 < a.length && 56320 == (a.charCodeAt(f + 1) & 64512) ? (c = 65536 + ((c & 1023) << 10) + (a.charCodeAt(++f) & 1023), g[d++] = c >> 18 | 240, g[d++] = c >> 12 & 63 | 128) : g[d++] = c >> 12 | 224, g[d++] = c >> 6 & 63 | 128), g[d++] = c & 63 | 128)"
        + "}" //
        + "a = h;" //
        + "for (d = 0; d < g.length; d++) a += g[d], a = b(a, '+-a^+6');" //
        + "a = b(a, '+-3^+b+-f');" //
        + "a ^= Number(e[1]) || 0;" //
        + "0 > a && (a = (a & 2147483647) + 2147483648);" //
        + "a %= 1E6;" //
        + "return a.toString() + '.' + (a ^ h)\n" //
        + "}"; //
    try {
      engine.eval(script);
      Invocable inv = (Invocable) engine;
      return (String) inv.invokeFunction("tk", val);
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

}
