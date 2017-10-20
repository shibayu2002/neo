package net.zive.shibayu.neo.lang.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;

/**
 * RecordSet�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class RecordSetTest extends TestCase {
    /**
     * �e�X�g�p�̒l.
     */
    private static final Double TEST_DOUBLE_VALUE = 1.1;

    /**
     * ���ʃe�X�g�f�[�^.
     */
    private RecordSet rec = new RecordSet();

    @Override
    protected final void setUp() throws Exception {
        super.setUp();
        Row row = new Row();
        row.put("column1", null);
        row.put("column2", "aaa");
        row.put("column3", new Integer(1));
        row.put("column4", TEST_DOUBLE_VALUE);
        rec.add(row);
    }

    /**
     * getString()�̃e�X�g.
     */
    public void testToString() {
        assertEquals("{[column1=,column4=1.1,column3=1,column2=aaa]}",
                rec.toString());
    }
}
