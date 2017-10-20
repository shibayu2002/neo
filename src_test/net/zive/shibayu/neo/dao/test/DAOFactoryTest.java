package net.zive.shibayu.neo.dao.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.dao.DAOFactory;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.RecordSet;
import net.zive.shibayu.neo.lang.Row;

/**
 * DAOFactoryのテストクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class DAOFactoryTest extends TestCase {
    /**
     * テスト用の値.
     */
    private static final Double TEST_DOUBLE_VALUE = 1.1;

    /**
     * テスト用の値.
     */
    private static final int TEST_REC_COUNT = 10;

    /**
     * 共通テストデータ.
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
            row.put("column1", null);
            row.put("column2", "aaa" + i);
            row.put("column3", new Integer(i));
            row.put("column4", TEST_DOUBLE_VALUE);
            rec.add(row);
        }
    }

    /**
     * create()のテスト(正常系).
     * @throws Exception システムエラー
     */
    public void testCreateNormal() throws Exception {
        DAOFactory factory = DAOFactory.getInstance();
        factory.create("FixedFileAccessObjectTest.xml");
        try {
            factory.create("none.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0003:DAO定義ファイル(XML)の読み込みに失敗しました。ファイル=[none.xml]。",
                e.getMessage());
        }
        try {
            factory.create("FixedFileAccessObjectErrorTest1.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0002:DAO定義ファイル(XML)が不正です。/define/@typeが定義されていません。",
                e.getMessage());
        }
        try {
            factory.create("FixedFileAccessObjectErrorTest2.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[cvc-complex-type.4: 要素'common'に属性'file'が含まれている必要があります。]。",
            e.getMessage());
        }
        try {
            factory.create("FixedFileAccessObjectErrorTest3.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[ファイルの読み込みに失敗しました。ファイル=[neo/xsd/NoneType.xsd]]。",
            e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系1).
     * @throws Exception システムエラー
     */
    public void testCreateError1() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest1.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0002:DAO定義ファイル(XML)が不正です。/define/@typeが定義されていません。",
                e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系2).
     * @throws Exception システムエラー
     */
    public void testCreateError2() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest2.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[cvc-complex-type.4: 要素'common'に属性'file'が含まれている必要があります。]。",
            e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系3).
     * @throws Exception システムエラー
     */
    public void testCreateError3() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest3.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[ファイルの読み込みに失敗しました。ファイル=[neo/xsd/NoneType.xsd]]。",
            e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系4).
     * @throws Exception システムエラー
     */
    public void testCreateError4() throws Exception {
        try {
            DAOFactory.getInstance().create("none.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0003:DAO定義ファイル(XML)の読み込みに失敗しました。ファイル=[none.xml]。",
                e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系5).
     * @throws Exception システムエラー
     */
    public void testCreateError5() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest5.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[cvc-complex-type.2.4.a: "
            + "要素'header'で始まる無効なコンテンツが見つかりました。"
            + "'{common}'のいずれかが必要です。]。",
            e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系6).
     * @throws Exception システムエラー
     */
    public void testCreateError6() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest6.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[cvc-complex-type.2.4.a: 要素'header'で始まる無効なコンテンツが見つかりました。"
            + "'{body}'のいずれかが必要です。]。",
            e.getMessage());
        }
    }

    /**
     * create()のテスト(異常系7).
     * @throws Exception システムエラー
     */
    public void testCreateError7() throws Exception {
        try {
            DAOFactory.getInstance()
                .create("FixedFileAccessObjectErrorTest7.xml");
            fail();
        } catch (NeoFrameworkException e) {
            assertEquals(
            "NEO-E0004:DAO定義ファイル(XML)の妥当性検証でエラーが発生しました。"
            + "詳細=[cvc-enumeration-valid: 値'CR'は、列挙'[CRLF, LF]'に対して"
            + "ファセットが有効ではありません。列挙からの値である必要があります。]。",
            e.getMessage());
        }
    }
}
