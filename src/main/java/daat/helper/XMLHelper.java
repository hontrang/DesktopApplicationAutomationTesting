package daat.helper;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

public class XMLHelper {
    private static DocumentBuilderFactory docFactory;
    private static Document doc;
    private static Transformer transformer;
    private static DOMSource source;
    private static StreamResult result;
    private static XPath xPathNode;

    private static void openXMLFile(String xmlFile) {
        try {
            File inputFile = new File(xmlFile);
            docFactory = DocumentBuilderFactory.newInstance();
            doc = docFactory.newDocumentBuilder().parse(inputFile);
            xPathNode = XPathFactory.newInstance().newXPath();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void openXMLFileFromString(String xmlContent) {
        try {
            docFactory = DocumentBuilderFactory.newInstance();
            InputSource is = new InputSource(new StringReader(xmlContent));
            doc = docFactory.newDocumentBuilder().parse(is);
            xPathNode = XPathFactory.newInstance().newXPath();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(String xmlFile) {
        try {
            File outputFile = new File(xmlFile);
            doc.getDocumentElement().normalize();
            transformer = TransformerFactory.newInstance().newTransformer();
            source = new DOMSource(doc);
            result = new StreamResult(outputFile);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.transform(source, result);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
    }

    /*
        String node xpath - /orders/order
        Map attribute - order-no:00001536
     */

    public static void updateNodeAttribute(String xml, String node, Map<String, String> attr) {
        Node currentNode = goToNode(xml, node);
        NamedNodeMap attributes = currentNode.getAttributes();
        for (String str : attr.keySet()) {
            attributes.getNamedItem(str).setTextContent(attr.get(str));
        }
    }

    /*
        String node xpath - /orders/order/shipping/line-item/item/sku
        String text - 00001536
     */

    public static void updateChildrenValue(String xml, String node, String text) {
        goToNode(xml, node).setTextContent(text);
    }

    /*
     *   add new node
     */

    public static void addNewNode(String xml, String node, String tagName, String tagValue) {
        goToNode(xml, node).appendChild(doc.createElement(tagName)).setTextContent(tagValue);
    }

    /*
     *   delete node
     */

    public static void deleteNode(String xml, String node) {
        Node child = goToNode(xml, node);
        child.getParentNode().removeChild(child);
    }

    /*
        use xpath - String node xpath - /orders/order/shipping/line-item/item/sku
    */

    private static Node goToNode(String xml, String node) {
        openXMLFile(xml);
        try {
            return (Node) xPathNode.compile(node).evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Node goToNodeFromString(String xml, String node) {
        openXMLFileFromString(xml);
        try {
            return (Node) xPathNode.compile(node).evaluate(doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getText(String xml, String node) {
        Node currentNode = goToNodeFromString(xml, node);
        NamedNodeMap attributes = currentNode.getAttributes();
        return attributes.getNamedItem("text").getTextContent();
    }
}
