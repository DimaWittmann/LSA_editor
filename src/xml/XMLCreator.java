package xml;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import internal_representation.AutomatonTable;
import java.awt.Point;
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
import moore.compatible_coding.SynchroState;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 *
 * @author Wittman
 */

public class XMLCreator {

    //TODO зробити красивий доступ до зачень
    List<Connection> connections;
    List<String> conditions;
    List<String> ids;
    List<Point> points;
    List<boolean[]> codes;
    Document dom;
    File file;

    public XMLCreator(File file) {
        this.file = file;
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
        Element rootEle = dom.createElement("table");
        dom.appendChild(rootEle);
        
        Element idsRoot = dom.createElement("vertexes");
        rootEle.appendChild(idsRoot);
        
        for (int i = 0; i < ids.size(); i++) {
            Element vertex = dom.createElement("vertex");
            vertex.setAttribute("zId", String.valueOf(i));
            vertex.setAttribute("code", SynchroState.codeToString(codes.get(i)));
            if(points != null && points.size() == ids.size()){
                vertex.setAttribute("x", String.valueOf(points.get(i).x));
                vertex.setAttribute("y", String.valueOf(points.get(i).y));
            }
            Text id = dom.createTextNode(ids.get(i));
            vertex.appendChild(id);
            idsRoot.appendChild(vertex);
        }
        
        Element condRoot = dom.createElement("conditions");
        rootEle.appendChild(condRoot);
        
        for (int i = 0; i < conditions.size(); i++) {
            Element cEl = addTextElement("condition", conditions.get(i));
            cEl.setAttribute("id", String.valueOf(i));
            condRoot.appendChild(cEl);
        }
        
        
        Element connRoot = dom.createElement("connections");
        rootEle.appendChild(connRoot);
        
        int i = 0;
        for(Connection nextConn: connections){
            Element connEle = createConnElement(nextConn , i);
            connRoot.appendChild(connEle);
            i++;
        }
        
    }
    
    private Element createConnElement(Connection c, int id){
        
        Element connection = dom.createElement("connection");
        connection.setAttribute("id", String.valueOf(id));
        connection.setAttribute("signal", c.signalId);
        connection.appendChild(addTextElement("from", String.valueOf(c.from)));
        connection.appendChild(addTextElement("to", String.valueOf(c.to)));
       
        Element cond = dom.createElement("conditions");
        for (int i = 0; i < c.conditions.length; i++) {
            Element xEl = addTextElement("X", String.valueOf(c.conditions[i]));
            xEl.setAttribute("id", conditions.get(i));
            cond.appendChild(xEl);
        }
        connection.appendChild(cond);
        return connection;
    }
    
    
    private Element addTextElement( String name, String value){
        Element newE = dom.createElement(name);
        Text fromI = dom.createTextNode(value);
        newE.appendChild(fromI);
        return newE;
    }
    
    public void writeToFile(AutomatonTable table) {
        this.connections = table.connections;
        this.conditions = table.conditions;
        this.ids = table.ids;
        this.points = table.points;
        this.codes = table.codes;
        
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

