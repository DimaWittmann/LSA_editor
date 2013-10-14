package GUI.algorithm;

import static GUI.algorithm.AlgPanel.heightCell;
import static GUI.algorithm.OpPanel.drawDownArrow;
import static GUI.algorithm.OpPanel.drawLeftArrow;
import java.awt.Font;
import java.awt.Graphics;
import parser.Operator;
import parser.X;


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
        char []F = {'1', '0'};
        g.drawChars(F, 0, 1, position.width+width/2, position.height);
        g.drawChars(F, 1, 1, position.width, position.height+height/2+10);
        
        if(operator.pred != null){//TODO допрацювати для висячих вершин
            g.drawLine(this.points[1].width, this.points[1].height-(int)(heightCell*0.3), 
                this.points[1].width, this.points[1].height);
             drawDownArrow(g, this.points[1].width, this.points[1].height);
        }
        if(operator.next() != null){
            g.drawLine(this.points[3].width, this.points[3].height, 
                this.points[3].width, this.points[3].height+(int)(heightCell*0.2));
        }
        
    }

    @Override
    public void drawConnection(Graphics g) {
        X operator = ((X)this.operator);
        
        assert(operator.next(false).pos-operator.pos == 1); //поки за не виконанм сигналом може йти тільки Y/E
        //TODO відповідно реалізувати можливість переходу по брехні
        //ти також реалізацію next для X
        
        g.drawLine(this.points[3].width, this.points[3].height, 
                operator.next(false).panel.points[1].width, 
                operator.next(false).panel.points[1].height);
        
        //перехід за виконанням умови
        int l = (int) (AlgPanel.widthCell*(0.2+1*Math.random()));
 
        g.drawLine(this.points[2].width, this.points[2].height, 
                this.points[2].width + l, this.points[2].height);

        int diff =  operator.next(true).pos-operator.pos;
        
        g.drawLine(this.points[2].width + l, this.points[2].height, 
                this.points[2].width + l, (int) (this.points[2].height +(diff-0.5)*heightCell));
        g.drawLine(this.points[2].width + l, (int)(this.points[2].height +(diff-0.5)*heightCell) , 
                this.points[2].width - width/2, (int)(this.points[2].height +(diff-0.5)*heightCell ));
        drawLeftArrow(g, this.points[2].width - width/2, (int)(this.points[2].height +(diff-0.5)*heightCell ));
 
    }
    
    
}
