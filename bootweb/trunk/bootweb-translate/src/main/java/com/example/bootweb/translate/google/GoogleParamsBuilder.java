package com.example.bootweb.translate.google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.example.bootweb.translate.api.CN;
import com.example.bootweb.translate.api.EN;
import com.example.bootweb.translate.api.Lang;
import com.example.bootweb.translate.api.Params;
import com.example.bootweb.translate.api.ParamsBuilder;
import com.example.bootweb.translate.google.tk.Tk;
import com.example.bootweb.translate.google.tk.Tk0;

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

  private String text = null;
  private Class<? extends Lang> src;
  private Class<? extends Lang> target;
  private Tk tk = new Tk0();
  private final List<Params> params = new ArrayList<>();
  private final Map<Class<? extends Lang>, String> LANG_MAP = new HashMap<>();

  private GoogleParamsBuilder(Class<? extends Lang> src, //
      Class<? extends Lang> target) {
    this.src = src;
    this.target = target;
    LANG_MAP.put(CN.class, "zh-CN");
    LANG_MAP.put(EN.class, "en");
  }

  public static GoogleParamsBuilder newBuilder(Class<? extends Lang> src, //
      Class<? extends Lang> target) {
    Assert.notNull(src, "源语言不能为空。");
    Assert.notNull(target, "目标语言不能为空。");
    return new GoogleParamsBuilder(src, target);
  }

  @Override
  public GoogleParamsBuilder setText(String text) {
    Assert.notNull(text, "text");
    this.text = text;
    return this;
  }

  protected GoogleParamsBuilder put(String key, String value) {
    Assert.notNull(key, "key");
    Assert.notNull(value, "value");
    params.add(new Params(key, value));
    return this;
  }
  
  protected GoogleParamsBuilder tk(Tk tk) {
    Assert.notNull(tk, "tk");
    this.tk = tk;
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
        .put("tk", tk.calc(this.text)) // 上面js加密的结果
        .put("q", this.text); // 要翻译的文本 记得url编码一下 你好
    return params;
  }

}
