package net.zive.shibayu.neo.dao;

import net.zive.shibayu.neo.lang.Row;

/**
 * データ入出力前後のHock関数インターフェース.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
@FunctionalInterface
public interface HockFunction {
    /**
     * Hock関数.
     * @param row 処理対象レコード
     * @return 後続処理続行：true, 後続処理スキップ：false
     */
    boolean hock(Row row);
}
