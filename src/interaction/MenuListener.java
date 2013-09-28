package interaction;

import GUI.WorkPanel;
import internal_representation.LSAmatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import parser.ParseException;
import parser.Parser;

/**
 *
 * @author wittman
 */
public class MenuListener implements ActionListener{
        Controller controller;
        
        public MenuListener(Controller controller){
            this.controller = controller;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Run":
                    try {
                        controller.parser.linkTokens(controller.parser.getTokens(controller.wp.inputArea.getText()));
                        LSAmatrix m = controller.parser.toMatrix();
                        controller.wp.outputArea.setText(m.toString());
                    } catch (ParseException ex) {
                        Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        controller.wp.outputArea.setText(ex.getMessage());
                    }
                    break;
                case "Validate":
                    try {
                        controller.parser.linkTokens(controller.parser.getTokens(controller.wp.inputArea.getText()));
                        LSAmatrix m = controller.parser.toMatrix(); 
                        String messages = m.validateMatrix();
                        if(messages.equals("")){
                            controller.writeInfo("Matrix is validated\n");
                        }else{
                            controller.writeInfo("Some problems with vertexes: \n" 
                                    + messages );
                        }
                        controller.writeInfo(m.showRoads());
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        controller.writeInfo(ex.getMessage());
                    }
                    break;
                case "Draw algorithm":
                    controller.algController.drawAlgoFrame();
                    break;
                case "New":
                    controller.parser = new Parser();
                    controller.setOptionalTitle("no name");
                    controller.wp.inputArea.setText("");
                    controller.wp.outputArea.setText("");
                    break;
                case "Save":
                    JFileChooser fc = new JFileChooser();
                    int res = fc.showSaveDialog(controller.wp);
                    
                    if(res == JFileChooser.APPROVE_OPTION){
                        ObjectOutputStream oos = null;
                        try {
                            
                            File file = fc.getSelectedFile();
                            controller.setOptionalTitle(file.getName());
                            
                            FileOutputStream fos = new FileOutputStream(file);
                            oos = new ObjectOutputStream(fos);
                            controller.readLSA();
                            oos.writeObject(controller.parser.parse());
                            oos.flush();
                        } catch ( IOException | ParseException ex) {
                            Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                            controller.writeInfo(ex.getMessage());
                        } finally {
                            try {
                                oos.close();
                            } catch (IOException ex) {
                                Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    break;
                case "Load":
                    //FIXME не завантажує безумовні переходи
                    fc = new JFileChooser();
                    res = fc.showOpenDialog(controller.wp);
                    if(res == JFileChooser.APPROVE_OPTION){
                        ObjectOutputStream oos = null;
                        try {
                            File file = fc.getSelectedFile();
                            controller.setOptionalTitle(file.getName());
                            
                            FileInputStream fis = new FileInputStream(file);
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            LSAmatrix matrix = (LSAmatrix) ois.readObject();
                            
                            matrix.toGraph(controller.parser);
                            String text = controller.parser.createLSA();
                            controller.wp.inputArea.setText(text);
                            controller.wp.outputArea.setText("");
                            
                        } catch ( ClassNotFoundException | IOException ex) {
                            Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                            controller.writeInfo(ex.getMessage());
                        } 
                    }
                    break;
                    
            }
        } 

}