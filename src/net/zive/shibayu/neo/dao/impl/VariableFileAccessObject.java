package net.zive.shibayu.neo.dao.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.CsvSpliter;
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

    @Override
    protected final Row createRow(final String line,
                final boolean first, final boolean last)
                        throws NeoSystemException {
        Row row = new Row();
        String[] values = null;
        CsvSpliter spliter = null;
        int index = 0;
        if ("DoubleQuotes".equals(enclosure)) {
            spliter = new CsvSpliter(delimiter, "\"");
        } else {
            spliter = new CsvSpliter(delimiter, "\0");
        }

        try {
            values = spliter.split(line);
        } catch (UnsupportedEncodingException e) {
            throw new NeoSystemException(e);
        }

        if (getHeaderColumnAtters().size() > 0 && first) {
            for (Map<String, String> map : getHeaderColumnAtters()) {
                index = setColumn(values, row, index, map);
            }
        } else if (getFutterColumnAtters().size() > 0 && last) {
            for (Map<String, String> map : getFutterColumnAtters()) {
                index = setColumn(values, row, index, map);
            }
        } else {
            for (Map<String, String> map : getBodyColumnAtters()) {
                index = setColumn(values, row, index, map);
            }
        }
        return row;
    }

    @Override
    protected void validateRowCheckForRead(
            final String line, final boolean first, final boolean last)
                    throws NeoFrameworkException {
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

    /**
     * 対象項目の値を設定する.
     * @param values 取得元の行データ
     * @param row セット対象のレコード
     * @param index インデックス
     * @param map 対象の項目属性
     * @return 次の開始位置
     * @throws NeoSystemException システムエラー
     */
    private int setColumn(final String[] values, final Row row,
                final int index, final Map<String, String> map)
                        throws NeoSystemException {
        String id = map.get("id");
        String value = values[index];
        row.put(id, value);
        return index + 1;
    }
}
