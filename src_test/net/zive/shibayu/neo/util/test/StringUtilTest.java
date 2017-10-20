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
     * lenB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testLenB() throws UnsupportedEncodingException {
        final int num = 3;
        final int num2 = 6;
        final int num3 = 7;
        assertEquals(num, StringUtil.lenB("abc"));
        assertEquals(num2, StringUtil.lenB("あいう"));
        assertEquals(num3, StringUtil.lenB("aあいう"));
    }

    /**
     * lpadB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testLpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("       abc", StringUtil.lpadB("abc", pad));
        assertEquals("    あいう", StringUtil.lpadB("あいう", pad));
    }

    /**
     * rpadB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testRpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("abc       ", StringUtil.rpadB("abc", pad));
        assertEquals("あいう    ", StringUtil.rpadB("あいう", pad));
    }

    /**
     * nlpadB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testNlpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("0000000abc", StringUtil.nlpadB("abc", pad));
        assertEquals("0000あいう", StringUtil.nlpadB("あいう", pad));
    }

    /**
     * nrpadB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testNrpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("abc0000000", StringUtil.nrpadB("abc", pad));
        assertEquals("あいう0000", StringUtil.nrpadB("あいう", pad));
    }

    /**
     * ltrimB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testLtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.ltrimB("       abc"));
        assertEquals("x   abc", StringUtil.ltrimB("   x   abc"));
        assertEquals("あいう", StringUtil.ltrimB("    あいう"));
        assertEquals("あいう", StringUtil.ltrimB("あいう"));
    }

    /**
     * lntrimB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testNtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.nltrimB("0000000abc"));
        assertEquals("あいう", StringUtil.nltrimB("0000あいう"));
    }

    /**
     * rtrimB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testRtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.rtrimB("abc       "));
        assertEquals("あいう", StringUtil.rtrimB("あいう    "));
    }

    /**
     * nrtrimB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testNrtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.nrtrimB("abc0000000"));
        assertEquals("123.0012", StringUtil.nrtrimB("123.0012000"));
        assertEquals("あいう", StringUtil.nrtrimB("あいう0000"));
        assertEquals("あいう", StringUtil.nrtrimB("あいう"));
    }

    /**
     * midB()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testMidB() throws UnsupportedEncodingException {
        final int three = 3;
        final int four = 4;
        final int five = 5;
        final int ten = 10;

        try {
            assertEquals("abc", StringUtil.midB("abc", -1, 1));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("startは0以上の値を指定して下さい。", e.getMessage());
        }

        try {
            assertEquals("abc", StringUtil.midB("abc", 0, -1));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("endはstart以上の値を指定して下さい。", e.getMessage());
        }

        try {
            assertEquals("abc", StringUtil.midB("abc", three, four));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("startの値がvalueのサイズを超えています。", e.getMessage());
        }

        try {
            assertEquals("abc", StringUtil.midB("abc", 2, three));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("endの値がvalueのサイズを超えています。", e.getMessage());
        }

        assertEquals("a", StringUtil.midB("abc", 0, 0));
        assertEquals("abc", StringUtil.midB("abc", 0, 2));
        assertEquals("c", StringUtil.midB("abc", 2, 2));
        assertEquals("いう", StringUtil.midB("あいうえお", 2, five));
        assertEquals("abcあいうえ", StringUtil.midB("abcあいうえお", 0, ten));
    }
}
