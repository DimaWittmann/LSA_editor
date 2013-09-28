package GUI.algorithm;

import java.awt.Font;
import java.awt.Graphics;
import parser.Operator;
import static parser.Operator.Type.I;
import static parser.Operator.Type.O;

/**
 *
 * @author wittman
 */
public class XPanel extends OpPanel{

    public XPanel(Operator op) {
        super(op);
    }

    @Override
    public void drawPanel(Graphics g) {
        
        int []x = {position.width-width/2, position.width, position.width+width/2, position.width};
        int []y = {position.height, position.height+height/2, position.height, position.height-height/2};
        g.drawPolygon(x, y, 4);
        
        Font font = g.getFont();
        int posX = position.width - font.getSize()*operator.toString().length()/4;
        int posY = position.height + font.getSize()/2;
        g.drawString(operator.toString(), posX, posY);
        
    }

    @Override
    public void drawConnection(Graphics g) {
        
    }
    
    
}
