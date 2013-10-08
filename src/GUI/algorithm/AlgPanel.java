package GUI.algorithm;
import interaction.Application;
import java.awt.Graphics;
import javax.swing.JPanel;
import parser.Operator;
import static parser.Operator.Type.X;

/**
 *
 * @author wittman
 */
public class AlgPanel extends JPanel{
    
    protected static int widthCell = 100;
    protected static int heightCell = 50;
    
    public AlgPanel(){
    }

    @Override
    protected void paintComponent(Graphics g) {
     
        Operator op = Application.mediator.parser.start;
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
    
       public void initOperatorPanels(){
        Application.mediator.parseLSA();

        Operator curr = Application.mediator.parser.start;

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
}
