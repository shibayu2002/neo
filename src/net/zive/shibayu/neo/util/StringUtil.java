package net.zive.shibayu.neo.util;

import java.io.UnsupportedEncodingException;

/**
 * ���[�e�B���e�B�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class StringUtil {
    /**
     * �u�����N.
     */
    public static final String BLANK = "";

    /**
     * �C���X�^���X���֎~.
     */
    private StringUtil() {
    }

    /**
     * null���󔒂ɕϊ�����.
     * @param value �ϊ��Ώۂ̒l
     * @return value��null�̏ꍇ�͋󔒁A����ȊO�̏ꍇ��value
     */
    public static String nullToBlank(final String value) {
        if (value == null) {
            return BLANK;
        }
        return value;
    }

    /**
     * ������̒�����Ԃ�.
     * @param value �l
     * @return ������̒���
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static int len(final String value)
                throws UnsupportedEncodingException {
        byte[] chs = value.getBytes("MS932");
        return chs.length;
    }

    /**
     * ������̍��󔒖��߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String lpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        int padcnt = afterChs.length - beforeChs.length;
        for (int i = 0; i < afterChs.length; i++) {
            if (i < padcnt) {
                afterChs[i] = ' ';
            } else {
                afterChs[i] = beforeChs[i - padcnt];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * ������̉E�󔒖��߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String rpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        for (int i = 0; i < afterChs.length; i++) {
            if (i >= beforeChs.length) {
                afterChs[i] = ' ';
            } else {
                afterChs[i] = beforeChs[i];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * ������̍�0�����߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String nlpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        int padcnt = afterChs.length - beforeChs.length;
        for (int i = 0; i < afterChs.length; i++) {
            if (i < padcnt) {
                afterChs[i] = '0';
            } else {
                afterChs[i] = beforeChs[i - padcnt];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * ������̉E0�����߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String nrpad(final String value,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        for (int i = 0; i < afterChs.length; i++) {
            if (i >= beforeChs.length) {
                afterChs[i] = '0';
            } else {
                afterChs[i] = beforeChs[i];
            }
        }
        return new String(afterChs, "MS932");
    }
}
