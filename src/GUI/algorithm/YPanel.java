package GUI.algorithm;

import static GUI.algorithm.AlgPanel.heightCell;
import static GUI.algorithm.AlgPanel.widthCell;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import parser.Operator;

/**
 *
 * @author wittman
 */
public class YPanel extends OpPanel{

    public YPanel(Operator op) {
        super(op);
    }

    @Override
    public void drawPanel(Graphics g) {
        
        g.drawRect(position.width-width/2, position.height-height/2, width, height);
        
        Font font = g.getFont();
        int posX = position.width - font.getSize()*operator.toString().length()/4;
        int posY = position.height + font.getSize()/2;
        g.drawString(operator.toString(), posX, posY);
    }
    
    @Override
    public void drawConnection(Graphics g) {
        if(operator.next().pos-operator.pos == 1){
            g.drawLine(this.points[3].width, this.points[3].height, 
                    operator.next().panel.points[1].width, 
                    operator.next().panel.points[1].height);
            drawDownArrow(g, operator.next().panel.points[1].width, operator.next().panel.points[1].height);
        }else{
            int l = (int) (AlgPanel.widthCell*(0.5+0.2*Math.random()));
            Dimension d = new Dimension(widthCell*numRow - widthCell/2, 
                    heightCell*numColon);;
            g.drawLine(this.points[3].width, this.points[3].height, d.width, d.height);
            g.drawLine(d.width, d.height, d.width + l, d.height);
            
            int diff =  operator.next().pos-operator.pos;
            g.drawLine(d.width + l, d.height, 
                    d.width + l, d.height + (diff-1)*heightCell);
            g.drawLine(d.width + l, d.height + (diff-1)*heightCell, 
                    d.width, d.height + (diff-1)*heightCell);
            drawLeftArrow(g, d.width, d.height + (diff-1)*heightCell);
            
        }
    } 
    
}
