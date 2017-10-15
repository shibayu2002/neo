package net.zive.shibayu.neo.lang;

/**
 * XML定義不正Exceptionクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class NeoSystemException extends NeoFrameworkException {
    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ.
     * @param e エラー発生元Exception
     */
    public NeoSystemException(final Exception e) {
        super(e);
    }

    /**
     * コンストラクタ.
     * @param code メッセージコード
     * @param args 可変メッセージ
     */
    public NeoSystemException(final String code, final Object ...args) {
        super(code, args);
    }
}
