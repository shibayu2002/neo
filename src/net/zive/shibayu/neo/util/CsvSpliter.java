package net.zive.shibayu.neo.util;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * CSV�Ǎ��N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class CsvSpliter {
    /** ��؂蕶�� (default:�J���}). */
    private char mySeparator = ',';
    /** �͂�����(default:�_�u���N�H�[�g). */
    private char myEnclose = '\"';

    /**
     * �R���X�g���N�^.
     * @param separator ��؂蕶��
     * @param enclose �͂�����
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
     * CSV�f�[�^�������s���܂�.
     * @param line �����Ώۂ�CSV�f�[�^
     * @return �������CSV�f�[�^
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
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
     * byte���X�g�𕶎���ɕϊ�����.
     * @param list byte���X�g
     * @return ������
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
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
