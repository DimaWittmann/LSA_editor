package moore;

import interaction.Application;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wittman
 */
public class Synthesizer {

    public int [] posToZid;
    public int numCond;
    
    private void initSunthesizer(){
        Application.mediator.automatonTable.conditions.clear();
        Application.mediator.automatonTable.ids.clear();
        
        List<String> conditions = Application.mediator.automatonTable.conditions;
        List<String> ids = Application.mediator.automatonTable.ids;
        
        posToZid = new int [Application.mediator.matrix.ids.length];
        int i = 0;
        int z = 1;
        numCond = 1;
        for (String id: Application.mediator.matrix.ids){
            if(id.charAt(0) == 'X'){
                posToZid[i] = -numCond;
                conditions.add(id);
                numCond++;
            }else if(id.charAt(0) == 'S' ||id.charAt(0) == 'E' ){
                posToZid[i] = 0;   
                if (id.charAt(0) == 'S'){
                    ids.add(0,id);
                }
            }else{
                ids.add(id);
                posToZid[i] = z;
                z++;
            }
            i++;
        }
        numCond -= 1;
        
    }

    /**
     * Знайти всі переходи в графі
     */
    public void findAllConnetions(){
        initSunthesizer();
        Application.mediator.automatonTable.connections.clear();
        int [][] trans = Application.mediator.matrix.transitions;
        for (int i = 0; i < trans.length; i++) {
            if(posToZid[i] >= 0){
                findConnection(i, i, new int[numCond]);
            }
        }
    }
    
    private void findConnection(int from, int curr, int []conds){
        List<Connection> connections = Application.mediator.automatonTable.connections;
        int [][] trans = Application.mediator.matrix.transitions;
        for (int i = 0; i < trans[curr].length; i++) {
            
            if(trans[curr][i] > 0){
                if (posToZid[curr] < 0){
                    int index = Math.abs(posToZid[curr]) - 1;
                    conds[index] = trans[curr][i];
                }
                if(posToZid[i] >= 0){
                    Connection conn = new Connection(posToZid[from], posToZid[i], 
                            conds, Application.mediator.matrix.ids[from]);
                    connections.add(conn);
                    conds = Arrays.copyOf(conds, conds.length);
                    
                }else{
                    findConnection(from, i, Arrays.copyOf(conds, conds.length));
                }
            }
        }

    }    
}


