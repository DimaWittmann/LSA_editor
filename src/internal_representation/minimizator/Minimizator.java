package internal_representation.minimizator;

import internal_representation.AutomatonTable;
import internal_representation.TrigerState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wittman
 */
public class Minimizator {
    
    public List<TrigerState[]>signals;
    public List<String> ids;
    public List<TrigerState[]> J;
    public List<TrigerState[]> K;
    public Map<String, List<TrigerState[]>> functions;
    public Map<String, List<TrigerState[]>> minimize_functions;

    public Minimizator(AutomatonTable table) {
        signals= new ArrayList<>();
        ids= new ArrayList<>();
        J = table.J;
        K = table.K;
        
        int places;
        try{
            places = table.connections.get(0).conditions.length + table.codes.get(0).length;
        }catch(NullPointerException e){
            System.err.println("Automaton table must be filled");
            return;
        }
       
        for (int j = table.codes.get(0).length-1, k=0; j >= 0; j--, k++) {
            ids.add("Q"+j);
        }
        ids.addAll(table.conditions);
        
        
        
        for (int i = 0; i < table.connections.size(); i++) {
            TrigerState [] new_signals = new TrigerState[places];
            
            
            boolean[] code = table.codes.get(table.connections.get(i).from);
            for (int j = code.length-1, k=0; j >= 0; j--, k++) {
                if(code[j]){
                    new_signals[k] = TrigerState.ONE;
                }else{
                    new_signals[k] = TrigerState.ZERO;
                }
            }
            int offset = code.length;
                    
            int[] conditions = table.connections.get(i).conditions;
            for (int j = 0; j < conditions.length; j++) {
                if(conditions[j] == 0){
                    new_signals[offset+j] = TrigerState.DOES_NOT_MATTER;
                }
                if(conditions[j] == 1){
                    new_signals[offset+j] = TrigerState.ONE;
                }
                if(conditions[j] == 2){
                    new_signals[offset+j] = TrigerState.ZERO;
                }
            }
            signals.add(new_signals);
        }
        
    }

    public static String showTable(List<String> ids ,List<TrigerState[]> signals){
        String result = "";
        for (int i = 0; i < ids.size(); i++) {
            result += ids.get(i) +" ";
        }
        result += "\n";
        
        for (int i = 0; i < signals.size(); i++) {
            for (int j = 0; j < signals.get(i).length; j++) {
                result += signals.get(i)[j] + "  ";
            }
            result += "\n";
        }
        return result;
    }
    
    public void generateFunctions(){
        functions = generateFunctions(TrigerState.ONE);
    }
    
    public Map<String, List<TrigerState[]>> generateFunctions(TrigerState condition){
        Map<String, List<TrigerState[]>> tmp_functions = new HashMap<>();
        
        for (int j = 0; j < J.get(0).length; j++) {
            String id = "J"+(j+1);
            List<TrigerState[]> true_codes = new ArrayList<>();
            for (int i = 0; i < J.size(); i++) {
                if(J.get(i)[j] == condition){
                    true_codes.add(Arrays.copyOf(signals.get(i), signals.get(i).length));
                }
            }
            tmp_functions.put(id, true_codes);
        }
        

        for (int j = 0; j < K.get(0).length; j++) {
            String id = "K"+(j+1);
            List<TrigerState[]> true_codes = new ArrayList<>();
            for (int i = 0; i < K.size(); i++) {
                if(K.get(i)[j] == condition){
                    true_codes.add(Arrays.copyOf(signals.get(i), signals.get(i).length));
                }
            }
            tmp_functions.put(id, true_codes);
            
        }
        
       return tmp_functions;
    }
    
    public static String showFunctions(Map<String, List<TrigerState[]>> functions, List<String> ids){
        String str = "";
        
        for (Map.Entry<String, List<TrigerState[]>> entry : functions.entrySet()) {
            String id = entry.getKey();
            List signal = entry.getValue();
            str += id + "=";
            
            for (int i = 0; i < signal.size(); i++) {
                TrigerState[] state = (TrigerState[]) signal.get(i);
                for (int j = 0; j < state.length; j++) {
                    if(state[j] == TrigerState.ONE){
                        str += ids.get(j);
                    }
                    if(state[j] == TrigerState.ZERO){
                        str += "~"+ids.get(j);
                    }
                }
                if(i != signal.size()-1){
                    str += "|";
                }
            }
            str += "\n";
            
        }
        
        return str;
    }
    
