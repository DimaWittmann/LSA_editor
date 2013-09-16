package GUI;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author wittmann
 */
public class Frame extends JFrame{
    
    public Frame(String name){
        super(name);
        WorkPanel wp = new WorkPanel();
        this.add(wp);
        this.setMinimumSize(new Dimension(840, 480));
        
    }

}
