package net.zive.shibayu.neo.dao;

import net.zive.shibayu.neo.lang.Row;

/**
 * �f�[�^���o�͑O���Hock�֐��C���^�[�t�F�[�X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
@FunctionalInterface
public interface HockFunction {
    /**
     * Hock�֐�.
     * @param row �����Ώۃ��R�[�h
     * @return �㑱�������s�Ftrue, �㑱�����X�L�b�v�Ffalse
     */
    boolean hock(Row row);
}
