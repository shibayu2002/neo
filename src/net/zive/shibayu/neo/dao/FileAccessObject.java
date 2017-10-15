package net.zive.shibayu.neo.dao;

import java.io.File;

/**
 * ファイルアクセスオブジェクトインターフェース.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public interface FileAccessObject extends DataAccessObject {
    /**
     * ファイルが存在する場合の処理方法.
     */
    enum WriteMode {
        /** 処理中断(デフォルト). */
        CANCEL,
        /** 上書き. */
        OVERWRITE,
        /** 追記. */
        APPEND
    }

    /**
     * 処理対象ファイルを変更する.
     * @param file 処理対象ファイル
     */
    void changeFile(File file);

    /**
     * 処理対象ファイルを返す.
     * @return 処理対象ファイル
     */
    File getFile();

    /**
     * ファイルが存在する場合の処理方法を設定する.
     * @param mode ファイルが存在する場合の処理方法
     */
    void setWriteMode(WriteMode mode);

    /**
     * ファイル出力前バックアップ有無を設定する.
     * @param mode true:バックアップを作成する。false：バックアップを作成しない。
     */
    void setBackupMode(boolean mode);
}
