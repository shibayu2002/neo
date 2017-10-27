package net.zive.shibayu.neo.util;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * CSV読込クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class CsvSpliter {
    /** 区切り文字 (default:カンマ). */
    private char mySeparator = ',';
    /** 囲い文字(default:ダブルクォート). */
    private char myEnclose = '\"';

    /**
     * コンストラクタ.
     * @param separator 区切り文字
     * @param enclose 囲い文字
     */
    public CsvSpliter(final String separator,
            final String enclose) {
        if (separator.equals(",")) {
            mySeparator = ',';
        } else {
            mySeparator = '\t';
        }

        if (enclose.equals("\"")) {
            myEnclose = '\"';
        } else {
            myEnclose = '\0';
        }
    }

    /**
     * CSVデータ分割を行います.
     * @param line 分割対象のCSVデータ
     * @return 分割後のCSVデータ
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public String[] split(final String line)
                throws UnsupportedEncodingException {
        List<String> list = new LinkedList<String>();
        List<Byte> value = new LinkedList<Byte>();

        byte[] chs = line.getBytes("MS932");
        boolean skip = false;
        boolean first = true;
        boolean inStr = true;

        for (int i = 0; i < chs.length; i++) {
            byte ch = chs[i];
            if (i == 0 && chs[0] == mySeparator) {
                list.add(bytesToString(value));
                value = new LinkedList<Byte>();
                first = true;
            } else if (first) {
                first = false;
                if (myEnclose == '\0') {
                    value.add(ch);
                } else if (ch != myEnclose) {
                    value.add(ch);
                }
                if (myEnclose == '\0') {
                    inStr = false;
                } else {
                    inStr = true;
                }
            } else {
                if (skip) {
                    skip = false;
                } else {
                    if (!inStr && ch == mySeparator) {
                        list.add(bytesToString(value));
                        value = new LinkedList<Byte>();
                        first = true;
                    } else if (myEnclose == '\"' && ch == '\"') {
                        if (i < chs.length - 1) {
                            if (chs[i + 1] == '\"') {
                                value.add(ch);
                                skip = true;
                            } else {
                                inStr = false;
                            }
                        } else {
                            inStr = false;
                        }
                    } else {
                        value.add(ch);
                    }
                }
            }
        }

        if (value.size() > 0) {
            list.add(bytesToString(value));
        } else if (first) {
            list.add(bytesToString(value));
        }

        return list.toArray(new String[0]);
    }

    /**
     * byteリストを文字列に変換する.
     * @param list byteリスト
     * @return 文字列
     * @throws UnsupportedEncodingException エンコードエラー
     */
    private String bytesToString(final List<Byte> list)
                throws UnsupportedEncodingException {
        byte[] chs = new byte[list.size()];
        for (int i = 0; i < chs.length; i++) {
            chs[i] = list.get(i);
        }
        return new String(chs, "MS932");
    }
}
