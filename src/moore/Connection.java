package moore;

/**
 * Представлення дуги графа Мура
 * @author Wittman
 */
public class Connection {
    public int from;
    public int to;
    public int[] conditions;
    public String signalId;

    public Connection(int from, int to, int[] conditions, String signalId) {
        this.conditions = conditions;
        this.signalId = signalId;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        String str = "Z"+String.valueOf(from)+" -> Z" +String.valueOf(to)+"| ";
        for (int i : conditions){
            str += String.valueOf(i)+" ";
        }
        str += "|"+signalId;
        
        return str;
    }
 
    
}
