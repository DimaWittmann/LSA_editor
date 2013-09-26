package internal_representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Являє собою матричне представлення алгоритму
 * @author wittmann
 */
public class LSAmatrix implements Serializable {
    public int dimension;
    public int [][] operationalTop;
    public String [] ids; 
    public List<List<Integer>> roads;
            
    public LSAmatrix(int dimension) {
        this.dimension = dimension;
        operationalTop = new int[dimension][dimension];
        for(int i=0;i<dimension;i++){
            for (int j = 0; j < dimension; j++) {
                operationalTop[i][j] = 0;
            }
        }
        ids = new String[dimension];
    }

    @Override
    public String toString() {
        int rowWidth = 3;
        String result = "";
        while(result.length() < rowWidth){
            result += " ";
        }
        
        for(String st:ids){
            while(st.length() < rowWidth){
                st += " ";
            }
            result += st;
        }
        result += "\n";
        for(int i=0;i<dimension;i++){
            String cell = ids[i];
            while(cell.length() < rowWidth){
                cell += " ";
            }
            result += cell;
            
            for (int j = 0; j < dimension; j++) {
                cell = String.valueOf(operationalTop[i][j]);
                while(cell.length() < rowWidth){
                    cell += " ";
                }
                result += cell;
            }
            result += "\n";
        }
        for(int i=0;i<ids.length;i++){
            result += String.valueOf(i) +": "+ids[i]+"\n";
        }
        return result;
    }
    
    public String validateMatrix(){
        ArrayList<String> messages = new ArrayList<>();
        int [] inputs = new int [dimension];
        int [] outputs = new int [dimension];
        
        for (int i = 0; i < operationalTop.length; i++) {
            for (int j = 0; j < operationalTop[i].length; j++) {
                if (operationalTop[i][j] != 0){
                    inputs[i]++;
                    outputs[j]++;
                }
                
            }
        }
        
        for (int i = 0; i < dimension; i++) {
            if(inputs[i] == 0 && (!"S".equals(ids[i]))) {
                messages.add("Hanging  vertex: "+ i);
            }
            if(outputs[i] == 0 && (!"E".equals(ids[i]))){
                messages.add("Unreachable vertex: "+ i);
            }
        }
        String result = "";
        for(String m : messages){
            result += m + "\n";
        }

        return result;
    }
    
    public void getRoads(){
        roads = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].charAt(0) == 'S'){
                start = i;
                break;
            }
        }
        
        getRoad(start, new ArrayList<Integer>());
    }
    
    private void getRoad(int curr, List<Integer> road ){
        road.add(curr);
        if(ids[curr].charAt(0) == 'X'){
            List<Integer> old_road = new ArrayList<>(road);
            for (int i = 0; i < operationalTop[curr].length; i++) {
                
                if(operationalTop[curr][i] == 1 ){
                    getRoad(i, road);
                }
                if(operationalTop[curr][i] == 2 ){
                    getRoad(i, old_road);
                }
            }
        }else if(ids[curr].charAt(0) == 'E'){
            roads.add(road);
        }else{
            for (int i = 0; i < operationalTop[curr].length; i++) {
                if(operationalTop[curr][i]>0 ){
                    getRoad(i, road);
                }
            }
        }
    }
    
    
    public String showRoads(){
        String result = "";
        getRoads();
        for(List l : this.roads){
            String line = "";
            for(Object node: l){
                line += node + "->";
            }
            result += line + "\n";
        }
        return result;
    }
    
}
