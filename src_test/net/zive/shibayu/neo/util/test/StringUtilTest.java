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
     * len()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testLen() throws UnsupportedEncodingException {
        final int num = 3;
        final int num2 = 6;
        final int num3 = 7;
        assertEquals(num, StringUtil.len("abc"));
        assertEquals(num2, StringUtil.len("������"));
        assertEquals(num3, StringUtil.len("a������"));
    }

    /**
     * xpad()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testXpad() throws UnsupportedEncodingException {
        final int pad = 10;
        assertEquals("       abc", StringUtil.lpad("abc", pad));
        assertEquals("    ������", StringUtil.lpad("������", pad));
        assertEquals("abc       ", StringUtil.rpad("abc", pad));
        assertEquals("������    ", StringUtil.rpad("������", pad));
        assertEquals("0000000abc", StringUtil.nlpad("abc", pad));
        assertEquals("0000������", StringUtil.nlpad("������", pad));
        assertEquals("abc0000000", StringUtil.nrpad("abc", pad));
        assertEquals("������0000", StringUtil.nrpad("������", pad));
    }
}
