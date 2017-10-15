package net.zive.shibayu.neo.dao;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.RecordSet;

/**
 * �f�[�^�A�N�Z�X�I�u�W�F�N�g�C���^�[�t�F�[�X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public interface DataAccessObject {
    /**
     * ���R�[�h�`�F�b�N�G���[���̏������@.<br>
     */
    enum RecordErrorMode {
        /** �������f(�f�t�H���g). */
        CANCEL,
        /** �X�L�b�v. */
        SKIP,
        /** ���ɖ߂�. */
        ROLLBACK,
        /** ����. */
        IGNORE
    }

    /**
     * �f�[�^���͂��s��.
     * @return RecordSet
     * @throws NeoFrameworkException �G���[
     */
    RecordSet read() throws NeoFrameworkException;

    /**
     * �f�[�^�o�͂��s��.
     * @param rec ���R�[�h�Z�b�g
     * @throws NeoFrameworkException �G���[
     */
    void write(RecordSet rec) throws NeoFrameworkException;

    /**
     * �f�[�^�Ǎ��ݑO���R�[�h�P��Hock����.
     * @param f Hock�֐�
     */
    void beforeReadRowFunc(HockFunction f);

    /**
     * �f�[�^�����ݑO���R�[�h�P��Hock����.
     * @param f Hock�֐�
     */
    void beforeWriteRowFunc(HockFunction f);

    /**
     * ���R�[�h�`�F�b�N�G���[���̏������@��ݒ肷��.
     * @param mode ���R�[�h�`�F�b�N�G���[���̏������@
     */
    void setRecordErrorMode(RecordErrorMode mode);
}