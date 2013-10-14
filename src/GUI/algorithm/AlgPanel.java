package GUI.algorithm;
import GUI.WorkPanel;
import interaction.Application;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;
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
    protected JFrame frame;
    
    public AlgPanel(){
        this.setLayout(null);
        
        frame = new JFrame("Algo");
        frame.setSize(new Dimension(WorkPanel.width, WorkPanel.height));
        frame.add(this);
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

        Operator curr = Application.mediator.parser.first;

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
        //TODO винести створення в Mediator
        initOperatorPanels();
        frame.setVisible(true);
    }

        
}
    
