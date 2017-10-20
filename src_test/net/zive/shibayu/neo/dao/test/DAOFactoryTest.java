package net.zive.shibayu.neo.dao.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.dao.DAOFactory;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;

/**
 * DAOFactory�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class DAOFactoryTest extends TestCase {
    /**
     * �e�X�g�p�̒l.
     */
    private static final Double TEST_DOUBLE_VALUE = 1.1;

    /**
     * �e�X�g�p�̒l.
     */
    private static final int TEST_REC_COUNT = 10;

    /**
     * ���ʃe�X�g�f�[�^.
     */
    private RecordSet rec = new RecordSet();

    @Override
    protected final void setUp() throws Exception {
        super.setUp();

        Row hrow = new Row();
        hrow.put("date", "9999/99/99");
        hrow.put("count", TEST_REC_COUNT);
        rec.add(hrow);

        for (int i = 1; i <= TEST_REC_COUNT; i++) {
            Row row = new Row();
            row.put("column1", null);
            row.put("column2", "aaa" + i);
            row.put("column3", new Integer(i));
            row.put("column4", TEST_DOUBLE_VALUE);
            rec.add(row);
        }
    }

    /**
     * create()�̃e�X�g(����n).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateNormal() throws Exception {
        DAOFactory factory = DAOFactory.getInstance();
        factory.create("FixedFileAccessObjectTest.xml");
        try {
            factory.create("none.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0003:DAO��`�t�@�C��(XML)�̓ǂݍ��݂Ɏ��s���܂����B�t�@�C��=[none.xml]�B",
                e.getMessage());
        }
        try {
            factory.create("FixedFileAccessObjectErrorTest1.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0002:DAO��`�t�@�C��(XML)���s���ł��B/define/@type����`����Ă��܂���B",
                e.getMessage());
        }
        try {
            factory.create("FixedFileAccessObjectErrorTest2.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[cvc-complex-type.4: �v�f'common'�ɑ���'file'���܂܂�Ă���K�v������܂��B]�B",
            e.getMessage());
        }
        try {
            factory.create("FixedFileAccessObjectErrorTest3.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B�t�@�C��=[neo/xsd/NoneType.xsd]]�B",
            e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n1).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError1() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest1.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0002:DAO��`�t�@�C��(XML)���s���ł��B/define/@type����`����Ă��܂���B",
                e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n2).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError2() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest2.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[cvc-complex-type.4: �v�f'common'�ɑ���'file'���܂܂�Ă���K�v������܂��B]�B",
            e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n3).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError3() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest3.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B�t�@�C��=[neo/xsd/NoneType.xsd]]�B",
            e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n4).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError4() throws Exception {
        try {
            DAOFactory.getInstance().create("none.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0003:DAO��`�t�@�C��(XML)�̓ǂݍ��݂Ɏ��s���܂����B�t�@�C��=[none.xml]�B",
                e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n5).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError5() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest5.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[cvc-complex-type.2.4.a: "
            + "�v�f'header'�Ŏn�܂閳���ȃR���e���c��������܂����B"
            + "'{common}'�̂����ꂩ���K�v�ł��B]�B",
            e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n6).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError6() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest6.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[cvc-complex-type.2.4.a: �v�f'header'�Ŏn�܂閳���ȃR���e���c��������܂����B"
            + "'{body}'�̂����ꂩ���K�v�ł��B]�B",
            e.getMessage());
        }
    }

    /**
     * create()�̃e�X�g(�ُ�n7).
     * @throws Exception �V�X�e���G���[
     */
    public void testCreateError7() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest7.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO��`�t�@�C��(XML)�̑Ó������؂ŃG���[���������܂����B"
            + "�ڍ�=[cvc-enumeration-valid: �l'CR'�́A��'[CRLF, LF]'�ɑ΂���"
            + "�t�@�Z�b�g���L���ł͂���܂���B�񋓂���̒l�ł���K�v������܂��B]�B",
            e.getMessage());
        }
    }
}
