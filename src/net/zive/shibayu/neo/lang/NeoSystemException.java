package net.zive.shibayu.neo.lang;

/**
 * XML��`�s��Exception�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class NeoSystemException extends NeoFrameworkException {
    /**
     * �V���A���o�[�W����.
     */
    private static final long serialVersionUID = 1L;

    /**
     * �R���X�g���N�^.
     * @param e �G���[������Exception
     */
    public NeoSystemException(final Exception e) {
        super(e);
    }

    /**
     * �R���X�g���N�^.
     * @param code ���b�Z�[�W�R�[�h
     * @param args �σ��b�Z�[�W
     */
    public NeoSystemException(final String code, final Object ...args) {
        super(code, args);
    }
}
