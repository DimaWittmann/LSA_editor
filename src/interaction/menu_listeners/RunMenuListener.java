package interaction.menu_listeners;

import interaction.Application;
import internal_representation.LSAmatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Wittman
 */
public class RunMenuListener implements ActionListener{

    public final static String RUN = "Run";
    public final static String VALIDATE = "Validate";
    public final static String SYNTHESIZE = "Synthesize automaton";
    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
                case RUN:
                    Application.mediator.parseLSA();
                    LSAmatrix m = Application.mediator.matrix;
                    Application.mediator.writeInfo(m.toString());
                    break;
                    
                    
                case VALIDATE:
                    Application.mediator.parseLSA();
                    m = Application.mediator.matrix;
                    String messages = m.validateMatrix();
                    if(messages.equals("")){
                        Application.mediator.writeInfo("Matrix is validated\n");
                    }else{
                        Application.mediator.writeInfo("Some problems with vertexes: \n"
                                + messages );
                    }
                    Application.mediator.writeInfo(m.showRoads());
                    break;
                    
                    
                case SYNTHESIZE:
                    Application.mediator.parseLSA();
                    Application.mediator.synthesizer.findAllConnetions();
                    Application.mediator.writeInfo(Application.mediator.synthesizer.showConnections());
                    break;
        }
    }

}
