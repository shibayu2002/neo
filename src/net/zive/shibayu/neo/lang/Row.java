package net.zive.shibayu.neo.lang;

import java.util.HashMap;

import net.zive.shibayu.neo.util.StringUtil;

/**
 * ���R�[�h�Z�b�g��1�s��\���N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class Row extends HashMap<String, Object> {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * �w�肳�ꂽ�L�[���}�b�v����Ă���l�𕶎���ŕԂ��܂�.
     * @param key �֘A�t����ꂽ�l���Ԃ����L�[
     * @return �w�肳�ꂽ�L�[�Ƀ}�b�v����Ă���l�B���̃L�[�̃}�b�s���O�����̃}�b�v�Ɋ܂܂�Ă��Ȃ��ꍇ�� �u�����N
     */
    public final String getString(final String key) {
        Object value = get(key);
        if (value == null) {
            return StringUtil.BLANK;
        }
        return value.toString();
    }

    /**
     * �w�肳�ꂽ�L�[���}�b�v����Ă���l�𐮐��ŕԂ��܂�.
     * @param key �֘A�t����ꂽ�l���Ԃ����L�[
     * @return �w�肳�ꂽ�L�[�Ƀ}�b�v����Ă���l�B���̃L�[�̃}�b�s���O�����̃}�b�v�Ɋ܂܂�Ă��Ȃ��ꍇ��0�B
     * @throws NumberFormatException �w�肳�ꂽ�L�[�Ƀ}�b�v����Ă���l�����l�Ŗ����ꍇ
     */
    public final int getInt(final String key) throws NumberFormatException {
        Object value = get(key);
        if (value == null) {
            return 0;
        }
        return Integer.parseInt(value.toString());
    }
}
