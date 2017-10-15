package net.zive.shibayu.neo.lang.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;

/**
 * RecordSetのテストクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class RecordSetTest extends TestCase {
    /**
     * テスト用の値.
     */
    private static final Double TEST_DOUBLE_VALUE = 1.1;

    /**
     * 共通テストデータ.
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
     * getString()のテスト.
     */
    public void testToString() {
        assertEquals("[{column1=null, column4=1.1, column3=1, column2=aaa}]",
                rec.toString());
    }
}
