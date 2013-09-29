package internal_representation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import parser.*;
import static parser.Operator.Type.*;

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
    
        
    /**
     * Генерація графа з матриці
     * @param p - парсер, який зберігає стан поточного графу
     * @return початок графа
     */
    public Operator toGraph(Parser p){
        ArrayList<Operator> operators = new ArrayList<>();
        p.start = null;
        p.first = null;
        
        int i = 0;
        Operator pred = null;
        for(String id:this.ids){
            Operator curr = null;
            switch(id.charAt(0)){
                case 'S':
                    curr = new S(i);
                    p.start = curr;
                    break;
                case 'X':
                    curr = new X(i);
                    break;
                case 'Y':
                    curr = new Y(i);
                    break;
                case 'E':
                    curr = new E(i);
                    break;
            }
            if(p.first == null){
                p.first = curr;
            }
            operators.add(curr);
            curr.pred = pred;
            curr.id = id.substring(1);
            if (pred != null){
                pred.next = curr;
            }
            pred = curr;
            i++;
            
        }
        
        int numJumps = 0;
        pred = null;
        for (Operator curr: operators){
            switch(curr.type){
                case S:
                case Y:
                    Operator step = null;
                    for (int j = 0; j < this.operationalTop.length; j++) {
                        if(this.operationalTop[curr.pos][j] == 1){
                            step = operators.get(j);
                            break;
                        }
                    }
                    if(step != null){
                        if (curr.next() == null || !curr.next().equals(step)){    
                            //додавання бузумовного переходу
                            numJumps++;

                            O out = new O(0);
                            out.id = String.valueOf(numJumps);

                            I in = new I(0);
                            in.id = String.valueOf(numJumps);

                            insertNextOperator(curr, out);
                            out.end = in;

                            insertPredOperator(step, in);
                        }
                    }
                    break;
                case X:
                    
                    Operator stepTrue = null;
                    Operator stepFalse = null;
                    for (int j = 0; j < this.operationalTop.length; j++) {
                        if(this.operationalTop[curr.pos][j] == 1){
                            stepTrue = operators.get(j);
                        }else if(this.operationalTop[curr.pos][j] == 2){
                            stepFalse = operators.get(j);
                        }
                    }
                    
                    if( curr.pos == stepTrue.pos - 1 && curr.pos == stepFalse.pos - 2){
                        
                    }else{
                        numJumps++;

                        O out = new O(0);
                        out.id = String.valueOf(numJumps);
                        insertNextOperator(curr, out);

                        I in = new I(0);
                        in.id = String.valueOf(numJumps);
                        out.end = in;
                        insertPredOperator(stepTrue, in);

                        assert (stepTrue != null);
                        //TODO зробити можливість переходу по false на умову чи безумовний перехід
                    }
                    break;
                case E:

                    break;
                case I:
                    break;
                case O:
                    break;

            }
            pred = curr;
        }
        return p.first;
    }
    
        /**
     * Додати оператор попереду іншого
     * @param left оператор, до якого додається
     * @param center новий елемент
     */
    private void insertNextOperator(Operator left ,Operator center){
        if (left.next != null){
            left.next.pred = center;
            center.next = left.next;
        }else{
            center.next = null;
        }
        left.next = center;
        center.pred = left;
    }
    /**
     * Додати оператор позаду іншого
     * @param right оператор, до якого додається
     * @param center новий елемент
     */
    private void insertPredOperator(Operator right ,Operator center){
        if(right.pred !=  null){
            right.pred.next = center;
            center.pred = right.pred;
        }else{
            center.pred = null;
        }
        right.pred = center;
        center.next = right;
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
            if(ids[i].charAt(0) != 'E' && inputs[i] == 0) {
                messages.add("Hanging  vertex: "+ i);
            }
            if(ids[i].charAt(0) != 'S' && outputs[i] == 0 ){
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

        if(road.contains(curr)){
            road.add(curr);
            while(!road.isEmpty() && curr != road.get(0)){
                road.remove(0);
            }
            roads.add(road);
        }else{
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
    }    
    
    public String showRoads(){
        String result = "";
        getRoads();
        for(List l : this.roads){
            String line = "";
            for(Object node: l){
                line += node + "->";
            }
            line = line.substring(0, line.length()-2);
            result += line + "\n";
        }
        return result;
    }
    
}
