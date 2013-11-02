package interaction.menu_listeners;

import interaction.Application;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Wittman
 */
public class DrawMenuListener implements ActionListener{

    public final static String DRAW_AUTOMATON = "Show automaton";
    public final static String DRAW_ALGO = "Draw algorithm";
    public final static String DRAW_AUTOMATON_TABLE = "Show automaton table";
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            
            case DRAW_AUTOMATON:
                Application.mediator.showAutomatonFrame();
                break;
                
            case DRAW_ALGO:
                Application.mediator.algPanel.drawAlgoFrame();
                break;
            
            case DRAW_AUTOMATON_TABLE:
                Application.mediator.showAutomatonTableFrame();
                break;
        }
    }
}
