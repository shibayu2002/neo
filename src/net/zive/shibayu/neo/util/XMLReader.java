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
 * XML�擾�N���X.
 * <br>
 * @author Yu.Shiba
 * @version 1.0
 */
public final class XMLReader {

    /**
     * XML�t�@�C���p�X.
     */
    private String path = null;

    /**
     * XML�h�L�������g.
     */
    private Document doc = null;

    /**
     * XPath.
     */
    private XPath xpath = null;

    /**
     * ���\�[�X�t�@�C���i�N���X�p�X�̒ʂ��Ă���t�H���_�̃t�@�C���j����XML��ǂݍ���XMLReader�𐶐�����.
     * @param path XML�t�@�C���̃p�X
     * @return XMLReader
     * @throws ParserConfigurationException XML��̓G���[
     * @throws IOException XML�t�@�C���Ǎ��G���[
     * @throws SAXException  XML��̓G���[
     */
    public static XMLReader createInstanceAsResouce(final String path)
            throws ParserConfigurationException, SAXException, IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(path);
        if (is == null) {
            throw new IOException("�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B�t�@�C��=[" + path + "]");
        }
        XMLReader reader = new XMLReader(is);
        reader.path = path;
        return reader;
    }

    /**
     * �R���X�g���N�^.
     * @param is InputStream
     * @throws ParserConfigurationException XML��̓G���[
     * @throws IOException XML�t�@�C���Ǎ��G���[
     * @throws SAXException  XML��̓G���[
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
     * ������擾.
     * @param location XPath
     * @return ������
     * @throws XPathExpressionException XPath�G���[
     */
    public String readString(final String location)
            throws XPathExpressionException {
        return (String) xpath.evaluate(
                location, doc, XPathConstants.STRING);
    }

    /**
     * �P��m�[�h�擾.
     * @param location XPath
     * @return �P��m�[�h
     * @throws XPathExpressionException XPath�G���[
     */
    public Node readNode(final String location)
            throws XPathExpressionException {
        return (Node) xpath.evaluate(
                location, doc, XPathConstants.NODE);
    }

    /**
     * �����m�[�h�擾.
     * @param location XPath
     * @return NodeList
     * @throws XPathExpressionException XPath�G���[
     */
    public NodeList readNodeSet(final String location)
            throws XPathExpressionException {
        return (NodeList) xpath.evaluate(
                location, doc, XPathConstants.NODESET);
    }

    /**
     * XML�̑Ó������؂��s��.
     * @param schemaPath XML�X�L�[�}
     * @throws IOException ���o�̓G���[
     * @throws SAXException XML�Ó������؃G���[
     */
    public void validate(final String schemaPath)
            throws SAXException, IOException {
        InputStream is = ClassLoader.getSystemResourceAsStream(schemaPath);
        if (is == null) {
            throw new IOException("�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B�t�@�C��=[" + schemaPath + "]");
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
     * XML�S�s��Ԃ�.
     * @return XML�S�s
     * @throws IOException XML�ǂݍ��݃G���[
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
