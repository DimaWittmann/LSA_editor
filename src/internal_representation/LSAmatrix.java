package internal_representation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import parser.Operator;
import parser.X;

/**
 * Являє собою матричне представлення алгоритму
 * @author wittmann
 */
public class LSAmatrix {
    public int dimension;
    public ArrayList<Operator> operationalTop;

    public Map<Operator, List<Connection>>  connections;

    public LSAmatrix(int dimension) {
        this.dimension = dimension;
        operationalTop = new ArrayList<>();
        connections = new HashMap();
    }

    
}
