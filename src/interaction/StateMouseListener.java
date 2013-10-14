package interaction;

import GUI.automaton.StatePanel;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author Wittman
 */
public class StateMouseListener implements MouseListener, MouseMotionListener {
    
    
    private int startX;
    private int startY;
    private Point startP;

    @Override
    public void mouseDragged(MouseEvent e) {
        StatePanel panel = (StatePanel)e.getComponent();
        Point newPoint = new Point(startP.x+(e.getXOnScreen()-startX),
                startP.y+(e.getYOnScreen()-startY));
        panel.setLocation(newPoint);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
 
        startX = e.getXOnScreen();
        startY = e.getYOnScreen();
        startP = e.getComponent().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
