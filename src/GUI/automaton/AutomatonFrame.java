package GUI.automaton;

import GUI.WorkPanel;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Wittman
 */
public class AutomatonFrame extends JFrame{


    public AutomatonPanel panel;
    public AutomatonFrame(){
        super("Automaton");
        
        panel = new AutomatonPanel();
        
        this.add(panel);
        this.setSize(new Dimension(WorkPanel.width, WorkPanel.height));
        this.setDefaultCloseOperation(AutomatonFrame.HIDE_ON_CLOSE);
    }
}
