/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interaction;
import GUI.WorkPanel;
import java.awt.Dimension;
import javax.swing.JFrame;
import parser.Parser;

/**
 *
 * @author Wittmann
 */
public class Controller {
    
    JFrame f;
    WorkPanel wp;
    Parser p;
    
    public Controller(){
        f = new JFrame("Editor");
        
        p = new Parser();
        
        wp = new WorkPanel(p);
        f.add(wp);
        f.setMinimumSize(new Dimension(840, 480));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        f.setVisible(true);
        
    }
    
}
