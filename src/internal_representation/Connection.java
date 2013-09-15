package internal_representation;

import java.util.ArrayList;
import parser.Operator;
import parser.X;

/**
 *
 * @author wittmann
 */
public class Connection {

    public ArrayList<X> logicalOp;
    public ArrayList<Boolean> invert;
    public Operator destination;

    public Connection(Operator dest){
        logicalOp = new ArrayList<>();
        invert = new ArrayList<>();
        this.destination = dest;
    }
}
