package net.zive.shibayu.neo.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.zive.shibayu.neo.dao.DataAccessObject;
import net.zive.shibayu.neo.dao.HockFunction;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * �f�[�^�A�N�Z�X�I�u�W�F�N�g���ۃN���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public abstract class AbstractDataAccessObject
                        implements DataAccessObject {
    /**
     * ���K�[.
     */
    private Logger myLogger = null;

    /**
     * root��`�}�b�v (�L�[�F����ID�A�l�F�����l).
     */
    private Map<String, String> rootAtters = null;

    /**
     * ���ʒ�`�}�b�v (�L�[�F����ID�A�l�F�����l).
     */
    private Map<String, String> commonAtters = null;

    /**
     * �f�[�^�Ǎ��ݑOHock����.
     */
    private HockFunction beforeReadRowFunc = null;

    /**
     * �f�[�^�����ݑOHock����.
     */
    private HockFunction beforeWriteRowFunc = null;

    /**
     * �R���X�g���N�^.
     * <ol>
     * <li>���K�[��ݒ肷��B<br>
     * �p�����^�Ŏw�肳�ꂽ���K�[��DataAccessObject�̃��K�[�ɐݒ肷��B<br>
     * �p�����^�Ŏw�肳�ꂽ���K�[��null�̏ꍇ�̓O���[�o�����K�[��ݒ肷��B
     * <li>DAO��`�t�@�C����define��`��ǂݍ��ށB
     * <li>DAO��`�t�@�C����common��`��ǂݍ��ށB
     * </ol>
     * @param reader DAO��`�t�@�C���Ǎ��p��XMLReader
     * @param logger ���K�[
     * @throws NeoFrameworkException �V�X�e���G���[
     */
    public AbstractDataAccessObject(final XMLReader reader,
            final Logger logger) throws NeoFrameworkException {
        if (logger == null) {
            this.myLogger = Logger.getGlobal();
        } else {
            this.myLogger = logger;
        }
        try {
            rootAtters = createNodeDef(
                    reader.readNode("/define").getAttributes());
            commonAtters = createNodeDef(
                    reader.readNode("//common").getAttributes());
            info("load xml. define:" + rootAtters);
            info("common:" + commonAtters);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0001", e);
        }
    }

    @Override
    public final void beforeReadRowFunc(final HockFunction f) {
        beforeReadRowFunc = f;
    }

    @Override
    public final void beforeWriteRowFunc(final HockFunction f) {
        beforeWriteRowFunc = f;
    }

    /**
     * ���ʒ�`�̃Q�b�^�[.
     * @return ���ʒ�`Map
     */
    protected final Map<String, String> getCommonAtters() {
        return commonAtters;
    }

    /**
     * ���ڒ�`���X�g�𐶐�����.
     * @param entries ���ڒ�`
     * @return ���ڒ�`���X�g (�L�[�F����ID�A�l�F�����l)
     */
    protected List<Map<String, String>> createNodeDefList(
                final NodeList entries) {
        List<Map<String, String>> columnList =
                new LinkedList<Map<String, String>>();
        for (int i = 0; i < entries.getLength(); i++) {
            Map<String, String> defMap = createNodeDef(
                    entries.item(i).getAttributes());
            columnList.add(defMap);
        }
        return columnList;
    }

    /**
     * ���ڒ�`�𐶐�����.
     * @param attrs ���ڒ�`
     * @return ���ڒ�` (�L�[�F����ID�A�l�F�����l)
     */
    protected Map<String, String> createNodeDef(final NamedNodeMap attrs) {
        Map<String, String> defMap = new HashMap<>();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            defMap.put(attr.getNodeName(), attr.getNodeValue());
        }
        return defMap;
    }

    /**
     * ���R�[�h�ǂݍ��ݑOHock����.
     * @param row �����Ώۃ��R�[�h
     * @return �㑱�������s�Ftrue, �㑱�����X�L�b�v�Ffalse
     */
    protected boolean hockBeforeRead(final Row row) {
        if (beforeReadRowFunc != null) {
            beforeReadRowFunc.hock(row);
        }
        return true;
    }

    /**
     * ���R�[�h�����ݑOHock����.
     * @param row �����Ώۃ��R�[�h
     * @return �㑱�������s�Ftrue, �㑱�����X�L�b�v�Ffalse
     */
    protected boolean hockBeforeWrite(final Row row) {
        if (beforeWriteRowFunc != null) {
            beforeWriteRowFunc.hock(row);
        }
        return true;
    }

    /**
     * info���O�o��.
     * @param msg ���b�Z�[�W
     */
    protected void info(final String msg) {
        myLogger.info("[NeoFramework] " + msg);
    }
}
