package xml;

import interaction.Application;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import moore.Connection;
import moore.compatible_coding.SynchroState;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Wittman
 */
public class XMLParser{
    
    private Document doc;
    private File file;

    public XMLParser( File file) throws ParserConfigurationException, SAXException, IOException {
        this.file = file;
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        doc = builder.parse(file);
        doc.getDocumentElement().normalize();
    }
    
    public void parseFile()  {
        
        parseVertexes(doc.getElementsByTagName("vertex"));
        parseConditions(doc.getElementsByTagName("condition"));
        parseConnections(doc.getElementsByTagName("connection"));
    }
    
    private void parseVertexes(NodeList vertexes){
        List<String> ids = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        List<boolean []> codes = new ArrayList<>();
        for (int i = 0; i < vertexes.getLength(); i++) {
            ids.add(vertexes.item(i).getTextContent());
            int x = Integer.parseInt(((Element)vertexes.item(i)).getAttribute("x"));
            int y = Integer.parseInt(((Element)vertexes.item(i)).getAttribute("y"));
            points.add(new Point(x, y));
            boolean [] code = SynchroState.stringToCode(((Element)vertexes.item(i)).getAttribute("code"));
            codes.add(code);
        }
        //TODO таж проблема з встановленням даних
        Application.mediator.automatonTable.ids = ids;
        Application.mediator.automatonTable.points = points;
        Application.mediator.automatonTable.codes = codes;
    }
    
    private void parseConditions(NodeList conditions){
        List<String> cond = new ArrayList<>();
        for (int i = 0; i < conditions.getLength(); i++) {
            cond.add(conditions.item(i).getTextContent());
        }
        Application.mediator.automatonTable.conditions = cond;
    }
    
    private void parseConnections(NodeList connections){
        List<Connection> connList= new ArrayList<>();
        
        for (int i = 0; i < connections.getLength(); i++) {
            
            Element connection = (Element) connections.item(i);

            String signal = connection.getAttribute("signal");
            
            int from = Integer.parseInt(connection.getElementsByTagName("from").item(0).getTextContent());
            int to = Integer.parseInt(connection.getElementsByTagName("to").item(0).getTextContent());
            
            int [] cond = new int [Application.mediator.automatonTable.conditions.size()];
            NodeList conds = connection.getElementsByTagName("X");
            
            for (int j = 0; j < conds.getLength(); j++) {
                cond[j] = Integer.parseInt(conds.item(j).getTextContent());
            }
            
            Connection c = new Connection(from, to, cond, signal);
            connList.add(c);
        }
        Application.mediator.automatonTable.connections = connList;
        
    }
}
