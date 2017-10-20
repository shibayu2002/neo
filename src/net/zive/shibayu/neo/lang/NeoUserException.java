package net.zive.shibayu.neo.lang;

import java.util.LinkedList;
import java.util.List;

/**
 * XML定義不正Exceptionクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public class NeoUserException extends NeoFrameworkException {
    /**
     * 子Exception.
     */
    private List<NeoUserException> children
                = new LinkedList<NeoUserException>();

    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 1L;

    /**
     * コンストラクタ.
     * @param e エラー発生元Exception
     */
    public NeoUserException(final Exception e) {
        super(e);
    }

    /**
     * コンストラクタ.
     * @param code メッセージコード
     * @param args 可変メッセージ
     */
    public NeoUserException(final String code, final Object ...args) {
        super(code, args);
    }

    /**
     * 子Exceiptionを追加する.
     * @param e 子Exceiption
     */
    public void addChild(final NeoUserException e) {
        children.add(e);
    }

    /**
     * 子Exceiptionリストを返す.
     * @return 子Exceiptionリスト
     */
    public List<NeoUserException> children() {
        return children;
    }

    @Override
    public final String getMessage() {
        return this.getMessages("");
    }

    /**
     * 子エラーを含めたエラーメッセージの一覧を返す.
     * @param index 文字列の前に付与するインデックス
     * @return エラーメッセージ一覧文字列
     */
    private String getMessages(final String index) {
        StringBuilder stb = new StringBuilder();
        stb.append(index + super.getMessage()).append("\r\n");
        for (NeoUserException e : children) {
            stb.append(e.getMessages(index + "\t"));
        }
        return stb.toString();
    }
}
