package net.zive.shibayu.neo.dao.impl;

import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * �f�[�^�A�N�Z�X�I�u�W�F�N�g���ۃN���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class VariableFileAccessObject extends AbstractFileAccessObject {
    /**
     * �_�u���N�H�[�g��\�����K�\��.
     */
    private static final Pattern DOUBLE_QUOTE_PATTERN = Pattern.compile("\"");

    /**
     * ��؂蕶��.
     */
    private String delimiter = null;

    /**
     * �͂�����.
     */
    private String enclosure = null;

    /**
     * �R���X�g���N�^.
     * @param reader XMLReader
     * @param logger ���K�[
     * @throws NeoFrameworkException XML��`�s��Exception
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
        // TODO �쐬��
        return null;
    }

    @Override
    protected void validateRowCheckForRead(
            final String line, final boolean first, final boolean last)
                    throws NeoFrameworkException {
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
