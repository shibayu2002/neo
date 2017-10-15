package net.zive.shibayu.neo.dao.test;

import java.io.File;

import junit.framework.TestCase;
import net.zive.shibayu.neo.dao.DAOFactory;
import net.zive.shibayu.neo.dao.FileAccessObject;
import net.zive.shibayu.neo.dao.FileAccessObject.WriteMode;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;

/**
 * FileAccessObject�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class VariableFileAccessObjectTest extends TestCase {
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
            row.put("remarks", "te\"st\"xxx\"" + i);
            rec.add(row);
        }
    }

    /**
     * write()�̃e�X�g.
     * @throws Exception �V�X�e���G���[
     */
    public void testWriteNormal() throws Exception {
        final int testFileSize = 833;
        final int testFileSize2 = 679;
        File file = new File("D:\\tmp\\Sample.txt");
        if (file.exists()) {
            file.delete();
        }
        DAOFactory factory = DAOFactory.getInstance();
        FileAccessObject dao = (FileAccessObject)
                factory.create("VariableFileAccessObjectTest.xml");
        dao.write(rec);
        assertEquals(testFileSize, file.length());

        dao = (FileAccessObject)
                factory.create("VariableFileAccessObjectTest2.xml");
        dao.setWriteMode(WriteMode.OVERWRITE);
        dao.write(rec);
        assertEquals(testFileSize2, file.length());
    }
}
