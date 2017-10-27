package net.zive.shibayu.neo.util.test;

import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;
import net.zive.shibayu.neo.util.CsvSpliter;

/**
 * Rowのテストクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class CsvSpliterTest extends TestCase {
    @Override
    protected final void setUp() throws Exception {
    }

    /**
     * split()のテスト.
     * @throws UnsupportedEncodingException エンコードエラー
     */
    public void testSplit() throws UnsupportedEncodingException {
        CsvSpliter spliter = new CsvSpliter(",", "");
        assertStringList(new String[]{"aaa", "いい", "c\tcc", "ddd"},
                spliter.split("aaa,いい,c\tcc,ddd"));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", " "},
                spliter.split("aaa,いい,ccc,ddd, "));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", ""},
                spliter.split("aaa,いい,ccc,ddd,"));
        assertStringList(new String[]{"", "aaa", "いい", "ccc"},
                spliter.split(",aaa,いい,ccc"));

        spliter = new CsvSpliter("\t", "");
        assertStringList(new String[]{"aaa", "いい", "c,cc", "ddd"},
                spliter.split("aaa\tいい\tc,cc\tddd"));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", " "},
                spliter.split("aaa\tいい\tccc\tddd\t "));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", ""},
                spliter.split("aaa\tいい\tccc\tddd\t"));
        assertStringList(new String[]{"", "aaa", "いい", "ccc"},
                spliter.split("\taaa\tいい\tccc"));

        spliter = new CsvSpliter(",", "\"");
        assertStringList(new String[]{"aaa", "いい", "c\tcc", "ddd"},
                spliter.split("\"aaa\",\"いい\",\"c\tcc\",\"ddd\""));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", " "},
                spliter.split("\"aaa\",\"いい\",\"ccc\",\"ddd\",\" \""));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", ""},
                spliter.split("\"aaa\",\"いい\",\"ccc\",\"ddd\","));
        assertStringList(new String[]{"", "aaa", "いい", "c\"c,\"c"},
                spliter.split(",\"aaa\",\"いい\",\"c\"\"c,\"\"c\""));

        spliter = new CsvSpliter("\t", "\"");
        assertStringList(new String[]{"aaa", "いい", "c,cc", "ddd"},
                spliter.split("\"aaa\"\t\"いい\"\t\"c,cc\"\t\"ddd\""));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", " "},
                spliter.split("\"aaa\"\t\"いい\"\t\"ccc\"\t\"ddd\"\t\" \""));
        assertStringList(new String[]{"aaa", "いい", "ccc", "ddd", ""},
                spliter.split("\"aaa\"\t\"いい\"\t\"ccc\"\t\"ddd\"\t"));
        assertStringList(new String[]{"", "aaa", "いい", "c\"c\t\"c"},
                spliter.split("\t\"aaa\"\t\"いい\"\t\"c\"\"c\t\"\"c\""));
    }

    /**
     * 文字列配列のテスト結果検証.
     * @param expected 予想
     * @param actual 結果
     */
    private void assertStringList(final String[] expected,
                    final String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}
