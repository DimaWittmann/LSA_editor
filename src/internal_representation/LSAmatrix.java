package internal_representation;

import java.io.Serializable;

/**
 * Являє собою матричне представлення алгоритму
 * @author wittmann
 */
public class LSAmatrix implements Serializable {
    public int dimension;
    public int [][] operationalTop;
    public String [] ids; 
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
        return result;
    }
    
    
    

    
}
