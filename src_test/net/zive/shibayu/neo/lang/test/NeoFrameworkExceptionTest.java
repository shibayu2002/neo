package net.zive.shibayu.neo.lang.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.NeoUserException;

/**
 * Rowのテストクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class NeoFrameworkExceptionTest extends TestCase {
    /**
     * NeoFrameworkException()のテスト.
     */
    public void testNeoFrameworkException() {
        try {
            throw new NeoSystemException("NEO-E0001", "xxxx");
        } catch (NeoSystemException e) {
            assertEquals("NEO-E0001", e.getCode());
            assertEquals("システムエラーが発生しました。xxxx", e.getMessageNoneCode());
            assertEquals("NEO-E0001:システムエラーが発生しました。xxxx", e.getMessage());
            assertTrue(e.isSystemError());
            assertFalse(e.isApplicationError());
        }

        try {
            throw new NeoSystemException("NEO-A0001", "xxxx");
        } catch (NeoSystemException e) {
            assertEquals("NEO-A0001", e.getCode());
            assertEquals("ファイルの書き込みに失敗しました。[xxxx]", e.getMessageNoneCode());
            assertEquals("NEO-A0001:ファイルの書き込みに失敗しました。[xxxx]", e.getMessage());
            assertFalse(e.isSystemError());
            assertTrue(e.isApplicationError());
        }

        try {
            throw new NeoSystemException("NEO-Exxxx");
        } catch (NeoFrameworkException e) {
            assertEquals("NEO-Exxxx", e.getCode());
            assertEquals("メッセージが取得できませんでした。", e.getMessageNoneCode());
            assertEquals("NEO-Exxxx:メッセージが取得できませんでした。", e.getMessage());
        }

        try {
            throw new NeoSystemException("NEO-E9999", "test");
        } catch (NeoFrameworkException e) {
            assertEquals("NEO-E9999", e.getCode());
            assertEquals("テスト用のエラーコードです。引数=test", e.getMessageNoneCode());
            assertEquals("NEO-E9999:テスト用のエラーコードです。引数=test", e.getMessage());
        }
    }

    /**
     * children()のテスト.
     */
    public void testChildren() {
        NeoUserException e = new NeoUserException("NEO-E9999",
                new Exception("test"), "test");
        assertEquals(0, e.children().size());
        e.addChild(new NeoUserException("NEO-A0005", 1, "xxxx"));
        assertEquals(1, e.children().size());
        assertEquals("NEO-A0005:1レコード目でエラーが発生しました。処理をxxxxしました。",
                e.children().get(0).getMessage().replaceAll("\r\n.*", ""));
        e.addChild(new NeoUserException("NEO-A0005", 2, "yyyy"));
        assertEquals(2, e.children().size());
        assertEquals("NEO-A0005:1レコード目でエラーが発生しました。処理をxxxxしました。",
                e.children().get(0).getMessage().replaceAll("\r\n.*", ""));
        assertEquals("NEO-A0005:2レコード目でエラーが発生しました。処理をyyyyしました。",
                e.children().get(1).getMessage().replaceAll("\r\n.*", ""));
    }
}
