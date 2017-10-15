package net.zive.shibayu.neo.lang;

import java.io.InputStreamReader;
import java.util.Properties;
import java.util.regex.Pattern;

import net.zive.shibayu.neo.Core;

/**
 * XML定義不正Exceptionクラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public abstract class NeoFrameworkException extends Exception {
    /**
     * シリアルバージョン.
     */
    private static final long serialVersionUID = 1L;

    /**
     * メッセージプロパティファイル.
     */
    private static Properties msgProperties = null;

    /**
     * エラーコードを取得する正規表現.
     */
    private static final Pattern GET_CODE_PATTERN = Pattern.compile(":.*");

    /**
     * エラーコードを除いたエラーメッセージを取得する正規表現.
     */
    private static final Pattern GET_MESSAGE_PATTERN = Pattern.compile("^.*?:");

    /**
     * システムエラーパターン正規表現.
     */
    private static final Pattern SYSTEM_ERROR_PATTERN
                            = Pattern.compile("^NEO-E.*");

    /**
     * アプリケーションエラーパターン正規表現.
     */
    private static final Pattern APPLICATION_ERRO_PATTERN
                            = Pattern.compile("^NEO-A.*");

    /**
     * Exception発生元のException.
     */
    private Exception sourceException = null;

    /**
     * コンストラクタ.
     * @param e エラー発生元Exception
     */
    protected NeoFrameworkException(final Exception e) {
        this(convToMsg("NEO-E0001", e.getMessage()), e);
    }

    /**
     * コンストラクタ.
     * @param code メッセージコード
     * @param args 可変メッセージ
     */
    protected NeoFrameworkException(final String code,
            final Object ...args) {
        super(convToMsg(code, args));
    }

    /**
     * エラーコードを返す.
     * @return エラーコード
     */
    public String getCode() {
        return GET_CODE_PATTERN.matcher(getMessage()).replaceAll("");
    }

    /**
     * エラーコードを除いたエラーメッセージを返す.
     * @return エラーコードを除いたエラーメッセージ
     */
    public String getMessageNoneCode() {
        return GET_MESSAGE_PATTERN.matcher(getMessage()).replaceAll("");
    }

    /**
     * エラー発生元のExceptionを返す.
     * @return エラー発生元のException
     */
    public Exception getSourceException() {
        return sourceException;
    }

    /**
     * システムエラー判定.
     * @return true:システムエラー, false:システムエラー以外
     */
    public boolean isSystemError() {
        return SYSTEM_ERROR_PATTERN.matcher(getMessage()).matches();
    }

    /**
     * アプリケーションエラー判定.
     * @return true:アプリケーションエラー, false:アプリケーションエラー以外
     */
    public boolean isApplicationError() {
        return APPLICATION_ERRO_PATTERN.matcher(getMessage()).matches();
    }

    /**
     * エラーコードをメッセージに変換する.
     * @param code エラーコード
     * @return エラーコード：エラーメッセージ
     * @param args 可変メッセージ
     */
    private static String convToMsg(final String code, final Object ...args) {
        if (msgProperties == null) {
            msgProperties = new Properties();
            try {
                msgProperties.load(new InputStreamReader(
                        ClassLoader.getSystemResourceAsStream(
                                "neo/msg/message.properties"), Core.ENCODE));
            } catch (Exception e) {
                return code + ":メッセージが取得できませんでした。";
            }
        }
        if (msgProperties.containsKey(code)) {
            String msg = msgProperties.getProperty(code);
            return code + ":" + String.format(msg, args);
        }
        return code + ":メッセージが取得できませんでした。";
    }
}
