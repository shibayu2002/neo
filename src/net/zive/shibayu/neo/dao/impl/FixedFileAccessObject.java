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
 * �f�[�^�A�N�Z�X�I�u�W�F�N�g���ۃN���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class FixedFileAccessObject extends AbstractFileAccessObject {

    /**
     * �R���X�g���N�^.
     * @param reader XMLReader
     * @param logger ���K�[
     * @throws NeoFrameworkException XML��`�s��Exception
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
     * �Ώۍ��ڂ̒l���擾����.
     * @param row �Ώۂ̃��R�[�h
     * @param map �Ώۂ̍��ڑ���
     * @return �l
     * @throws NeoSystemException �V�X�e���G���[
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
