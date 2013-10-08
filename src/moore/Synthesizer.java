package moore;

import interaction.Application;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import parser.Parser;
import xml.XMLCreator;

/**
 *
 * @author wittman
 */
public class Synthesizer {
    public Parser parser;
    public int [] posToZid;
    public List<Connection> connetions;
    public List<State> states;
    public int numCond;
    
    public Synthesizer(Parser parser){
        this.parser = parser;    
    }
    
    private void initSunthesizer(){
        posToZid = new int [Application.mediator.matrix.ids.length];
        int i = 0;
        int z = 1;
        numCond = 1;
        for (String id: Application.mediator.matrix.ids){
            if(id.charAt(0) == 'X'){
                posToZid[i] = -numCond;
                numCond++;
            }else if(id.charAt(0) == 'S' ||id.charAt(0) == 'E' ){
                posToZid[i] = 0;   
            }else{
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
        connetions = new ArrayList<>();
        states = new ArrayList<>(); 
        
        
        for (int i = 0; i < posToZid.length; i++) {
            if(posToZid[i] >= 0){
                State state = new State(posToZid[i]);
                state.allStates = states;
                if(!states.contains(state)){
                    states.add(state);
                }
            }
        }
        
        
        int [][] trans = Application.mediator.matrix.transitions;
        for (int i = 0; i < trans.length; i++) {
            if(posToZid[i] >= 0){
                findConnection(i, i, new int[numCond]);
            }
        }
    }
    
    public void findConnection(int from, int curr, int []conds){
        
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
                    System.out.println(conn);
                    connetions.add(conn);
                    conds = Arrays.copyOf(conds, conds.length);
                    
                    states.get(states.indexOf(new State(posToZid[from]))).outConnections.add(conn);
                    states.get(states.indexOf(new State(posToZid[i]))).inConnections.add(conn);
                }else{
                    findConnection(from, i, Arrays.copyOf(conds, conds.length));
                }
            }
        }

    }
    
    public String showConnections(){
        String str = "Conditions:";
        for(String ids: Application.mediator.matrix.ids){
            if(ids.charAt(0) == 'X'){
                str += ids+" ";
            }
        }
        str += "\n";
        for (Connection conn: connetions){
            str += conn.toString()+"\n";
        }
        return str;
    }
    
    public void saveToXML(File file){
        XMLCreator xmlP = new XMLCreator(file);
        xmlP.loadData(connetions);
        xmlP.writeToFile();
    }

}


