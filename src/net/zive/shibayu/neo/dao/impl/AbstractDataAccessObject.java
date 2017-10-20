package net.zive.shibayu.neo.dao.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.zive.shibayu.neo.dao.DataAccessObject;
import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * データアクセスオブジェクト抽象クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public abstract class AbstractDataAccessObject
                        implements DataAccessObject {
    /**
     * ロガー.
     */
    private Logger myLogger = null;

    /**
     * root定義マップ (キー：属性ID、値：属性値).
     */
    private Map<String, String> rootAtters = null;

    /**
     * 共通定義マップ (キー：属性ID、値：属性値).
     */
    private Map<String, String> commonAtters = null;

    /**
     * コンストラクタ.
     * <ol>
     * <li>ロガーを設定する。<br>
     * パラメタで指定されたロガーをDataAccessObjectのロガーに設定する。<br>
     * パラメタで指定されたロガーがnullの場合はグローバルロガーを設定する。
     * <li>DAO定義ファイルのdefine定義を読み込む。
     * <li>DAO定義ファイルのcommon定義を読み込む。
     * </ol>
     * @param reader DAO定義ファイル読込用のXMLReader
     * @param logger ロガー
     * @throws NeoFrameworkException システムエラー
     */
    public AbstractDataAccessObject(final XMLReader reader,
            final Logger logger) throws NeoFrameworkException {
        if (logger == null) {
            this.myLogger = Logger.getGlobal();
        } else {
            this.myLogger = logger;
        }
        try {
            rootAtters = createNodeDef(
                    reader.readNode("/define").getAttributes());
            commonAtters = createNodeDef(
                    reader.readNode("//common").getAttributes());
            config("load xml. define:" + rootAtters);
            config("common:" + commonAtters);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0001", e);
        }
    }

    /**
     * 共通定義のゲッター.
     * @return 共通定義Map
     */
    protected final Map<String, String> getCommonAtters() {
        return commonAtters;
    }

    /**
     * 項目定義リストを生成する.
     * @param entries 項目定義
     * @return 項目定義リスト (キー：属性ID、値：属性値)
     */
    protected List<Map<String, String>> createNodeDefList(
                final NodeList entries) {
        List<Map<String, String>> columnList =
                new LinkedList<Map<String, String>>();
        for (int i = 0; i < entries.getLength(); i++) {
            Map<String, String> defMap = createNodeDef(
                    entries.item(i).getAttributes());
            columnList.add(defMap);
        }
        return columnList;
    }

    /**
     * 項目定義を生成する.
     * @param attrs 項目定義
     * @return 項目定義 (キー：属性ID、値：属性値)
     */
    protected Map<String, String> createNodeDef(final NamedNodeMap attrs) {
        Map<String, String> defMap = new HashMap<>();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            defMap.put(attr.getNodeName(), attr.getNodeValue());
        }
        return defMap;
    }

    /**
     * configログ出力.
     * @param msg メッセージ
     */
    protected void config(final String msg) {
        myLogger.config("[NeoFramework] " + msg);
    }

    /**
     * infoログ出力.
     * @param msg メッセージ
     */
    protected void info(final String msg) {
        myLogger.info("[NeoFramework] " + msg);
    }
}
