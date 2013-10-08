package interaction;

import GUI.algorithm.AlgPanel;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author wittman
 */
public class AlgController {
    
    protected AlgPanel panel;
    protected JFrame frame;
    
    public AlgController(){
        panel = new AlgPanel();
                
        frame = new JFrame("Algo");
        frame.setMinimumSize(new Dimension(860, 480));
        frame.add(panel);
        
    }
    
    public void drawAlgoFrame(){
        panel.initOperatorPanels();
        frame.setVisible(true);
    }
}
