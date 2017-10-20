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
    public static int lenB(final String value)
                throws UnsupportedEncodingException {
        byte[] chs = value.getBytes("MS932");
        return chs.length;
    }

    /**
     * 文字列の左パディングを行う.
     * @param value 値
     * @param pad パディング文字
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String lpadB(final String value, final char pad,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        int padcnt = afterChs.length - beforeChs.length;
        for (int i = 0; i < afterChs.length; i++) {
            if (i < padcnt) {
                afterChs[i] = (byte) pad;
            } else {
                afterChs[i] = beforeChs[i - padcnt];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * 文字列の左空白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String lpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return lpadB(value, ' ', length);
    }

    /**
     * 文字列の左0白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String nlpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return lpadB(value, '0', length);
    }

    /**
     * 文字列の右パディングを行う.
     * @param value 値
     * @param pad パディング文字
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String rpadB(final String value, final char pad,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        for (int i = 0; i < afterChs.length; i++) {
            if (i >= beforeChs.length) {
                afterChs[i] = (byte) pad;
            } else {
                afterChs[i] = beforeChs[i];
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
    public static String rpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return rpadB(value, ' ', length);
    }

    /**
     * 文字列の右0白埋めを行う.
     * @param value 値
     * @param length 桁数
     * @return パディング後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String nrpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return rpadB(value, '0', length);
    }

    /**
     * 文字列の左トリムを行う.
     * @param value 値
     * @param trim トリム文字
     * @return トリム後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String ltrimB(final String value, final char trim)
                    throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        int trimcnt = 0;
        for (int i = 0; i < beforeChs.length; i++) {
            if (beforeChs[i] == trim) {
                trimcnt++;
            } else {
                break;
            }
        }
        byte[] afterChs = new byte[beforeChs.length - trimcnt];
        for (int i = 0; i < afterChs.length; i++) {
            afterChs[i] = beforeChs[i + trimcnt];
        }
        return new String(afterChs, "MS932");
    }

    /**
     * 文字列の左空白除去を行う.
     * @param value 値
     * @return トリム後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String ltrimB(final String value)
                throws UnsupportedEncodingException {
        return ltrimB(value, ' ');
    }

    /**
     * 文字列の左0除去を行う.
     * @param value 値
     * @return トリム後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String nltrimB(final String value)
                throws UnsupportedEncodingException {
        return ltrimB(value, '0');
    }

    /**
     * 文字列の右トリムを行う.
     * @param value 値
     * @param trim トリム文字
     * @return トリム後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String rtrimB(final String value, final char trim)
                    throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        int trimcnt = 0;
        for (int i = beforeChs.length - 1; i >= 0; i--) {
            if (beforeChs[i] == trim) {
                trimcnt++;
            } else {
                break;
            }
        }
        byte[] afterChs = new byte[beforeChs.length - trimcnt];
        for (int i = afterChs.length - 1; i >= 0; i--) {
            afterChs[i] = beforeChs[i];
        }
        return new String(afterChs, "MS932");
    }

    /**
     * 文字列の右空白除去を行う.
     * @param value 値
     * @return トリム後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String rtrimB(final String value)
                throws UnsupportedEncodingException {
        return rtrimB(value, ' ');
    }

    /**
     * 文字列の右0除去を行う.
     * @param value 値
     * @return トリム後の値
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String nrtrimB(final String value)
                throws UnsupportedEncodingException {
        return rtrimB(value, '0');
    }

    /**
     * 文字列の切り出しを行う.
     * @param value 値
     * @param start 開始位置(0～値のサイズ)
     * @param end 終了位置(0～値のサイズ)
     * @return 切り出し後の値
     * @throws IndexOutOfBoundsException 開始、終了位置指定の不正エラー
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public static String midB(final String value,
                    final int start, final int end)
                    throws IndexOutOfBoundsException,
                        UnsupportedEncodingException {
        if (start < 0) {
            throw new IndexOutOfBoundsException("startは0以上の値を指定して下さい。");
        }

        if (start > end) {
            throw new IndexOutOfBoundsException("endはstart以上の値を指定して下さい。");
        }

        byte[] chs = value.getBytes("MS932");
        if (start > chs.length - 1) {
            throw new IndexOutOfBoundsException("startの値がvalueのサイズを超えています。");
        }

        if (end > chs.length - 1) {
            throw new IndexOutOfBoundsException("endの値がvalueのサイズを超えています。");
        }

        byte[] afterChs = new byte[end - start + 1];
        for (int i = 0; i < afterChs.length; i++) {
                afterChs[i] = chs[start + i];
        }
        return new String(afterChs, "MS932");
    }
}
