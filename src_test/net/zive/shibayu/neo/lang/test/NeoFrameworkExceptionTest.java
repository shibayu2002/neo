package net.zive.shibayu.neo.lang.test;

import junit.framework.TestCase;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.lang.NeoUserException;

/**
 * Row�̃e�X�g�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class NeoFrameworkExceptionTest extends TestCase {
    /**
     * NeoFrameworkException()�̃e�X�g.
     */
    public void testNeoFrameworkException() {
        try {
            throw new NeoSystemException("NEO-E0001", "xxxx");
        } catch (NeoSystemException e) {
            assertEquals("NEO-E0001", e.getCode());
            assertEquals("�V�X�e���G���[���������܂����Bxxxx", e.getMessageNoneCode());
            assertEquals("NEO-E0001:�V�X�e���G���[���������܂����Bxxxx", e.getMessage());
            assertTrue(e.isSystemError());
            assertFalse(e.isApplicationError());
        }

        try {
            throw new NeoSystemException("NEO-A0001", "xxxx");
        } catch (NeoSystemException e) {
            assertEquals("NEO-A0001", e.getCode());
            assertEquals("�t�@�C���̏������݂Ɏ��s���܂����B[xxxx]", e.getMessageNoneCode());
            assertEquals("NEO-A0001:�t�@�C���̏������݂Ɏ��s���܂����B[xxxx]", e.getMessage());
            assertFalse(e.isSystemError());
            assertTrue(e.isApplicationError());
        }

        try {
            throw new NeoSystemException("NEO-Exxxx");
        } catch (NeoFrameworkException e) {
            assertEquals("NEO-Exxxx", e.getCode());
            assertEquals("���b�Z�[�W���擾�ł��܂���ł����B", e.getMessageNoneCode());
            assertEquals("NEO-Exxxx:���b�Z�[�W���擾�ł��܂���ł����B", e.getMessage());
        }

        try {
            throw new NeoSystemException("NEO-E9999", "test");
        } catch (NeoFrameworkException e) {
            assertEquals("NEO-E9999", e.getCode());
            assertEquals("�e�X�g�p�̃G���[�R�[�h�ł��B����=test", e.getMessageNoneCode());
            assertEquals("NEO-E9999:�e�X�g�p�̃G���[�R�[�h�ł��B����=test", e.getMessage());
        }
    }

    /**
     * children()�̃e�X�g.
     */
    public void testChildren() {
        NeoUserException e = new NeoUserException("NEO-E9999",
                new Exception("test"), "test");
        assertEquals(0, e.children().size());
        e.addChild(new NeoUserException("NEO-A0005", 1, "xxxx"));
        assertEquals(1, e.children().size());
        assertEquals("NEO-A0005:1���R�[�h�ڂŃG���[���������܂����B������xxxx���܂����B",
                e.children().get(0).getMessage());
        e.addChild(new NeoUserException("NEO-A0005", 2, "yyyy"));
        assertEquals(2, e.children().size());
        assertEquals("NEO-A0005:1���R�[�h�ڂŃG���[���������܂����B������xxxx���܂����B",
                e.children().get(0).getMessage());
        assertEquals("NEO-A0005:2���R�[�h�ڂŃG���[���������܂����B������yyyy���܂����B",
                e.children().get(1).getMessage());
    }
}