    public void minimize(){
        minimize_functions = new HashMap<>();
        for (Map.Entry<String, List<TrigerState[]>> entry : functions.entrySet()) {
            String string = entry.getKey();
            List<TrigerState[]> list = entry.getValue();
            List<TrigerState[]> new_list = new ArrayList<>();
            for (TrigerState[] trigerStates : list) {
                new_list.add(Arrays.copyOf(trigerStates, trigerStates.length));
            }
            minimize_functions.put(string, new_list);
        }
        Map<String, List<TrigerState[]>> undef_functions = generateFunctions(TrigerState.DOES_NOT_MATTER);
        
        for (String id : functions.keySet()) {

            List<TrigerState[]> signal = minimize_functions.get(id);
            List<TrigerState[]> dom_signal = undef_functions.get(id);
            
            boolean flag = true;
            while (flag) {            
                flag = false;
                for (int i = 0; i < signal.size(); i++) {
                    for (int j = 0; j < signal.size(); j++) {
                        if(i != j){
                            int index = -1;
                            for (int k = 0; k < signal.get(j).length; k++) {
                                if(signal.get(i)[k] != signal.get(j)[k]){
                                    if(index == -1){
                                        index = k;
                                    }else{
                                        index = -2;
                                        break;
                                    }
                                }
                            }
                            if(index >= 0){
                                signal.get(i)[index] = TrigerState.DOES_NOT_MATTER;
                                signal.remove(j);
                                
                                flag = true;
                            }
                            
                        }
                    }
                    
                    
                    for (int j = 0; j < dom_signal.size(); j++) {
                        int index = -1;
                        for (int k = 0; k < dom_signal.get(j).length; k++) {
                            if(signal.get(i)[k] != dom_signal.get(j)[k]){
                                if(index == -1){
                                    index = k;
                                }else{
                                    index = -2;
                                    break;
                                }
                            }
                        }
                        if(index >= 0){
                            flag = true;

                            signal.get(i)[index] = TrigerState.DOES_NOT_MATTER;
                            dom_signal.remove(j);
                        }
                    }
                    
                }
                //прибрати повторення коду
                
                for (int i = 0; i < dom_signal.size(); i++) {
                    for (int j = 0; j < signal.size(); j++) {
                        if(i != j){
                            int index = -1;
                            for (int k = 0; k < signal.get(j).length; k++) {
                                if(dom_signal.get(i)[k] != signal.get(j)[k]){
                                    if(index == -1){
                                        index = k;
                                    }else{
                                        index = -2;
                                        break;
                                    }
                                }
                            }
                            if(index >= 0){
                                dom_signal.get(i)[index] = TrigerState.DOES_NOT_MATTER;
                                dom_signal.remove(j);
                                
                                flag = true;
                            }
                            
                        }
                    }
                    
                    
                    for (int j = 0; j < dom_signal.size(); j++) {
                        int index = -1;
                        for (int k = 0; k < dom_signal.get(j).length; k++) {
                            if(dom_signal.get(i)[k] != dom_signal.get(j)[k]){
                                if(index == -1){
                                    index = k;
                                }else{
                                    index = -2;
                                    break;
                                }
                            }
                        }
                        if(index >= 0){
                            flag = true;

                            dom_signal.get(i)[index] = TrigerState.DOES_NOT_MATTER;
                            dom_signal.remove(j);
                        }
                    }
                    
                }

            }
        }
        
        
    }
    
    private class Parameters{
        public int inputs = 0;
        public int outputs = 0;
        public int elements = 0;

        @Override
        public String toString() {
            String str = "Inputs: "+ inputs + "\n Outputs: "+outputs+"\n Elements: "+ elements;
            return str;
        }
        
        
    }
    
