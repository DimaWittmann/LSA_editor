package interaction.menu_listeners;

import interaction.Application;
import internal_representation.AutomatonTable;
import internal_representation.LSAmatrix;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import moore.Connection;
import moore.compatible_coding.ConnectionAnalyzer;
import moore.compatible_coding.ZState;

/**
 *
 * @author Wittman
 */
public class RunMenuListener implements ActionListener{

    public final static String RUN = "Run";
    public final static String VALIDATE = "Validate";
    public final static String SYNTHESIZE = "Synthesize automaton";
    public final static String ANALYZE = "Analyze";
    
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


            case ANALYZE:
                Map<Integer,ZState> states = ConnectionAnalyzer.createStates(Application.mediator.automatonTable.connections);
                
                //TODO зробити по людські
                try {
                    ConnectionAnalyzer.encode(states);
                } catch (Exception ex) {
                    Logger.getLogger(RunMenuListener.class.getName()).log(Level.SEVERE, null, ex);
                    Application.mediator.writeInfo(ex.getMessage());
                }
                AutomatonTable table = ConnectionAnalyzer.getConnections(states);
                
                Application.mediator.automatonTable.connections = table.connections;
                Application.mediator.automatonTable.ids = table.ids;
                Application.mediator.automatonTable.codes = table.codes;
                Application.mediator.automatonFrame.panel.initPanels();
                break;
        }
    }

}
