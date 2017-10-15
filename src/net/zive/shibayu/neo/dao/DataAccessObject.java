package net.zive.shibayu.neo.dao;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.RecordSet;

/**
 * データアクセスオブジェクトインターフェース.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public interface DataAccessObject {
    /**
     * レコードチェックエラー時の処理方法.<br>
     */
    enum RecordErrorMode {
        /** 処理中断(デフォルト). */
        CANCEL,
        /** スキップ. */
        SKIP,
        /** 元に戻す. */
        ROLLBACK,
        /** 無視. */
        IGNORE
    }

    /**
     * データ入力を行う.
     * @return RecordSet
     * @throws NeoFrameworkException エラー
     */
    RecordSet read() throws NeoFrameworkException;

    /**
     * データ出力を行う.
     * @param rec レコードセット
     * @throws NeoFrameworkException エラー
     */
    void write(RecordSet rec) throws NeoFrameworkException;

    /**
     * データ読込み前レコード単位Hock処理.
     * @param f Hock関数
     */
    void beforeReadRowFunc(HockFunction f);

    /**
     * データ書込み前レコード単位Hock処理.
     * @param f Hock関数
     */
    void beforeWriteRowFunc(HockFunction f);

    /**
     * レコードチェックエラー時の処理方法を設定する.
     * @param mode レコードチェックエラー時の処理方法
     */
    void setRecordErrorMode(RecordErrorMode mode);
}
