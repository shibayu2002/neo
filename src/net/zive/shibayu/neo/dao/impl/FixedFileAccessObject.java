package net.zive.shibayu.neo.dao.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.NeoUserException;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.StringUtil;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * データアクセスオブジェクト抽象クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class FixedFileAccessObject extends AbstractFileAccessObject {

    /**
     * コンストラクタ.
     * @param reader XMLReader
     * @param logger ロガー
     * @throws NeoFrameworkException XML定義不正Exception
     */
    public FixedFileAccessObject(final XMLReader reader,
            final Logger logger) throws NeoFrameworkException {
        super(reader, logger);
    }


    @Override
    protected final Row createRow(final String line,
                final boolean first, final boolean last)
                        throws NeoSystemException {
        Row row = new Row();
        int start = 0;
        if (getHeaderColumnAtters().size() > 0 && first) {
            for (Map<String, String> map : getHeaderColumnAtters()) {
                start = setColumn(line, row, start, map);
            }
        } else if (getFutterColumnAtters().size() > 0 && last) {
            for (Map<String, String> map : getFutterColumnAtters()) {
                start = setColumn(line, row, start, map);
            }
        } else {
            for (Map<String, String> map : getBodyColumnAtters()) {
                start = setColumn(line, row, start, map);
            }
        }
        return row;
    }

    @Override
    protected final String createWriteRecord(final Row row,
            final boolean first, final boolean last) throws NeoSystemException {
        StringBuilder stb = new StringBuilder();
        if (getHeaderColumnAtters().size() > 0 && first) {
            for (Map<String, String> map : getHeaderColumnAtters()) {
                stb.append(getColumnValue(row, map));
            }
        } else if (getFutterColumnAtters().size() > 0 && last) {
            for (Map<String, String> map : getFutterColumnAtters()) {
                stb.append(getColumnValue(row, map));
            }
        } else {
            for (Map<String, String> map : getBodyColumnAtters()) {
                stb.append(getColumnValue(row, map));
            }
        }
        return stb.toString();
    }

    @Override
    protected final void validateRowCheckForRead(
            final String line, final boolean first, final boolean last)
                    throws NeoFrameworkException {
        long maxLength = 0;
        if (getHeaderColumnAtters().size() > 0 && first) {
            for (Map<String, String> map : getHeaderColumnAtters()) {
                String length = map.get("length");
                maxLength = maxLength + Long.parseLong(length);
            }
        } else if (getFutterColumnAtters().size() > 0 && last) {
            for (Map<String, String> map : getFutterColumnAtters()) {
                String length = map.get("length");
                maxLength = maxLength + Long.parseLong(length);
            }
        } else {
            for (Map<String, String> map : getBodyColumnAtters()) {
                String length = map.get("length");
                maxLength = maxLength + Long.parseLong(length);
            }
        }

        try {
            if (StringUtil.lenB(line) != maxLength) {
                throw new NeoUserException("NEO-A0013",
                        new Long(maxLength).toString());
            }
        } catch (UnsupportedEncodingException e) {
            throw new NeoSystemException(e);
        }
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
        int length = Integer.parseInt(map.get("length"));
        String padding = map.get("padding");
        String value = row.getString(map.get("id"));
        try {
            switch (padding) {
            case "LPad":
                return StringUtil.lpadB(value, length);
            case "RPad":
                return StringUtil.rpadB(value, length);
            case "NLPad":
                return StringUtil.nlpadB(value, length);
            case "NRPad":
                return StringUtil.nrpadB(value, length);
            default:
                return value;
            }
        } catch (UnsupportedEncodingException e) {
            throw new NeoSystemException(e);
        }
    }

    /**
     * 対象項目の値を設定する.
     * @param line 取得元の行データ
     * @param row セット対象のレコード
     * @param start 開始位置
     * @param map 対象の項目属性
     * @return 次の開始位置
     * @throws NeoSystemException システムエラー
     */
    private int setColumn(final String line, final Row row,
                final int start, final Map<String, String> map)
                        throws NeoSystemException {
        String id = map.get("id");
        String length = map.get("length");
        String padding = map.get("padding");
        int end = start + Integer.parseInt(length) - 1;
        try {
            String value = StringUtil.midB(line, start, end);
            try {
                switch (padding) {
                case "LPad":
                    value = StringUtil.ltrimB(value);
                    break;
                case "RPad":
                    value = StringUtil.rtrimB(value);
                    break;
                case "NLPad":
                    value = StringUtil.nltrimB(value);
                    break;
                case "NRPad":
                    value = StringUtil.nrtrimB(value);
                    break;
                default:
                }
            } catch (UnsupportedEncodingException e) {
                throw new NeoSystemException(e);
            }
            row.put(id, value);
            return end + 1;
        } catch (UnsupportedEncodingException e) {
            throw new NeoSystemException(e);
        }
    }
}
