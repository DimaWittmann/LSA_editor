package interaction;

import GUI.algorithm.AlgPanel;
import GUI.algorithm.XPanel;
import GUI.algorithm.YPanel;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import parser.Operator;
import static parser.Operator.Type.X;
import parser.ParseException;

/**
 *
 * @author wittman
 */
public class AlgController {
    
    public Controller controller;
    protected AlgPanel panel;
    protected JFrame frame;
    
    public AlgController(Controller controller){
        this.controller = controller;
        
        panel = new AlgPanel(this);
                
        frame = new JFrame("Algo");
        frame.setMinimumSize(new Dimension(860, 480));
        frame.add(panel);
        
    }
    
    public void initOperatorPanels(){
        controller.readLSA();
        try {
            controller.parser.parse();
        } catch (ParseException ex) {
            controller.writeInfo(ex.getMessage());
        }
        Operator curr = controller.parser.start;

        int posX = 1;
        int posY = 1;
        while(curr != null){
            if(!curr.isConnection()){
                if(curr.type == X){
                    curr.panel = new XPanel(curr);
                }else{
                    curr.panel = new YPanel(curr);
                }
                curr.panel.initPosition(posX, posY);
                posY++;
            }
            curr = curr.next;
        }
        
    }
    

    public void drawAlgoFrame(){
        initOperatorPanels();
        
        frame.setVisible(true);
    }
}
