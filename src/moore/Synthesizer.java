package moore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import parser.Parser;

/**
 *
 * @author wittman
 */
public class Synthesizer {
    public Parser parser;
    public int [] posToZid;
    public List<Connection> connetions;
    public int numCond;
    
    public Synthesizer(Parser parser){
        this.parser = parser;
        posToZid = new int [parser.matrix.ids.length];
        connetions = new ArrayList<>();
        
        int i = 0;
        int z = 1;
        numCond = 1;
        for (String id: parser.matrix.ids){
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
    

    public void findAllConnetions(){
        int [][] trans = parser.matrix.transitions;
        for (int i = 0; i < trans.length; i++) {
            if(posToZid[i] >= 0){
                findConnection(i, i, new int[numCond]);
            }
        }
    }
    
    public void findConnection(int from, int curr, int []conds){
        
        int [][] trans = parser.matrix.transitions;
        
        
        for (int i = 0; i < trans[curr].length; i++) {
            
            if(trans[curr][i] > 0){
                if (posToZid[curr] < 0){
                    int index = Math.abs(posToZid[curr]) - 1;
                    conds[index] = trans[curr][i];
                }
                if(posToZid[i] >= 0){
                    Connection conn = new Connection(from, posToZid[i], conds, parser.matrix.ids[from]);
                    System.out.println(conn);
                    connetions.add(conn);
                    conds = Arrays.copyOf(conds, conds.length);
                }else{
                    findConnection(from, i, Arrays.copyOf(conds, conds.length));
                }
            }
        }

    }
    
    public String showConnections(){
        String str = "Conditions:";
        for(String ids: parser.matrix.ids){
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
}
