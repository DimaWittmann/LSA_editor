package GUI.algorithm;
import interaction.AlgController;
import java.awt.Graphics;
import javax.swing.JPanel;
import parser.Operator;

/**
 *
 * @author wittman
 */
public class AlgPanel extends JPanel{
    
    protected static int widthCell = 100;
    protected static int heightCell = 50;
    protected AlgController algController;
    
    public AlgPanel(AlgController controller){
        this.algController = controller;
    }

    @Override
    protected void paintComponent(Graphics g) {
     
        Operator op = algController.controller.parser.start;
        while(op != null){
            if(!op.isConnection()){
                op.panel.drawPanel(g);
                if(op.next() != null){
                    op.panel.drawConnection(g);
                }
            }
            op = op.next;
        } 
    }
}
