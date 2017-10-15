package net.zive.shibayu.neo.dao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * DAO(�f�[�^�A�N�Z�X�I�u�W�F�N�g)�����N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class DAOFactory {
    /**
     * �V���O���g���C���X�^���X.
     */
    private static DAOFactory instance = new DAOFactory();

    /**
     * �����ς�DataAccessObject��ێ�����}�b�v.
     */
    private Map<String, DataAccessObject> daoMap
                = new HashMap<String, DataAccessObject>();
    /**
     * �f�[�^�A�N�Z�X�I�u�W�F�N�g�����N���X�̃C���X�^���X��Ԃ�.
     * @return �f�[�^�A�N�Z�X�I�u�W�F�N�g�����N���X�̃C���X�^���X
     */
    public static DAOFactory getInstance() {
        return instance;
    }

    /**
     * �R���X�g���N�^.<br>
     * �O���A�N�Z�X�֎~
     */
    private DAOFactory() {
    }

    /**
     * DAO��`�t�@�C����ǂݍ���DAO�𐶐�����.
     * <ul>
     * <li>����A�N�Z�X�̏ꍇ
     * <ol>
     * <li>DAO��`�t�@�C����Ǎ��ށB
     * <li>DAO��`���(type)���擾����B
     * <li>DAO��`�t�@�C��(xml)�Ó�������<br>
     * �X�L�[�}��`�t�@�C��(neo/xsd/(DAO��`���).xsd)��p���đÓ����̌��؂��s���B
     * <li>DAO��`���(type)�ɊY������DataAccessObject�𐶐�����B
     * <li>DAO��`�}�b�v��DataAccessObject���i�[����B
     * <li>DataAccessObject��ԋp����B
     * </ol>
     * <li>����ȍ~�̃A�N�Z�X�̏ꍇ
     * <ol>
     * <li>DAO��`�}�b�v���p�����^xml�Ŏw�肳�ꂽDataAccessObject���擾���ԋp����B
     * </ol>
     * </ul>
     * @param xml DAO��`�t�@�C��
     * @param logger DAO�ɐݒ肷�郍�K�[
     * @return DAO
     * @throws NeoFrameworkException �G���[
     */
    public DataAccessObject create(final String xml, final Logger logger)
            throws NeoFrameworkException {
        if (!daoMap.containsKey(xml)) {
            XMLReader reader = readDaoXML(xml);
            String type = getType(reader);
            schemaValidate(reader, type);
            DataAccessObject dao = createDataAccessObject(reader, type, logger);
            daoMap.put(xml, dao);
        }
        return daoMap.get(xml);
    }

    /**
     * DAO��`�t�@�C����ǂݍ���DAO�𐶐�����.
     * <p>�ڍׂ�create(final String xml, final Logger logger)�Q��</p>
     * @param xml DAO��`�t�@�C��
     * @return DAO
     * @throws NeoFrameworkException �G���[
     */
    public DataAccessObject create(final String xml)
            throws NeoFrameworkException {
        return create(xml, Logger.getGlobal());
    }

    /**
     * DAO��`�t�@�C����Ǎ��ނ��߂̃��[�_�[���擾����.
     * @param xml DAO��`�t�@�C��
     * @return DAO��`�t�@�C�����[�_�[
     * @throws NeoFrameworkException DAO��`�t�@�C���Ǎ��G���[
     */
    private XMLReader readDaoXML(final String xml)
                throws NeoFrameworkException {
        try {
            return XMLReader.createInstanceAsResouce(xml);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0003", xml);
        }
    }

    /**
     * DAO��`���(type)���擾����.
     * @param reader XML���[�_�[
     * @return DAO��`���(type)
     * @throws NeoFrameworkException DAO��`��ʎ擾�G���[
     */
    private String getType(final XMLReader reader)
                throws NeoFrameworkException {
        try {
            String type = reader.readString("/define/@type");
            if ("".equals(type)) {
                throw new NeoSystemException("NEO-E0002",
                        "/define/@type����`����Ă��܂���");
            }
            return type;
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0002",
                    "/define/@type����`����Ă��܂���");
        }
    }

    /**
     * DAO��`�t�@�C��(xml)�Ó�������.
     * @param reader XML���[�_�[
     * @param type DAO��`���(type)
     * @throws NeoFrameworkException �Ó������؃G���[
     */
    private void schemaValidate(final XMLReader reader,
                final String type) throws NeoFrameworkException {
        try {
            reader.validate("neo/xsd/" + type + ".xsd");
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0004",
                    e.getMessage());
        }
    }

    /**
     * DAO��`���(type)�ɊY������DataAccessObject�𐶐�����.
     * @param reader XML���[�_�[
     * @param type DAO��`���(type)
     * @param logger ���K�[
     * @return DataAccessObject
     * @throws NeoFrameworkException DataAccessObject�����G���[
     */
    private DataAccessObject createDataAccessObject(
                final XMLReader reader, final String type,
                final Logger logger) throws NeoFrameworkException {
        String path = this.getClass().getPackage().getName() + ".impl";

        try {
            Class<?> clazz = Class.forName(path + "." + type + "AccessObject");
            Class<?>[] types = {XMLReader.class, Logger.class};
            Constructor<?> constructor = clazz.getConstructor(types);

            Object[] args = {reader, logger};
            return (DataAccessObject) constructor.newInstance(args);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0005",
                    e.getMessage());
        }
    }
}
