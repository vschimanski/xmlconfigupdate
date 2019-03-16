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


package testxml;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class xmlUpdateTest {

  @SuppressWarnings("unused")	
  @Test
  public void testconfig() throws ConfigurationException, ParserConfigurationException, SAXException, IOException {
         
        String update_XML_path = "src//test//java//xml//config_update.xml";
        String current_XML_CONFIG = "src//test//java//xml//config.xml";
       
        xmlUpdateTest xUpdate = new xmlUpdateTest();
        
        
        File file = new File(update_XML_path);

        System.out.println(file.getAbsolutePath());   
        XMLConfiguration config = new XMLConfiguration(current_XML_CONFIG);
        config.setEncoding("UTF-8");

        String dirPath = "src//main//resources//updateconfig/updateaddnewrootnodeelements";
        

        File dir = new File(dirPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File fileXML : directoryListing) {

                if (fileXML.length() > 0) {
                    // uncoment if addding new complex child elements to the config.
                    //System.out.println(fileXML.getAbsolutePath());                    
                    //xmlElementNodeAdd(fileXML, SourceFile, current_XML_CONFIG);
                }

            }

        } else {
            System.out.println("Error can't find directory to load xml's ");
        }

    }
  
  
  @Test    
public  void xmlApfsAddValue() throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {
        String current_XML_CONFIG = "src\\test\\java\\xml\\config.xml";
        String update_XML_path = "src\\test\\java\\xml\\config_update.xml";
        File file = new File(update_XML_path);
         XMLConfiguration config = new XMLConfiguration(current_XML_CONFIG);
        config.setEncoding("UTF-8");
        
        
        
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
  
    public void xmlApfsAddValueConfigSave(String xPathKey, String xPathObjValue, XMLConfiguration config) throws ParserConfigurationException, org.xml.sax.SAXException, IOException, ConfigurationException {
        config.reload();
        config.addProperty(xPathKey, xPathObjValue);
        config.save();
    }
  
   

    
  
   


}
