package GUI.automaton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

/**
 *
 * @author Wittman
 */
public class StatePanel extends JPanel{

    public final static int R = 25;
    
    private String id;
    private String signal;
    private String code;

    public StatePanel(String id, String signal) {
        this.id = id;
        this.signal = signal;
        this.setSize(R*2+1, R*2+1);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(0, 0, R*2, R*2);
        g.setColor(Color.BLACK);
        g.drawOval(0, 0, R*2, R*2);
        g.drawString(id, R-5, R/2+3);
        
        if(signal.length()>0){// TODO знову якісь костилі, пов'язано з тим, що при повторниму аналізі зникають сигнали
            if(signal.charAt(0) == 'S' ){
                g.drawString(String.valueOf(0), R-5, R*3/2);
            }else{
                g.drawString(signal, R-5, R*3/2);
            }
        }else{
            g.drawString("0", R-5, R*3/2);
        }
        if(code != null){
            g.setColor(Color.RED);
            g.drawString(code, 0, R/2);
            g.setColor(Color.BLACK);
        }
        g.drawLine(0, R, R*2, R);
    }

    @Override
    public void setLocation(int x, int y) {
        //FIXME зробити перевірку на вихід за межі панелі
        super.setLocation(x, y);
        
        if(this.getParent() != null){
            this.getParent().repaint();            
        }
    }
    
    /**
     * Повертає центр панелі
     * @return точку в центрі панелів
     */
    public Point getCenter(){
        Point center = new Point(getLocation().x+R-1, getLocation().y+R-1);
        return center;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
