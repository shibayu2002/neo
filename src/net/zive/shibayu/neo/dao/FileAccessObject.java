package net.zive.shibayu.neo.dao;

import java.io.File;

/**
 * �t�@�C���A�N�Z�X�I�u�W�F�N�g�C���^�[�t�F�[�X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public interface FileAccessObject extends DataAccessObject {
    /**
     * �t�@�C�������݂���ꍇ�̏������@.
     */
    enum WriteMode {
        /** �������f(�f�t�H���g). */
        CANCEL,
        /** �㏑��. */
        OVERWRITE,
        /** �ǋL. */
        APPEND
    }

    /**
     * �����Ώۃt�@�C����ύX����.
     * @param file �����Ώۃt�@�C��
     */
    void changeFile(File file);

    /**
     * �����Ώۃt�@�C����Ԃ�.
     * @return �����Ώۃt�@�C��
     */
    File getFile();

    /**
     * �t�@�C�������݂���ꍇ�̏������@��ݒ肷��.
     * @param mode �t�@�C�������݂���ꍇ�̏������@
     */
    void setWriteMode(WriteMode mode);

    /**
     * �t�@�C���o�͑O�o�b�N�A�b�v�L����ݒ肷��.
     * @param mode true:�o�b�N�A�b�v���쐬����Bfalse�F�o�b�N�A�b�v���쐬���Ȃ��B
     */
    void setBackupMode(boolean mode);
}
