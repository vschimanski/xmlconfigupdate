/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package xmlconfigupdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@SuppressWarnings("unused")
public class xmlConfigUpdate {

    public static void main(String[] args) throws ConfigurationException, IOException, ParserConfigurationException, SAXException, TransformerException {
        
            xmlConfigUpdate.UpdateConfig(args);
      
    }

    /*
    @param update_XML_path update file, then current_XML_CONFIG configuration file that should be updated
    */
    public static void UpdateConfig(String[] args) throws ConfigurationException, IOException, ParserConfigurationException, org.xml.sax.SAXException, TransformerException {

        final String XML_DEFAULT = "src//main//resources//updateconfig//config_update.xml";
        final String XML_CONFIG_DEFAULT = "src//main//resources//updateconfig//config.xml";
        File file;
        XMLConfiguration config;

        String update_XML_path = "", current_XML_CONFIG = "";

        if ((args != null) && (args.length > 0)) {
            update_XML_path = args[0];
            current_XML_CONFIG = args[1];
            file = new File(update_XML_path);
            config = new XMLConfiguration(current_XML_CONFIG);
            config.setEncoding("UTF-8");
        } else {
            System.out.println(" Usage : add params to the void config_updatexml_path config_xmlpath using defaults ");
            update_XML_path = XML_DEFAULT;
            current_XML_CONFIG = XML_CONFIG_DEFAULT;
            file = new File(update_XML_path);
            config = new XMLConfiguration(current_XML_CONFIG);
            config.setEncoding("UTF-8");
            System.out.println(" Done: check updated xml ");

        }

        // 01 add new element values 
        xmlApfsAddValue(file, config);

        // 02 remove exising elements
        xmlApfsRemoveExistingElement(file, config);

        // 03 Update exisiting elements values
        xmlApfsUpdateValue(file, config);

        // 04 ADD NEW child ROOT nodes (under apfs root element)  
        String dirPath = "src//main//resources//updateaddnewrootnodeelements//";
        // uncoment and check the rootelements child nodes 
        //(if you find hard time to code it with xPath's)

        File dir = new File(dirPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File fileXML : directoryListing) {

                if (fileXML.length() > 0) {
                    // uncoment if add new complex child elements to the config.
                    //System.out.println(fileXML.getAbsolutePath());                    
                    //xmlElementNodeAdd(fileXML, SourceFile, current_XML_CONFIG);
                }

            }

        } else {

            System.out.println("Error can't find directory to load child root import xml's ");
        }

    }

    ////////////////////////////////////////ADD NEW PARAMETERS PART START
    private static void xmlApfsAddValue(File file, XMLConfiguration config) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {

        File fXmlFile = file;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList flowList = doc.getElementsByTagName("addNewElementsAndValues");

        String xPathKey = "", xPathObjValue = "";

        for (int i = 0; i < flowList.getLength(); i++) {
            NodeList childList = flowList.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
             
                NodeList sublist = childList.item(j).getChildNodes();
                if (sublist.getLength() > 0) {
                    for (int k = 0; k < sublist.getLength(); k++) {
                        Node subNode = sublist.item(k);
                        if ("xPathKey".equals(subNode.getNodeName())) {
                            xPathKey = subNode.getTextContent();
                        }
                        if ("xPathObjValue".equals(subNode.getNodeName())) {
                            xPathObjValue = subNode.getTextContent();
                        }
                        if (xPathKey.length() > 0 && xPathObjValue.length() > 0) {
                            xmlApfsAddValueConfigSave(xPathKey, xPathObjValue, config);
                            xPathKey = "";
                            xPathObjValue = "";
                        }

                    }

                }

            }
        }

    }

    private static void xmlApfsAddValueConfigSave(String xPathKey, String xPathObjValue, XMLConfiguration config) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {
        config.reload();
        config.addProperty(xPathKey, xPathObjValue);
        config.save();
    }
    ////////////////////////////////////////ADD NEW PARAMETERS PART END

    ////////////////////////////////////////REMOVE PARAMETERS PART START
    private static void xmlApfsRemoveExistingElement(File file, XMLConfiguration config) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {
        config.reload();
        String key;
        File fXmlFile = file;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList flowList = doc.getElementsByTagName("deleteExistingElementsAndValues");
        for (int i = 0; i < flowList.getLength(); i++) {
            NodeList childList = flowList.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                Node childNode = childList.item(j);
                if ("element".equals(childNode.getNodeName())) {
                    //System.out.println(childList.item(j).getTextContent().trim());
                    key = childList.item(j).getTextContent().trim();
                    config.clearProperty(key);

                }
            }
        }
        config.save();
    }
    ////////////////////////////////////////REMOVE PARAMETERS PART END

    ////////////////////////////////////////UPDATE EXISTING PARAMETERS PART START
    private static void xmlApfsUpdateValue(File file, XMLConfiguration config) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {

        File fXmlFile = file;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList flowList = doc.getElementsByTagName("updateExistingElementsAndValues");

        String xPathKey = "", xPathObjValue = "";

        for (int i = 0; i < flowList.getLength(); i++) {
            NodeList childList = flowList.item(i).getChildNodes();
            for (int j = 0; j < childList.getLength(); j++) {
                NodeList sublist = childList.item(j).getChildNodes();
                if (sublist.getLength() > 0) {
                    for (int k = 0; k < sublist.getLength(); k++) {

                        Node subNode = sublist.item(k);
                        if ("xPathKey".equals(subNode.getNodeName())) {
                            xPathKey = subNode.getTextContent();
                        }

                        if ("xPathObjValue".equals(subNode.getNodeName())) {
                            xPathObjValue = subNode.getTextContent();

                        }

                        if (xPathKey.length() > 0 && xPathObjValue.length() > 0) {
                            xmlUpdateValueConfigSave(xPathKey, xPathObjValue, config);
                            xPathKey = "";
                            xPathObjValue = "";
                        }

                    }
                }

            }
        }

    }

    private static void xmlUpdateValueConfigSave(String xPathKey, String xPathObjValue, XMLConfiguration config) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {
        config.reload();
        config.setProperty(xPathKey, xPathObjValue);
        config.save();
    }
////////////////////////////////////////UPDATE EXISTING PARAMETERS PART END

////////////////////////////////////////ADD NEW CHILD ELEMENT TO ROOT START
    private static void xmlElementNodeAdd(File file, File SourceFile, String OutPutXMLConfig) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, TransformerConfigurationException, TransformerException {
        File docFile = SourceFile;
        String xmlString = xmlPrepareNewElementNodeAdd(file);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document documentXML;
        Document doc;

        builder = factory.newDocumentBuilder();
        documentXML = builder.parse(new InputSource(new StringReader(xmlString)));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(docFile);
        
        Node newNode = doc.importNode(documentXML.getDocumentElement(), true);
        //Need to import prior to appending it
        doc.getDocumentElement().appendChild(newNode);

        String outputURL = OutPutXMLConfig;
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(outputURL));

        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();

        transformer.transform(source, result);

    }

    private static String xmlPrepareNewElementNodeAdd(File file) throws IOException {

        final String rootElementStart = "<addNewChildToRootElement>";
        final String rootElementEnd = "</addNewChildToRootElement>";

        StringBuffer sb = new StringBuffer();
        String xmlElementResult;

        sb.append(FileUtils.readFileToString(file, "UTF-8"));
        xmlElementResult = sb.substring(sb.indexOf(rootElementStart) + rootElementStart.length(), sb.indexOf(rootElementEnd));

        return xmlElementResult.trim();

    }
////////////////////////////////////////ADD NEW CHILD ELEMENT TO ROOT END 

}
