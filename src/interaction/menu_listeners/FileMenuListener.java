package interaction.menu_listeners;

import GUI.WorkPanel;
import interaction.Application;
import internal_representation.LSAmatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import parser.Parser;
import xml.XMLParser;

/**
 *
 * @author wittman
 */
public class FileMenuListener implements ActionListener{
    
    public final static String NEW = "New";
    public final static String SAVE = "Save";
    public final static String LOAD = "Load";
    public final static String SAVE_XML = "Save automaton to XML";
    public final static String LOAD_XML = "Load automaton from XML";

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){

            case NEW:
                Application.mediator.parser = new Parser();
                Application.mediator.setOptionalTitle("no name");
                Application.mediator.wp.inputArea.setText("");
                Application.mediator.wp.outputArea.setText("");
                break;


            case SAVE:
                JFileChooser fc = new JFileChooser();
                int res = fc.showSaveDialog(Application.mediator.wp);

                if(res == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();
                    Application.mediator.setOptionalTitle(file.getName());
                    Application.mediator.parseLSA();

                    try {
                        Application.mediator.matrix.saveToFile(file);
                    } catch (IOException ex) {
                        Logger.getLogger(FileMenuListener.class.getName()).log(Level.SEVERE, null, ex);
                        Application.mediator.writeInfo(ex.getMessage());
                    }

                }
                break;


            case LOAD:
                fc = new JFileChooser();
                res = fc.showOpenDialog(Application.mediator.wp);
                if(res == JFileChooser.APPROVE_OPTION){
                    ObjectOutputStream oos = null;
                    try {
                        File file = fc.getSelectedFile();
                        Application.mediator.setOptionalTitle(file.getName());
                        Application.mediator.parser = new Parser(new LSAmatrix(file));
                        Application.mediator.wp.inputArea.setText(Application.mediator.parser.createLSA());
                    } catch ( ClassNotFoundException | IOException ex) {
                        Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        Application.mediator.writeInfo(ex.getMessage());
                    } 
                }
                break;


            case SAVE_XML:


                fc = new JFileChooser();
                res = fc.showSaveDialog(Application.mediator.wp);

                if(res == JFileChooser.APPROVE_OPTION){
                    File file = fc.getSelectedFile();

                    Application.mediator.saveXML(file);
                }
                break;


            case LOAD_XML:
                fc = new JFileChooser();
                res = fc.showOpenDialog(Application.mediator.wp);
                if(res == JFileChooser.APPROVE_OPTION){
                    try {
                        XMLParser xmlc = new XMLParser(fc.getSelectedFile());
                        xmlc.parseFile();
                        Application.mediator.writeInfo(Application.mediator.synthesizer.showConnections());
                    } catch (        ParserConfigurationException | SAXException | IOException ex) {
                        Logger.getLogger(FileMenuListener.class.getName()).log(Level.SEVERE, null, ex);
                        Application.mediator.writeInfo(ex.getMessage());
                    }
                }

                break;
        }
    } 

}