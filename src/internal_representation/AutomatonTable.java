package internal_representation;

import interaction.Application;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import moore.Connection;
import moore.compatible_coding.SynchroState;

/**
 *
 * @author Wittman
 */
public class AutomatonTable extends AbstractTableModel 
                            implements Serializable{
    public List<Connection> connections;
    public List<String> conditions;
    public List<String> ids;
    public List<Point> points;
    public List<boolean[]> codes;
    public List<boolean[]> J;
    public List<boolean[]> K;
    
    
    public AutomatonTable() {
        //TODO зробити красиве створення і ініціалізацію в одному місці
        connections = new ArrayList<>();
        conditions = new ArrayList<>();
        ids = new ArrayList<>();
        points = new ArrayList<>();
    } 

    public String getConnectionsInfo(){
        String str = "Conditions:";
        for(String id: conditions){
            str += id+" "; 
        }
        str += "\n";
        
        for(int i=0;i<connections.size();i++){
            Connection conn = connections.get(i);
            str += "Z"+String.valueOf(conn.from)+" -> Z" +String.valueOf(conn.to)+"| ";
            for (int c : conn.conditions){
                str += String.valueOf(c)+" ";
            }
            str += "|" + SynchroState.codeToString(codes.get(conn.from));
            if(conn.signalId != null){
                str += "|" + conn.signalId;
            }else{
                str += "|" + 0;
            }
            str += "\n";
        }
        
        return str;
    }

    @Override
    public int getRowCount() {
        return connections.size();
    }

    @Override
    public int getColumnCount() {
        if( J == null || K == null){ 
            return 5;
        }else{
            return 5 + J.get(0).length;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Connection conn = connections.get(rowIndex);
        String str = "";
        switch(columnIndex){
        case 0:
            
            str = "Z" + conn.from + "->" + "Z"+conn.to;
            return str;
            
        case 1:
            str = SynchroState.codeToString(codes.get(conn.from));
            return str;
        
        case 2:
            str = SynchroState.codeToString(codes.get(conn.to));
            return str;
            
        case 3:
            for(int i=0;i<conn.conditions.length;i++){
                str += " " + conn.conditions[i] + " ";
            }
            return str;
            
        case 4:
            return conn.signalId;
            
        default:
            int i = columnIndex - 4;
            i = J.get(0).length - i;
            return (J.get(rowIndex)[i]?"1":"0")+"   "+(K.get(rowIndex)[i]?"1":"0");
        }

    }

    @Override
    public String getColumnName(int column) {
     
        switch(column){
            case 0:
                return "Transit";
            case 1:
                return "Start";
            case 2: 
                return "Destination";
            case 3:
                String str = "";
                for(String cond : conditions){
                    str += cond + " ";
                }
                return str;
            case 4:
                return "Signal";
                
            default :
                return "J"+(J.get(0).length-(column-5))+" K"+(J.get(0).length-(column-5));
        }

    }
    
    public void codeTrigrers(){
        if(codes == null){
            Application.mediator.writeInfo("Automaton isn`t coded");
            return;
        }
        
        J = new ArrayList<>();
        K = new ArrayList<>();
        
        for (int i = 0; i < connections.size(); i++) {
            int from = connections.get(i).from;
            int to = connections.get(i).to;
            boolean []jtriger = new boolean[codes.get(0).length];
            boolean []ktriger = new boolean[codes.get(0).length];
            for (int j = 0; j < codes.get(from).length; j++) {
                if(codes.get(from)[j] == false){
                    jtriger[j] = codes.get(to)[j];
                }else{
                    ktriger[j] = !codes.get(to)[j];
                }
            }
            J.add(jtriger);
            K.add(ktriger);
        }
    }

       
}
