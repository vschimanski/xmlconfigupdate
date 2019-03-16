
package testxml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

public class xmlTestResources { 
	
	@SuppressWarnings("unused")
    @Test
 public void testconfig() throws ConfigurationException, IOException, ParserConfigurationException, org.xml.sax.SAXException, TransformerException {

        ////src//main//resources//updateconfig
        
        String update_XML_path = "src//main//resources//updateconfig//config_update.xml";
        String current_XML_CONFIG = "src//main//resources//updateconfig//config.xml";
       
        xmlTestResources xUpdate = new xmlTestResources();
        
        
        File file = new File(update_XML_path);

        System.out.println(file.getAbsolutePath());
        
          
        XMLConfiguration config = new XMLConfiguration(current_XML_CONFIG);
        config.setEncoding("UTF-8");
        
    }

}
