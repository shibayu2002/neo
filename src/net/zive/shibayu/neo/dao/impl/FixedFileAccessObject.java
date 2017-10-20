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
     * �Ώۍ��ڂ̒l��ݒ肷��.
     * @param line �擾���̍s�f�[�^
     * @param row �Z�b�g�Ώۂ̃��R�[�h
     * @param start �J�n�ʒu
     * @param map �Ώۂ̍��ڑ���
     * @return ���̊J�n�ʒu
     * @throws NeoSystemException �V�X�e���G���[
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
