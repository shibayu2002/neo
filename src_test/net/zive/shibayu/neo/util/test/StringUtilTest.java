package net.zive.shibayu.neo.util.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import net.zive.shibayu.neo.util.StringUtil;

/**
 * Row�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class StringUtilTest extends TestCase {
    @Override
    protected final void setUp() throws Exception {
    }

    /**
     * lenB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testLenB() throws UnsupportedEncodingException {
        final int num = 3;
        final int num2 = 6;
        final int num3 = 7;
        assertEquals(num, StringUtil.lenB("abc"));
        assertEquals(num2, StringUtil.lenB("������"));
        assertEquals(num3, StringUtil.lenB("a������"));
    }

    /**
     * lpadB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testLpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("       abc", StringUtil.lpadB("abc", pad));
        assertEquals("    ������", StringUtil.lpadB("������", pad));
    }

    /**
     * rpadB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testRpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("abc       ", StringUtil.rpadB("abc", pad));
        assertEquals("������    ", StringUtil.rpadB("������", pad));
    }

    /**
     * nlpadB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testNlpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("0000000abc", StringUtil.nlpadB("abc", pad));
        assertEquals("0000������", StringUtil.nlpadB("������", pad));
    }

    /**
     * nrpadB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testNrpadB() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("abc0000000", StringUtil.nrpadB("abc", pad));
        assertEquals("������0000", StringUtil.nrpadB("������", pad));
    }

    /**
     * ltrimB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testLtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.ltrimB("       abc"));
        assertEquals("x   abc", StringUtil.ltrimB("   x   abc"));
        assertEquals("������", StringUtil.ltrimB("    ������"));
        assertEquals("������", StringUtil.ltrimB("������"));
    }

    /**
     * lntrimB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testNtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.nltrimB("0000000abc"));
        assertEquals("������", StringUtil.nltrimB("0000������"));
    }

    /**
     * rtrimB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testRtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.rtrimB("abc       "));
        assertEquals("������", StringUtil.rtrimB("������    "));
    }

    /**
     * nrtrimB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testNrtrimB() throws UnsupportedEncodingException {
        assertEquals("abc", StringUtil.nrtrimB("abc0000000"));
        assertEquals("123.0012", StringUtil.nrtrimB("123.0012000"));
        assertEquals("������", StringUtil.nrtrimB("������0000"));
        assertEquals("������", StringUtil.nrtrimB("������"));
    }

    /**
     * midB()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
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
            assertEquals("start��0�ȏ�̒l���w�肵�ĉ������B", e.getMessage());
        }

        try {
            assertEquals("abc", StringUtil.midB("abc", 0, -1));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("end��start�ȏ�̒l���w�肵�ĉ������B", e.getMessage());
        }

        try {
            assertEquals("abc", StringUtil.midB("abc", three, four));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("start�̒l��value�̃T�C�Y�𒴂��Ă��܂��B", e.getMessage());
        }

        try {
            assertEquals("abc", StringUtil.midB("abc", 2, three));
            fail();
        } catch (IndexOutOfBoundsException e) {
            assertEquals("end�̒l��value�̃T�C�Y�𒴂��Ă��܂��B", e.getMessage());
        }

        assertEquals("a", StringUtil.midB("abc", 0, 0));
        assertEquals("abc", StringUtil.midB("abc", 0, 2));
        assertEquals("c", StringUtil.midB("abc", 2, 2));
        assertEquals("����", StringUtil.midB("����������", 2, five));
        assertEquals("abc��������", StringUtil.midB("abc����������", 0, ten));
    }
}
