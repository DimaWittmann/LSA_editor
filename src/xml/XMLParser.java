package xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Wittman
 */
public class XMLParser extends DefaultHandler{
    
    private Document dom;
    private File file;

    public XMLParser( File file) {
        this.file = file;
    }
    
    public void parseFile() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder db = dbf.newDocumentBuilder();
        dom = (Document) db.parse(file);
        
    }
    
    public void parseDocument(){
        Element docElement = dom.getDocumentElement();
        
        
    }
    
   
    
}
