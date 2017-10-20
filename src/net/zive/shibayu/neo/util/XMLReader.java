package net.zive.shibayu.neo.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.zive.shibayu.neo.Core;

/**
 * XML取得クラス.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class XMLReader {

    /**
     * XMLファイルパス.
     */
    private String path = null;

    /**
     * XMLドキュメント.
     */
    private Document doc = null;

    /**
     * XPath.
     */
    private XPath xpath = null;

    /**
     * リソースファイル（クラスパスの通っているフォルダのファイル）からXMLを読み込みXMLReaderを生成する.
     * @param path XMLファイルのパス
     * @return XMLReader
     * @throws ParserConfigurationException XML解析エラー
     * @throws IOException XMLファイル読込エラー
     * @throws SAXException  XML解析エラー
     */
    public static XMLReader createInstanceAsResouce(final String path)
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(path);
        if (is == null) {
            throw new IOException("ファイルの読み込みに失敗しました。ファイル=[" + path + "]");
        }
        XMLReader reader = new XMLReader(is);
        reader.path = path;
        return reader;
    }

    /**
     * コンストラクタ.
     * @param is InputStream
     * @throws ParserConfigurationException XML解析エラー
     * @throws IOException XMLファイル読込エラー
     * @throws SAXException  XML解析エラー
     */
    private XMLReader(final InputStream is)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().
                                    newDocumentBuilder();
        doc = builder.parse(is);
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
    }

    /**
     * 文字列取得.
     * @param location XPath
     * @return 文字列
     * @throws XPathExpressionException XPathエラー
     */
    public String readString(final String location)
            throws XPathExpressionException {
        return (String) xpath.evaluate(
                location, doc, XPathConstants.STRING);
    }

    /**
     * 単一ノード取得.
     * @param location XPath
     * @return 単一ノード
     * @throws XPathExpressionException XPathエラー
     */
    public Node readNode(final String location)
            throws XPathExpressionException {
        return (Node) xpath.evaluate(
                location, doc, XPathConstants.NODE);
    }

    /**
     * 複数ノード取得.
     * @param location XPath
     * @return NodeList
     * @throws XPathExpressionException XPathエラー
     */
    public NodeList readNodeSet(final String location)
            throws XPathExpressionException {
        return (NodeList) xpath.evaluate(
                location, doc, XPathConstants.NODESET);
    }

    /**
     * XMLの妥当性検証を行う.
     * @param schemaPath XMLスキーマ
     * @throws IOException 入出力エラー
     * @throws SAXException XML妥当性検証エラー
     */
    public void validate(final String schemaPath)
            throws SAXException, IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(schemaPath);
        if (is == null) {
            throw new IOException("ファイルの読み込みに失敗しました。ファイル=[" + schemaPath + "]");
        }

        SchemaFactory factory = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(is));
        Validator validator = schema.newValidator();
        String xmlStr = getXML();
        validator.validate(new StreamSource(
                new StringReader(xmlStr)));
    }

    /**
     * XML全行を返す.
     * @return XML全行
     * @throws IOException XML読み込みエラー
     */
    public String getXML() throws IOException {
        StringBuilder builder = new StringBuilder();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    ClassLoader.getSystemResourceAsStream(path), Core.ENCODE));
            String string = null;
            while ((string = reader.readLine()) != null) {
                builder.append(string + System.getProperty("line.separator"));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return builder.toString();
    }
}
