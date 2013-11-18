package interaction.menu_listeners;

import interaction.Application;
import internal_representation.AutomatonTable;
import internal_representation.LSAmatrix;
import internal_representation.minimizator.Minimizator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public final static String ADJACENT = "Adjacent coding";
    public final static String CODE_TRIGERS = "Encode trigers";
    public final static String MINIMIZE = "Minimaze trigers";
    
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
                Application.mediator.writeInfo(Application.mediator.automatonTable.getConnectionsInfo());
                break;


            case ADJACENT:
                Map<Integer,ZState> states = ConnectionAnalyzer.createStates(Application.mediator.automatonTable.connections);
                
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
                
                Application.mediator.showConnections();
                break;
                
                
            case CODE_TRIGERS:
                Application.mediator.automatonTable.codeTrigrers();
                Application.mediator.tableFrame = null;
                Application.mediator.writeInfo("Trigers encoded");
                break;
                
            case MINIMIZE:
                Minimizator minimizator = new Minimizator(Application.mediator.automatonTable);
                Application.mediator.writeInfo(Minimizator.showTable(minimizator.ids, minimizator.signals));
                minimizator.generateFunctions();
                Application.mediator.writeInfo("Initial version:");
                Application.mediator.writeInfo(Minimizator.showFunctions(minimizator.functions, minimizator.ids));
                minimizator.minimize();
                Application.mediator.writeInfo("Minimized version:");
                Application.mediator.writeInfo(Minimizator.showFunctions(minimizator.minimize_functions, minimizator.ids));
                Application.mediator.writeInfo("Result:");
                Application.mediator.writeInfo(minimizator.analyzeMinimization());
                break;
        }
    }

}
