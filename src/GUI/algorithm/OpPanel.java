package GUI.algorithm;

import static GUI.algorithm.AlgPanel.heightCell;
import static GUI.algorithm.AlgPanel.widthCell;
import java.awt.Dimension;
import java.awt.Graphics;
import parser.Operator;

/**
 *
 * @author wittman
 */
public abstract class OpPanel{
    
    protected static int width = 120;
    protected static int height = 40;
    /**
     * Чотири точки
     * [0]ліво, [1]верх, [2]право, [3]низ
     */
    public Dimension [] points;
    protected int numRow;
    protected int numColon;
    public Dimension position;
    protected Operator operator;
    
    public OpPanel(Operator op){
        this.operator = op;
    }
    

    public void getPointsOfConnection(){
        points = new Dimension[4];

        points[0] = new Dimension(position.width - width/2, position.height);
        points[1] = new Dimension(position.width, position.height - height/2);
        points[2] = new Dimension(position.width + width/2, position.height);
        points[3] = new Dimension(position.width, position.height + height/2);
        
    }
    
    
    public void initPosition(int numRow, int numColon){
        this.numRow = numRow;
        this.numColon = numColon;
        getPositionOnGrid();
    }

    public void getPositionOnGrid(){
        int x = numRow*widthCell - widthCell/2;
        int y = numColon*heightCell - heightCell/2;
        position = new Dimension(x, y);  
        getPointsOfConnection();
    }
    
    public static void drawDownArrow(Graphics g, int x, int y){
        g.drawLine(x, y, x+3, y-10);
        g.drawLine(x, y, x-3, y-10);
    }
    
    public static void drawLeftArrow(Graphics g, int x, int y){
        g.drawLine(x, y, x+10, y+3);
        g.drawLine(x, y, x+10, y-3);
    }

    public abstract void drawPanel(Graphics g);
    public abstract void drawConnection(Graphics g);
}
