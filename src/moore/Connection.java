package moore;

import java.io.Serializable;

/**
 * Представлення дуги графа Мура
 * @author Wittman
 */
public class Connection implements Serializable{
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.from;
        hash = 83 * hash + this.to;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Connection other = (Connection) obj;
        if (this.from != other.from) {
            return false;
        }
        if (this.to != other.to) {
            return false;
        }
        return true;
    }
 
    
}
