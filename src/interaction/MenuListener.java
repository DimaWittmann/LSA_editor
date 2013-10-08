package interaction;

import GUI.WorkPanel;
import internal_representation.LSAmatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import parser.Parser;

/**
 *
 * @author wittman
 */
public class MenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Run":
                    Application.mediator.parseLSA();
                    LSAmatrix m = Application.mediator.matrix;
                    Application.mediator.writeInfo(m.toString());
                    break;
                    
                    
                case "Validate":
                    Application.mediator.parseLSA();
                    m = Application.mediator.matrix;
                    String messages = m.validateMatrix();
                    if(messages.equals("")){
                        Application.mediator.writeInfo("Matrix is validated\n");
                    }else{
                        Application.mediator.writeInfo("Some problems with vertexes: \n"
                                + messages );
                    }
                    Application.mediator.writeInfo(m.showRoads());
                    break;
                    
                    
                case "Draw algorithm":
                    Application.mediator.algPanel.drawAlgoFrame();
                    break;
                    
                    
                case "Synthesize automaton":
                    Application.mediator.parseLSA();
                    Application.mediator.synthesizer.findAllConnetions();
                    Application.mediator.writeInfo(Application.mediator.synthesizer.showConnections());
                    break;
                    
                    
                case "New":
                    Application.mediator.parser = new Parser();
                    Application.mediator.setOptionalTitle("no name");
                    Application.mediator.wp.inputArea.setText("");
                    Application.mediator.wp.outputArea.setText("");
                    break;
                    
                    
                case "Save":
                    JFileChooser fc = new JFileChooser();
                    int res = fc.showSaveDialog(Application.mediator.wp);
                    
                    if(res == JFileChooser.APPROVE_OPTION){
                        File file = fc.getSelectedFile();
                        Application.mediator.setOptionalTitle(file.getName());
                        Application.mediator.parseLSA();

                        try {
                            Application.mediator.matrix.saveToFile(file);
                        } catch (IOException ex) {
                            Logger.getLogger(MenuListener.class.getName()).log(Level.SEVERE, null, ex);
                            Application.mediator.writeInfo(ex.getMessage());
                        }
                        
                    }
                    break;
                    
                    
                case "Load":
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
                    
                    
                case "Save automaton to XML":
                    fc = new JFileChooser();
                    res = fc.showSaveDialog(Application.mediator.wp);
                    
                    if(res == JFileChooser.APPROVE_OPTION){
                        File file = fc.getSelectedFile();
                        
                        Application.mediator.parseLSA();
                        Application.mediator.synthesizer.findAllConnetions();
                        Application.mediator.synthesizer.saveToXML(file);
                    }
                    break;
            }
        } 

}