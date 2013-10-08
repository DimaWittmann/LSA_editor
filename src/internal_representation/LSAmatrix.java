package internal_representation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public int [][] transitions;
    public String [] ids; 
    public List<List<Integer>> roads;
            
    
    /**
     * Створення матриці з файлу
     * @param file файл, який був створений викликом savaToFile
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public LSAmatrix(File file) throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        this.ids = (String[]) ois.readObject();
        this.transitions = (int[][]) ois.readObject();
    }
    
    
    /**
     * Створення матриці переходів з графа і відображення позиції - id.
     * @param first перший токен графа
     */
    public LSAmatrix(Operator first){
        
        
        ArrayList<Operator> operational = new ArrayList<>();
        
        Operator curr = first;
        while(curr != null){
            if (curr.type == S||curr.type == Y||curr.type == E||curr.type == X){
                operational.add(curr);
            }
            curr = curr.next;
        }
        int dimension = operational.size();
        
        transitions = new int[dimension][dimension];
        for(int i=0;i<dimension;i++){
            for (int j = 0; j < dimension; j++) {
                transitions[i][j] = 0;
            }
        }
        ids = new String[dimension];
        
        int i=0;
        for (Operator op : operational){
            this.ids[i] = op.type+op.id;
            if (op.type == X){
                this.transitions[op.pos][((X)op).next(true).pos] = 1;
                this.transitions[op.pos][((X)op).next(false).pos] = 2;
            }else{
                if(op.next() != null){
                    this.transitions[op.pos][op.next().pos] = 1;
                }
            }
            i++;
        }
        
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
        for(int i=0;i<transitions.length;i++){
            String cell = ids[i];
            while(cell.length() < rowWidth){
                cell += " ";
            }
            result += cell;
            
            for (int j = 0; j < transitions.length; j++) {
                cell = String.valueOf(transitions[i][j]);
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
        int [] inputs = new int [transitions.length];
        int [] outputs = new int [transitions.length];
        
        for (int i = 0; i < transitions.length; i++) {
            for (int j = 0; j < transitions[i].length; j++) {
                if (transitions[i][j] != 0){
                    inputs[i]++;
                    outputs[j]++;
                }
                
            }
        }
        
        for (int i = 0; i < transitions.length; i++) {
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
                for (int i = 0; i < transitions[curr].length; i++) {

                    if(transitions[curr][i] == 1 ){
                        getRoad(i, road);
                    }
                    if(transitions[curr][i] == 2 ){
                        getRoad(i, old_road);
                    }
                }
            }else if(ids[curr].charAt(0) == 'E'){
                roads.add(road);
            }else{
                for (int i = 0; i < transitions[curr].length; i++) {
                    if(transitions[curr][i]>0 ){
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
    
    
    public void saveToFile(File file) throws FileNotFoundException, IOException{
        ObjectOutputStream oos = null;

        FileOutputStream fos = new FileOutputStream(file);
        oos = new ObjectOutputStream(fos);

        oos.writeObject(ids);
        oos.writeObject(transitions);
        oos.flush();
    }
    
}
