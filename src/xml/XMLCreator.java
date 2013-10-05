package xml;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import moore.Connection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author Wittman
 */

public class XMLCreator {

    List<Connection> connections;
    Document dom;
    File file;

    public XMLCreator(File file) {
        this.file = file;
    }
    
    public void loadData(List<Connection>connections){
        this.connections = connections;
    }

    private void createDocument(){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.newDocument();
            createDOMTree();
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    private void createDOMTree(){
        Element rootEle = dom.createElement("connections");
        dom.appendChild(rootEle);
        
        int i = 0;
        for(Connection nextConn: connections){
            Element connEle = createConnElement(nextConn , i);
            rootEle.appendChild(connEle);
            i++;
        }

    }
    
    private Element createConnElement(Connection c, int id){
        
        Element connection = dom.createElement("Connection");
        connection.setAttribute("id", String.valueOf(id));
        connection.setAttribute("signal", c.signalId);
        connection.appendChild(addTextElement("from", String.valueOf(c.from)));
        connection.appendChild(addTextElement("to", String.valueOf(c.to)));
       
        Element conditions = dom.createElement("conditions");
        for (int i = 0; i < c.conditions.length; i++) {
            Element xEl = addTextElement("X", String.valueOf(c.conditions[i]));
            xEl.setAttribute("id", String.valueOf(i));
            conditions.appendChild(xEl);
        }
        
        connection.appendChild(conditions);
        
        
        return connection;
    }
    
    
    private Element addTextElement( String name, String value){
        Element newE = dom.createElement(name);
        Text fromI = dom.createTextNode(value);
        newE.appendChild(fromI);
        return newE;
    }
    
    public void writeToFile() {
        createDocument();
        OutputFormat format = new OutputFormat(dom);
        format.setIndenting(true);
        try {
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(file), format);
            serializer.serialize(dom);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XMLCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

