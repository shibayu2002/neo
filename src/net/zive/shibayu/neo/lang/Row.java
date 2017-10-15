package net.zive.shibayu.neo.lang;

import java.util.HashMap;

import net.zive.shibayu.neo.util.StringUtil;

/**
 * レコードセットの1行を表すクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class Row extends HashMap<String, Object> {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 指定されたキーがマップされている値を文字列で返します.
     * @param key 関連付けられた値が返されるキー
     * @return 指定されたキーにマップされている値。そのキーのマッピングがこのマップに含まれていない場合は ブランク
     */
    public final String getString(final String key) {
        Object value = get(key);
        if (value == null) {
            return StringUtil.BLANK;
        }
        return value.toString();
    }

    /**
     * 指定されたキーがマップされている値を整数で返します.
     * @param key 関連付けられた値が返されるキー
     * @return 指定されたキーにマップされている値。そのキーのマッピングがこのマップに含まれていない場合は0。
     * @throws NumberFormatException 指定されたキーにマップされている値が数値で無い場合
     */
    public final int getInt(final String key) throws NumberFormatException {
        Object value = get(key);
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value.toString());
    }
}
