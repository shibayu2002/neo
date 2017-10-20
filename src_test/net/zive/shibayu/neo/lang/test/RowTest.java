package net.zive.shibayu.neo.lang.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.lang.Row;
import net.zive.shibayu.neo.util.StringUtil;

/**
 * Row�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class RowTest extends TestCase {
    /**
     * �e�X�g�p�̒l.
     */
    private static final Double TEST_DOUBLE_VALUE = 1.1;

    /**
     * ���ʃe�X�g�f�[�^.
     */
    private Row row = new Row();

    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        row.put("column1", null);
        row.put("column2", "aaa");
        row.put("column3", new Integer(1));
        row.put("column4", TEST_DOUBLE_VALUE);
    }

    /**
     * getString()�̃e�X�g.
     */
    public void testGetString() {
        assertEquals(StringUtil.BLANK, row.getString("non column"));
        assertEquals(StringUtil.BLANK, row.getString("column1"));
        assertEquals("aaa", row.getString("column2"));
        assertEquals("1", row.getString("column3"));
        assertEquals(TEST_DOUBLE_VALUE.toString(), row.getString("column4"));
    }

    /**
     * getInt()�̃e�X�g.
     */
    public void testGetInt() {
        assertEquals(0, row.getInt("non column"));
        try {
            row.getInt("column1");
            row.getInt("column2");
            fail();
        } catch (NumberFormatException e) {
        }
        assertEquals(1, row.getInt("column3"));
        try {
            row.getInt("column4");
            fail();
        } catch (NumberFormatException e) {
        }
    }
}
