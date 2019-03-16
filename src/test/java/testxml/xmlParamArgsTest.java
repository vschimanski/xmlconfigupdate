package testxml;

import java.io.File;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

public class xmlParamArgsTest { 

    final String XML_DEFAULT = "src//main//resources//updateconfig//config_update.xml";
    final String XML_CONFIG_DEFAULT = "src//main//resources//updateconfig//config.xml";

    @SuppressWarnings("unused")
	@Test
    public void testconfig() throws ConfigurationException {

        String[] args = {"src//main//resources//updateconfig//config_update.xml", "src//main//resources//updateconfig//config.xml"};

            String update_XML_path = "", current_XML_CONFIG = "";

        if ((args != null) && (args.length > 0)) {
            update_XML_path = args[0];
            current_XML_CONFIG = args[1];
            
			File file = new File(update_XML_path);
            XMLConfiguration config = new XMLConfiguration(current_XML_CONFIG);
            config.setEncoding("UTF-8");
        } else {
            System.out.println(" Usage : java program config_updatexml_path config_xmlpath using defaults ");
            update_XML_path = XML_DEFAULT;
            current_XML_CONFIG = XML_CONFIG_DEFAULT;
            File file = new File(update_XML_path);
            XMLConfiguration config = new XMLConfiguration(current_XML_CONFIG);
            config.setEncoding("UTF-8");

        }

    }

}
