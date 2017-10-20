package net.zive.shibayu.neo.lang;

import java.util.LinkedList;
import java.util.List;

/**
 * XML��`�s��Exception�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class NeoUserException extends NeoFrameworkException {
    /**
     * �qException.
     */
    private List<NeoUserException> children
                = new LinkedList<NeoUserException>();

    /**
     * �V���A���o�[�W����.
     */
    private static final long serialVersionUID = 1L;

    /**
     * �R���X�g���N�^.
     * @param e �G���[������Exception
     */
    public NeoUserException(final Exception e) {
        super(e);
    }

    /**
     * �R���X�g���N�^.
     * @param code ���b�Z�[�W�R�[�h
     * @param args �σ��b�Z�[�W
     */
    public NeoUserException(final String code, final Object ...args) {
        super(code, args);
    }

    /**
     * �qExceiption��ǉ�����.
     * @param e �qExceiption
     */
    public void addChild(final NeoUserException e) {
        children.add(e);
    }

    /**
     * �qExceiption���X�g��Ԃ�.
     * @return �qExceiption���X�g
     */
    public List<NeoUserException> children() {
        return children;
    }

    @Override
    public final String getMessage() {
        return this.getMessages("");
    }

    /**
     * �q�G���[���܂߂��G���[���b�Z�[�W�̈ꗗ��Ԃ�.
     * @param index ������̑O�ɕt�^����C���f�b�N�X
     * @return �G���[���b�Z�[�W�ꗗ������
     */
    private String getMessages(final String index) {
        StringBuilder stb = new StringBuilder();
        stb.append(index + super.getMessage()).append("\r\n");
        for (NeoUserException e : children) {
            stb.append(e.getMessages(index + "\t"));
        }
        return stb.toString();
    }
}
