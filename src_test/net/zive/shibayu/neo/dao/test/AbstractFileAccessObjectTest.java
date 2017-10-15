package net.zive.shibayu.neo.dao.test;

import java.io.File;
import java.io.FilenameFilter;

import junit.framework.TestCase;
import net.zive.shibayu.neo.dao.DAOFactory;
import net.zive.shibayu.neo.dao.FileAccessObject;
import net.zive.shibayu.neo.dao.DataAccessObject.RecordErrorMode;
import net.zive.shibayu.neo.dao.FileAccessObject.WriteMode;
import net.zive.shibayu.neo.lang.NeoUserException;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;

/**
 * FileAccessObject�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class AbstractFileAccessObjectTest extends TestCase {
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
            row.put("userId", "user" + i);
            row.put("userName", "���[�U�[" + i);
            row.put("password", "xxxxx");
            row.put("tel", "999-9999-999" + i);
            row.put("mailAdress", "user" + i + "@xxxx.jp");
            row.put("remarks", "test" + i);
            rec.add(row);
        }
    }

    /**
     * getFile()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testGetFile() throws Exception {
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        File file = dao.getFile();
        assertEquals("D:\\tmp\\Sample.txt", file.getPath());
    }

    /**
     * changeFile()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testChangeFile() throws Exception {
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        dao.changeFile(new File("D:\\tmp\\Change.txt"));
        File file = dao.getFile();
        assertEquals("D:\\tmp\\Change.txt", file.getPath());
    }

    /**
     * setWriteMode()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testSetWriteMode() throws Exception {
        final long testFileSize = 7622L;
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        File file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }

        dao.write(rec);
        assertEquals(testFileSize, file.length());
        try {
            dao.write(rec);
        } catch (Exception e) {
            assertEquals("NEO-A0004:���Ƀt�@�C�������݂��܂��B[D:\\tmp\\Sample.txt]",
                    e.getMessage());
        }
        dao.setWriteMode(WriteMode.OVERWRITE);
        dao.write(rec);
        assertEquals(testFileSize, file.length());
        dao.setWriteMode(WriteMode.APPEND);
        dao.write(rec);
        assertEquals(testFileSize * 2, file.length());
        try {
            dao.setWriteMode(WriteMode.CANCEL);
            dao.write(rec);
            assertEquals(testFileSize, file.length());
        } catch (Exception e) {
            assertEquals("NEO-A0004:���Ƀt�@�C�������݂��܂��B[D:\\tmp\\Sample.txt]",
                    e.getMessage());
        }
    }

    /**
     * setBackupMode()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testSetBackupMode() throws Exception {

        for (File f : findDir("D:\\tmp\\", "Sample.txt.*\\.zip")) {
            f.delete();
        }
        File file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }

        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        dao.write(rec);
        if (findDir("D:\\tmp\\", "Sample.txt.*\\.zip").length > 0) {
            fail("Sample.txt.*\\.zip���쐬����Ă��܂��B");
        }

        dao.setBackupMode(true);
        dao.setWriteMode(WriteMode.OVERWRITE);
        dao.write(rec);
        if (findDir("D:\\tmp\\", "Sample.txt.*\\.zip").length == 0) {
            fail("Sample.txt.*\\.zip���쐬����Ă��܂���B");
        }
        for (File f : findDir("D:\\tmp\\", "Sample.txt.*\\.zip")) {
            f.delete();
        }

        dao.setBackupMode(false);
        dao.write(rec);
        if (findDir("D:\\tmp\\", "Sample.txt.*\\.zip").length > 0) {
            fail("Sample.txt.*\\.zip���쐬����Ă��܂��B");
        }

        file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }
        dao.setBackupMode(true);
        dao.setWriteMode(WriteMode.APPEND);
        dao.write(rec);
        if (findDir("D:\\tmp\\", "Sample.txt.*\\.zip").length > 0) {
            fail("Sample.txt.*\\.zip���쐬����Ă��܂��B");
        }

        dao.write(rec);
        if (findDir("D:\\tmp\\", "Sample.txt.*\\.zip").length == 0) {
            fail("Sample.txt.*\\.zip���쐬����Ă��܂���B");
        }
    }

    /**
     * write()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testWriteNormal() throws Exception {
        final int testFileSize = 7622;
        final int testFileSize2 = 7611;
        final int testFileSize3 = 7651;
        File file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        dao.write(rec);
        assertEquals(testFileSize, file.length());

        dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest2.xml");
        dao.setWriteMode(WriteMode.OVERWRITE);
        dao.write(rec);
        assertEquals(testFileSize2, file.length());

        dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest3.xml");
        dao.setWriteMode(WriteMode.OVERWRITE);
        dao.write(rec);
        assertEquals(testFileSize3, file.length());

        dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest4.xml");
        dao.setWriteMode(WriteMode.OVERWRITE);
        dao.write(rec);
        assertEquals(testFileSize2, file.length());
    }

    /**
     * read()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testReadNormal() throws Exception {
        final int testFileSize = 7622;
        final int testFileSize2 = 7611;
        final int testFileSize3 = 7651;
        File file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        dao.write(rec);
        assertEquals(testFileSize, file.length());

        dao.read();
    }

    /**
     * recordErrorMode()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testRecordErrorMode() throws Exception {
        for (File f : findDir("D:\\tmp\\", "Sample.txt.*\\.zip")) {
            f.delete();
        }

        final long fileSize = 3062L;
        final long fileSize2 = 10684L;
        final long fileSize3 = 13746L;
        final long fileSize4 = 9142L;
        final int appendErrorIndex = 5;
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("FixedFileAccessObjectTest.xml");
        File file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }

        Row row = new Row();
        row.put("password", "xxxx");
        rec.add(appendErrorIndex, row);
        try {
            dao.write(rec);
        } catch (NeoUserException e) {
            System.out.println(e.getMessages());
            assertEquals("NEO-A0008:�t�@�C�������ݒ��ɃG���[���������܂����B", e.getMessage());
            assertEquals("NEO-A0005:5���R�[�h�ڂŃG���[���������܂����B�����𒆒f���܂����B",
                    e.children().get(0).getMessage());
            assertEquals(fileSize, file.length());
        }

        try {
            row = new Row();
            row.put("userId", "abcdefghijklmnopqrstuvwxyz");
            row.put("userName", "xxxx");
            row.put("password", "xxxx");
            row.put("tel", "xxxx");
            row.put("remarks", "xxxx");
            rec.add(appendErrorIndex, row);

            dao.setWriteMode(WriteMode.APPEND);
            dao.setRecordErrorMode(RecordErrorMode.SKIP);
            dao.write(rec);
        } catch (NeoUserException e) {
            System.out.println(e.getMessages());
            assertEquals("NEO-A0008:�t�@�C�������ݒ��ɃG���[���������܂����B", e.getMessage());
            assertEquals("NEO-A0005:5���R�[�h�ڂŃG���[���������܂����B�������X�L�b�v���܂����B",
                    e.children().get(0).getMessage());
            assertEquals("NEO-A0005:6���R�[�h�ڂŃG���[���������܂����B�������X�L�b�v���܂����B",
                    e.children().get(1).getMessage());
            assertEquals(fileSize2, file.length());
        }

        try {
            dao.setRecordErrorMode(RecordErrorMode.ROLLBACK);
            dao.write(rec);
        } catch (NeoUserException e) {
            System.out.println(e.getMessages());
            assertEquals("NEO-A0008:�t�@�C�������ݒ��ɃG���[���������܂����B", e.getMessage());
            assertEquals("NEO-A0005:5���R�[�h�ڂŃG���[���������܂����B�����𒆒f���܂����B",
                    e.children().get(0).getMessage());
            assertEquals(fileSize3, file.length());
        }

        try {
            dao.setBackupMode(true);
            dao.setRecordErrorMode(RecordErrorMode.ROLLBACK);
            dao.write(rec);
        } catch (NeoUserException e) {
            System.out.println(e.getMessages());
            assertEquals("NEO-A0008:�t�@�C�������ݒ��ɃG���[���������܂����B", e.getMessage());
            assertEquals("NEO-A0005:5���R�[�h�ڂŃG���[���������܂����B���������[���o�b�N���܂����B",
                    e.children().get(0).getMessage());
            assertEquals(fileSize3, file.length());
        }

        try {
            dao.setWriteMode(WriteMode.OVERWRITE);
            dao.setRecordErrorMode(RecordErrorMode.IGNORE);
            dao.write(rec);
        } catch (NeoUserException e) {
            System.out.println(e.getMessages());
            assertEquals("NEO-A0008:�t�@�C�������ݒ��ɃG���[���������܂����B", e.getMessage());
            assertEquals("NEO-A0005:5���R�[�h�ڂŃG���[���������܂����B�������������s���܂����B",
                    e.children().get(0).getMessage());
            assertEquals(fileSize4, file.length());
        }
    }

    /**
     * �w�肳�ꂽ�t�@�C���p�^�[���Ƀ}�b�`����t�@�C����Ԃ�.
     * @param path �����Ώۃf�B���N�g��
     * @param matches �����p�^�[��
     * @return �����p�^�[���ƈ�v�����t�@�C��
     */
    private File[] findDir(final String path, final String matches) {
        File dir = new File(path);
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                if (name.matches(matches)) {
                    return true;
                    }
                return false;
            }
        });
        return files;
    }
}
