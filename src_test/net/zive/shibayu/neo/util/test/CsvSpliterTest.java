package net.zive.shibayu.neo.util.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import net.zive.shibayu.neo.util.CsvSpliter;

/**
 * Row�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class CsvSpliterTest extends TestCase {
    @Override
    protected final void setUp() throws Exception {
    }

    /**
     * split()�̃e�X�g.
     * @throws UnsupportedEncodingException �G���R�[�h�G���[
     */
    public void testSplit() throws UnsupportedEncodingException {
        CsvSpliter spliter = new CsvSpliter(",", "");
        assertStringList(new String[]{"aaa", "����", "c\tcc", "ddd"},
                spliter.split("aaa,����,c\tcc,ddd"));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", " "},
                spliter.split("aaa,����,ccc,ddd, "));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", ""},
                spliter.split("aaa,����,ccc,ddd,"));
        assertStringList(new String[]{"", "aaa", "����", "ccc"},
                spliter.split(",aaa,����,ccc"));

        spliter = new CsvSpliter("\t", "");
        assertStringList(new String[]{"aaa", "����", "c,cc", "ddd"},
                spliter.split("aaa\t����\tc,cc\tddd"));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", " "},
                spliter.split("aaa\t����\tccc\tddd\t "));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", ""},
                spliter.split("aaa\t����\tccc\tddd\t"));
        assertStringList(new String[]{"", "aaa", "����", "ccc"},
                spliter.split("\taaa\t����\tccc"));

        spliter = new CsvSpliter(",", "\"");
        assertStringList(new String[]{"aaa", "����", "c\tcc", "ddd"},
                spliter.split("\"aaa\",\"����\",\"c\tcc\",\"ddd\""));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", " "},
                spliter.split("\"aaa\",\"����\",\"ccc\",\"ddd\",\" \""));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", ""},
                spliter.split("\"aaa\",\"����\",\"ccc\",\"ddd\","));
        assertStringList(new String[]{"", "aaa", "����", "c\"c,\"c"},
                spliter.split(",\"aaa\",\"����\",\"c\"\"c,\"\"c\""));

        spliter = new CsvSpliter("\t", "\"");
        assertStringList(new String[]{"aaa", "����", "c,cc", "ddd"},
                spliter.split("\"aaa\"\t\"����\"\t\"c,cc\"\t\"ddd\""));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", " "},
                spliter.split("\"aaa\"\t\"����\"\t\"ccc\"\t\"ddd\"\t\" \""));
        assertStringList(new String[]{"aaa", "����", "ccc", "ddd", ""},
                spliter.split("\"aaa\"\t\"����\"\t\"ccc\"\t\"ddd\"\t"));
        assertStringList(new String[]{"", "aaa", "����", "c\"c\t\"c"},
                spliter.split("\t\"aaa\"\t\"����\"\t\"c\"\"c\t\"\"c\""));
    }

    /**
     * ������z��̃e�X�g���ʌ���.
     * @param expected �\�z
     * @param actual ����
     */
    private void assertStringList(final String[] expected,
                    final String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
