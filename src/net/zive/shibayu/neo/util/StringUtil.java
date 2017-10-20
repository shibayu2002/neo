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
    public static int lenB(final String value)
                throws UnsupportedEncodingException {
        byte[] chs = value.getBytes("MS932");
        return chs.length;
    }

    /**
     * ������̍��p�f�B���O���s��.
     * @param value �l
     * @param pad �p�f�B���O����
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String lpadB(final String value, final char pad,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        int padcnt = afterChs.length - beforeChs.length;
        for (int i = 0; i < afterChs.length; i++) {
            if (i < padcnt) {
                afterChs[i] = (byte) pad;
            } else {
                afterChs[i] = beforeChs[i - padcnt];
            }
        }
        return new String(afterChs, "MS932");
    }

    /**
     * ������̍��󔒖��߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String lpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return lpadB(value, ' ', length);
    }

    /**
     * ������̍�0�����߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String nlpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return lpadB(value, '0', length);
    }

    /**
     * ������̉E�p�f�B���O���s��.
     * @param value �l
     * @param pad �p�f�B���O����
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String rpadB(final String value, final char pad,
            final int length) throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        byte[] afterChs = new byte[length];
        for (int i = 0; i < afterChs.length; i++) {
            if (i >= beforeChs.length) {
                afterChs[i] = (byte) pad;
            } else {
                afterChs[i] = beforeChs[i];
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
    public static String rpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return rpadB(value, ' ', length);
    }

    /**
     * ������̉E0�����߂��s��.
     * @param value �l
     * @param length ����
     * @return �p�f�B���O��̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String nrpadB(final String value,
            final int length) throws UnsupportedEncodingException {
        return rpadB(value, '0', length);
    }

    /**
     * ������̍��g�������s��.
     * @param value �l
     * @param trim �g��������
     * @return �g������̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String ltrimB(final String value, final char trim)
                    throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        int trimcnt = 0;
        for (int i = 0; i < beforeChs.length; i++) {
            if (beforeChs[i] == trim) {
                trimcnt++;
            } else {
                break;
            }
        }
        byte[] afterChs = new byte[beforeChs.length - trimcnt];
        for (int i = 0; i < afterChs.length; i++) {
            afterChs[i] = beforeChs[i + trimcnt];
        }
        return new String(afterChs, "MS932");
    }

    /**
     * ������̍��󔒏������s��.
     * @param value �l
     * @return �g������̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String ltrimB(final String value)
                throws UnsupportedEncodingException {
        return ltrimB(value, ' ');
    }

    /**
     * ������̍�0�������s��.
     * @param value �l
     * @return �g������̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String nltrimB(final String value)
                throws UnsupportedEncodingException {
        return ltrimB(value, '0');
    }

    /**
     * ������̉E�g�������s��.
     * @param value �l
     * @param trim �g��������
     * @return �g������̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String rtrimB(final String value, final char trim)
                    throws UnsupportedEncodingException {
        byte[] beforeChs = value.getBytes("MS932");
        int trimcnt = 0;
        for (int i = beforeChs.length - 1; i >= 0; i--) {
            if (beforeChs[i] == trim) {
                trimcnt++;
            } else {
                break;
            }
        }
        byte[] afterChs = new byte[beforeChs.length - trimcnt];
        for (int i = afterChs.length - 1; i >= 0; i--) {
            afterChs[i] = beforeChs[i];
        }
        return new String(afterChs, "MS932");
    }

    /**
     * ������̉E�󔒏������s��.
     * @param value �l
     * @return �g������̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String rtrimB(final String value)
                throws UnsupportedEncodingException {
        return rtrimB(value, ' ');
    }

    /**
     * ������̉E0�������s��.
     * @param value �l
     * @return �g������̒l
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String nrtrimB(final String value)
                throws UnsupportedEncodingException {
        return rtrimB(value, '0');
    }

    /**
     * ������̐؂�o�����s��.
     * @param value �l
     * @param start �J�n�ʒu(0�`�l�̃T�C�Y)
     * @param end �I���ʒu(0�`�l�̃T�C�Y)
     * @return �؂�o����̒l
     * @throws IndexOutOfBoundsException �J�n�A�I���ʒu�w��̕s���G���[
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public static String midB(final String value,
                    final int start, final int end)
                    throws IndexOutOfBoundsException,
                        UnsupportedEncodingException {
        if (start < 0) {
            throw new IndexOutOfBoundsException("start��0�ȏ�̒l���w�肵�ĉ������B");
        }

        if (start > end) {
            throw new IndexOutOfBoundsException("end��start�ȏ�̒l���w�肵�ĉ������B");
        }

        byte[] chs = value.getBytes("MS932");
        if (start > chs.length - 1) {
            throw new IndexOutOfBoundsException("start�̒l��value�̃T�C�Y�𒴂��Ă��܂��B");
        }

        if (end > chs.length - 1) {
            throw new IndexOutOfBoundsException("end�̒l��value�̃T�C�Y�𒴂��Ă��܂��B");
        }

        byte[] afterChs = new byte[end - start + 1];
        for (int i = 0; i < afterChs.length; i++) {
                afterChs[i] = chs[start + i];
        }
        return new String(afterChs, "MS932");
    }
}
