package net.zive.shibayu.neo.dao.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Logger;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
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
                return StringUtil.lpad(value, length);
            case "RPad":
                return StringUtil.rpad(value, length);
            case "NLPad":
                return StringUtil.nlpad(value, length);
            case "NRPad":
                return StringUtil.nrpad(value, length);
            default:
                return value;
            }
        } catch (UnsupportedEncodingException e) {
            throw new NeoSystemException(e);
        }
    }
}
