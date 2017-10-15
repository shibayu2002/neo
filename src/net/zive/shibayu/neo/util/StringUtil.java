package net.zive.shibayu.neo.util;

import java.io.UnsupportedEncodingException;

/**
 * ユーティリティクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class StringUtil {
    /**
     * ブランク.
     */
    public static final String BLANK = "";

    /**
     * インスタンス化禁止.
     */
    private StringUtil() {
    }

    /**
     * nullを空白に変換する.
     * @param value 変換対象の値
     * @return valueがnullの場合は空白、それ以外の場合はvalue
     */
    public static String nullToBlank(final String value) {
        if (value == null) {
            return BLANK;
        }
        return value;
    }

    /**
     * 文字列の長さを返す.
     * @param value 値
     * @return 文字列の長さ
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static int len(final String value)
                throws UnsupportedEncodingException {
        byte[] chs = value.getBytes("MS932");
        return chs.length;
    }

    /**
     * 文字列の左空白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String lpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        int padcnt = afterChs.length - beforeChs.length;
        for (int i = 0; i < afterChs.length; i++) {
            if (i < padcnt) {
                afterChs[i] = ' ';
            } else {
                afterChs[i] = beforeChs[i - padcnt];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * 文字列の右空白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String rpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        for (int i = 0; i < afterChs.length; i++) {
            if (i >= beforeChs.length) {
                afterChs[i] = ' ';
            } else {
                afterChs[i] = beforeChs[i];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * 文字列の左0白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String nlpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        int padcnt = afterChs.length - beforeChs.length;
        for (int i = 0; i < afterChs.length; i++) {
            if (i < padcnt) {
                afterChs[i] = '0';
            } else {
                afterChs[i] = beforeChs[i - padcnt];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * 文字列の右0白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String nrpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        for (int i = 0; i < afterChs.length; i++) {
            if (i >= beforeChs.length) {
                afterChs[i] = '0';
            } else {
                afterChs[i] = beforeChs[i];
            }
        }
        return new String(afterChs, "MS932");
    }
}
