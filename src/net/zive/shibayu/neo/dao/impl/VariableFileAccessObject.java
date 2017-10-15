package net.zive.shibayu.neo.dao.impl;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * データアクセスオブジェクト抽象クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class VariableFileAccessObject extends AbstractFileAccessObject {
    /**
     * ダブルクォートを表す正規表現.
     */
    private static final Pattern DOUBLE_QUOTE_PATTERN = Pattern.compile("\"");

    /**
     * 区切り文字.
     */
    private String delimiter = null;

    /**
     * 囲い文字.
     */
    private String enclosure = null;

    /**
     * コンストラクタ.
     * @param reader XMLReader
     * @param logger ロガー
     * @throws NeoFrameworkException XML定義不正Exception
     */
    public VariableFileAccessObject(final XMLReader reader,
            final Logger logger) throws NeoFrameworkException {
        super(reader, logger);

        if ("Comma".equals(getCommonAtters().get("delimiter"))) {
            delimiter = ",";
        } else {
            delimiter = "\t";
        }
        enclosure = getCommonAtters().get("enclosure");
    }

    @Override
    protected final String createWriteRecord(final Row row,
            final boolean first, final boolean last) throws NeoSystemException {
        StringBuilder stb = new StringBuilder();
        if (getHeaderColumnAtters().size() > 0 && first) {
            boolean firstRow = true;
            for (Map<String, String> map : getHeaderColumnAtters()) {
                if (firstRow) {
                    firstRow = false;
                } else {
                    stb.append(delimiter);
                }
                stb.append(getColumnValue(row, map));
            }
        } else if (getFutterColumnAtters().size() > 0 && last) {
            boolean firstRow = true;
            for (Map<String, String> map : getFutterColumnAtters()) {
                if (firstRow) {
                    firstRow = false;
                } else {
                    stb.append(delimiter);
                }
                stb.append(getColumnValue(row, map));
            }
        } else {
            boolean firstRow = true;
            for (Map<String, String> map : getBodyColumnAtters()) {
                if (firstRow) {
                    firstRow = false;
                } else {
                    stb.append(delimiter);
                }
                stb.append(getColumnValue(row, map));
            }
        }
        return stb.toString();
    }

    /**
     * 対象項目の値を取得する.
     * @param row 対象のレコード
     * @param map 対象の項目属性
     * @return 値
     * @throws NeoSystemException システムエラー
     */
    private String getColumnValue(final Row row,
                final Map<String, String> map) throws NeoSystemException {
        String value = row.getString(map.get("id"));
        if ("DoubleQuotes".equals(enclosure)) {
            return "\""
                    + DOUBLE_QUOTE_PATTERN.matcher(value).replaceAll("\"\"")
                    + "\"";
        } else {
            return value;
        }
    }
}
