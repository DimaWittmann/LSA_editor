package moore.compatible_coding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import moore.Connection;

/**
 * Стан в графі автомата
 * @author Wittman
 */
public class ZState {
    
    public List<Connection> toConnections;
    public List<Connection> fromConnections;
    public int id;
    public String signalId;
    public Integer code;
    
    
    public ZState(int id, String signalId){
        this.id = id;
        this.signalId = signalId;
        toConnections = new ArrayList<>();
        fromConnections = new ArrayList<>();
    }
    
    public ZState(ZState state){
        toConnections = new ArrayList<>(state.toConnections);
        fromConnections = new ArrayList<>(state.fromConnections);
        id = state.id;
        signalId = state.signalId;
        code = state.code;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final ZState other = (ZState) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
