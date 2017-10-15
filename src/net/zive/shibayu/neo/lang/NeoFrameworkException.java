package net.zive.shibayu.neo.lang;

import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import net.zive.shibayu.neo.Core;

/**
 * XML��`�s��Exception�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public abstract class NeoFrameworkException extends Exception {
    /**
     * �V���A���o�[�W����.
     */
    private static final long serialVersionUID = 1L;

    /**
     * ���b�Z�[�W�v���p�e�B�t�@�C��.
     */
    private static Properties msgProperties = null;

    /**
     * �G���[�R�[�h���擾���鐳�K�\��.
     */
    private static final Pattern GET_CODE_PATTERN = Pattern.compile(":.*");

    /**
     * �G���[�R�[�h���������G���[���b�Z�[�W���擾���鐳�K�\��.
     */
    private static final Pattern GET_MESSAGE_PATTERN = Pattern.compile("^.*?:");

    /**
     * �V�X�e���G���[�p�^�[�����K�\��.
     */
    private static final Pattern SYSTEM_ERROR_PATTERN
                            = Pattern.compile("^NEO-E.*");

    /**
     * �A�v���P�[�V�����G���[�p�^�[�����K�\��.
     */
    private static final Pattern APPLICATION_ERRO_PATTERN
                            = Pattern.compile("^NEO-A.*");

    /**
     * Exception��������Exception.
     */
    private Exception sourceException = null;

    /**
     * �R���X�g���N�^.
     * @param e �G���[������Exception
     */
    protected NeoFrameworkException(final Exception e) {
        this(convToMsg("NEO-E0001", e.getMessage()), e);
    }

    /**
     * �R���X�g���N�^.
     * @param code ���b�Z�[�W�R�[�h
     * @param args �σ��b�Z�[�W
     */
    protected NeoFrameworkException(final String code,
            final Object ...args) {
        super(convToMsg(code, args));
    }

    /**
     * �G���[�R�[�h��Ԃ�.
     * @return �G���[�R�[�h
     */
    public String getCode() {
        return GET_CODE_PATTERN.matcher(getMessage()).replaceAll("");
    }

    /**
     * �G���[�R�[�h���������G���[���b�Z�[�W��Ԃ�.
     * @return �G���[�R�[�h���������G���[���b�Z�[�W
     */
    public String getMessageNoneCode() {
        return GET_MESSAGE_PATTERN.matcher(getMessage()).replaceAll("");
    }

    /**
     * �G���[��������Exception��Ԃ�.
     * @return �G���[��������Exception
     */
    public Exception getSourceException() {
        return sourceException;
    }

    /**
     * �V�X�e���G���[����.
     * @return true:�V�X�e���G���[, false:�V�X�e���G���[�ȊO
     */
    public boolean isSystemError() {
        return SYSTEM_ERROR_PATTERN.matcher(getMessage()).matches();
    }

    /**
     * �A�v���P�[�V�����G���[����.
     * @return true:�A�v���P�[�V�����G���[, false:�A�v���P�[�V�����G���[�ȊO
     */
    public boolean isApplicationError() {
        return APPLICATION_ERRO_PATTERN.matcher(getMessage()).matches();
    }

    /**
     * �G���[�R�[�h�����b�Z�[�W�ɕϊ�����.
     * @param code �G���[�R�[�h
     * @return �G���[�R�[�h�F�G���[���b�Z�[�W
     * @param args �σ��b�Z�[�W
     */
    private static String convToMsg(final String code, final Object ...args) {
        if (msgProperties == null) {
            msgProperties = new Properties();
            try {
                msgProperties.load(new InputStreamReader(
                        ClassLoader.getSystemResourceAsStream(
                                "neo/msg/message.properties"), Core.ENCODE));
            } catch (Exception e) {
                return code + ":���b�Z�[�W���擾�ł��܂���ł����B";
            }
        }
        if (msgProperties.containsKey(code)) {
            String msg = msgProperties.getProperty(code);
            return code + ":" + String.format(msg, args);
        }
        return code + ":���b�Z�[�W���擾�ł��܂���ł����B";
    }
}
