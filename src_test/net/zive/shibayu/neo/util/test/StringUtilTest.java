package net.zive.shibayu.neo.util.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import net.zive.shibayu.neo.util.StringUtil;

/**
 * Rowのテストクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class StringUtilTest extends TestCase {
    @Override
    protected final void setUp() throws Exception {
    }

    /**
     * len()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testLen() throws UnsupportedEncodingException {
        final int num = 3;
        final int num2 = 6;
        final int num3 = 7;
        assertEquals(num, StringUtil.len("abc"));
        assertEquals(num2, StringUtil.len("あいう"));
        assertEquals(num3, StringUtil.len("aあいう"));
    }

    /**
     * xpad()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testXpad() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("       abc", StringUtil.lpad("abc", pad));
        assertEquals("    あいう", StringUtil.lpad("あいう", pad));
        assertEquals("abc       ", StringUtil.rpad("abc", pad));
        assertEquals("あいう    ", StringUtil.rpad("あいう", pad));
        assertEquals("0000000abc", StringUtil.nlpad("abc", pad));
        assertEquals("0000あいう", StringUtil.nlpad("あいう", pad));
        assertEquals("abc0000000", StringUtil.nrpad("abc", pad));
        assertEquals("あいう0000", StringUtil.nrpad("あいう", pad));
    }
}