    private Parameters analyzeFunction(Map<String, List<TrigerState[]>> func){
        Parameters parameters = new Parameters();
        
        parameters.outputs += func.size();
        for (Map.Entry<String, List<TrigerState[]>> entry : func.entrySet()) {
            String id = entry.getKey();
            List<TrigerState[]> signal = entry.getValue();
            parameters.elements += signal.size();
            if(signal.size() >1 ){
                parameters.elements++;
            }
            for (TrigerState[] state : signal) {
                for(TrigerState s : state){
                    if(s != TrigerState.DOES_NOT_MATTER){
                        parameters.inputs++;
                    }
                }
            }
        }
        
        return parameters;
    }
    
    public String analyzeMinimization(){
        String str = "";
        Parameters old_param = analyzeFunction(functions);
        str += "Start parameters\n";
        str += old_param.toString()+"\n";
        
        Parameters new_param = analyzeFunction(minimize_functions);
        str += "Minimized parameters\n";
        str += new_param.toString()+"\n";
        
        return str;
    }
    
    public String ganerateVHDL(){
        String tamplate = 
                        "library ieee\n" +
                        "use ieee.std_logic_1164.all;\n" +
                        
                        "entity KC is\n" +
                        "port(  	"+
                               "%s" +
                        ");\n" +
                        "end KC;\n" +
                        
                        "architecture arch of KC is\n" +
                        "begin\n" +
                        "process(" +
                        "%s" +
                        ")\n" +
                        "begin\n" +
                        "%s\n" +
                        "end process;\n" +
                        "end arch;"
                ;
        
        
        String ports = "";
        for(int i=0; i<ids.size();i++){
            ports += ids.get(i) + ": in std_logic";
            if(i != ids.size() - 1){
                ports += ";";
            }
            ports += "\n";
            
            if(ids.get(i).charAt(0) ==  'Q'){
                ports += ids.get(i) + "_out: out std_logic";
                if(i != ids.size() - 1){
                    ports += ";";
                }
                ports += "\n";
            }
            
        }
        
        String signal = "";
        for(int i=0; i<ids.size();i++){
            signal += ids.get(i);
            if(i < ids.size()-1){
                signal += ", ";
            }else{
                signal += " ";
            }
        }
        
        String cirсuit = "";
        for (Map.Entry<String, List<TrigerState[]>> entry : minimize_functions.entrySet()) {
            String string = entry.getKey();
            List<TrigerState[]> list = entry.getValue();
            String line = generateTerm(string, minimize_functions.get(string));
            cirсuit += line + ";\n";
        }
        
        return String.format(tamplate, ports, signal, cirсuit);
    }
    
    public String generateTerm(String id, List<TrigerState[]> signal){
        BiNode start = null;
        for (int i=0;i<signal.size();i++) {
            int count_and = 0;
            for (int j = 0; j < signal.get(i).length; j++) {
                
                if(signal.get(i)[j] != TrigerState.DOES_NOT_MATTER){
                    if(start  == null){
                        if (signal.get(i)[j] == TrigerState.ONE){
                            start = new BiNode(ids.get(j), LogicElement.SIGNAL);
                        }else{
                            start = new BiNode("not "+ids.get(j), LogicElement.SIGNAL);
                        }
                        count_and++;
                    }else{
                        
                        if(count_and != 0){
                            start = start.addChild(new BiNode("and", LogicElement.AND));
                        }
                        if (signal.get(i)[j] == TrigerState.ONE){
                            start = start.addChild(new BiNode(ids.get(j), LogicElement.SIGNAL));
                            
                        }else{
                            start = start.addChild(new BiNode("not "+ids.get(j), LogicElement.SIGNAL));
                        }
                        count_and++;
                        
                    }
                    
                    
                }
                
            }
            
            if(i < signal.size() - 1){
                start = start.addChild(new BiNode("or", LogicElement.OR));
            }
            
        }
        
        
        String result = "";
        result += id + " <= " + start.toString();
        
        return result;
    }
}
