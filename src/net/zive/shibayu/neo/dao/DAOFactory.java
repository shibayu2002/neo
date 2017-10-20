package net.zive.shibayu.neo.dao;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import net.zive.shibayu.neo.lang.NeoFrameworkException;
import net.zive.shibayu.neo.lang.NeoSystemException;
import net.zive.shibayu.neo.util.XMLReader;

/**
 * DAO(データアクセスオブジェクト)生成クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class DAOFactory {
    /**
     * シングルトンインスタンス.
     */
    private static DAOFactory instance = new DAOFactory();

    /**
     * データアクセスオブジェクト生成クラスのインスタンスを返す.
     * @return データアクセスオブジェクト生成クラスのインスタンス
     */
    public static DAOFactory getInstance() {
        return instance;
    }

    /**
     * コンストラクタ.<br>
     * 外部アクセス禁止
     */
    private DAOFactory() {
    }

    /**
     * DAO定義ファイルを読み込みDAOを生成する.
     * <ol>
     * <li>DAO定義ファイルを読込む。
     * <li>DAO定義種別(type)を取得する。
     * <li>DAO定義ファイル(xml)妥当性検証<br>
     * スキーマ定義ファイル(neo/xsd/(DAO定義種別).xsd)を用いて妥当性の検証を行う。
     * <li>DAO定義種別(type)に該当するDataAccessObjectを生成する。
     * <li>DAO定義マップにDataAccessObjectを格納する。
     * <li>DataAccessObjectを返却する。
     * </ol>
     * @param xml DAO定義ファイル
     * @param logger DAOに設定するロガー
     * @return DAO
     * @throws NeoFrameworkException エラー
     */
    public DataAccessObject create(final String xml, final Logger logger)
            throws NeoFrameworkException {
        // TODO シングルトン化の検討要。インスタンスは別にしたいがXML読込は一回限りにしたい
        XMLReader reader = readDaoXML(xml);
        String type = getType(reader);
        schemaValidate(reader, type);
        return createDataAccessObject(reader, type, logger);
    }

    /**
     * DAO定義ファイルを読み込みDAOを生成する.
     * <p>詳細はcreate(final String xml, final Logger logger)参照</p>
     * @param xml DAO定義ファイル
     * @return DAO
     * @throws NeoFrameworkException エラー
     */
    public DataAccessObject create(final String xml)
            throws NeoFrameworkException {
        return create(xml, Logger.getGlobal());
    }

    /**
     * DAO定義ファイルを読込むためのリーダーを取得する.
     * @param xml DAO定義ファイル
     * @return DAO定義ファイルリーダー
     * @throws NeoFrameworkException DAO定義ファイル読込エラー
     */
    private XMLReader readDaoXML(final String xml)
                throws NeoFrameworkException {
        try {
            return XMLReader.createInstanceAsResouce(xml);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0003", xml);
        }
    }

    /**
     * DAO定義種別(type)を取得する.
     * @param reader XMLリーダー
     * @return DAO定義種別(type)
     * @throws NeoFrameworkException DAO定義種別取得エラー
     */
    private String getType(final XMLReader reader)
                throws NeoFrameworkException {
        try {
            String type = reader.readString("/define/@type");
            if ("".equals(type)) {
                throw new NeoSystemException("NEO-E0002",
                        "/define/@typeが定義されていません");
            }
            return type;
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0002",
                    "/define/@typeが定義されていません");
        }
    }

    /**
     * DAO定義ファイル(xml)妥当性検証.
     * @param reader XMLリーダー
     * @param type DAO定義種別(type)
     * @throws NeoFrameworkException 妥当性検証エラー
     */
    private void schemaValidate(final XMLReader reader,
                final String type) throws NeoFrameworkException {
        try {
            reader.validate("neo/xsd/" + type + ".xsd");
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0004",
                    e.getMessage());
        }
    }

    /**
     * DAO定義種別(type)に該当するDataAccessObjectを生成する.
     * @param reader XMLリーダー
     * @param type DAO定義種別(type)
     * @param logger ロガー
     * @return DataAccessObject
     * @throws NeoFrameworkException DataAccessObject生成エラー
     */
    private DataAccessObject createDataAccessObject(
                final XMLReader reader, final String type,
                final Logger logger) throws NeoFrameworkException {
        String path = this.getClass().getPackage().getName() + ".impl";

        try {
            Class<?> clazz = Class.forName(path + "." + type + "AccessObject");
            Class<?>[] types = {XMLReader.class, Logger.class};
            Constructor<?> constructor = clazz.getConstructor(types);

            Object[] args = {reader, logger};
            return (DataAccessObject) constructor.newInstance(args);
        } catch (Exception e) {
            throw new NeoSystemException("NEO-E0005",
                    e.getMessage());
        }
    }
}
