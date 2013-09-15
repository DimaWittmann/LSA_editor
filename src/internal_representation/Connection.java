package internal_representation;

import java.util.ArrayList;
import parser.X;
import parser.Y;

/**
 *
 * @author wittmann
 */
public class Connection {

    public ArrayList<X> logicalOp;
    public ArrayList<Boolean> invert;
    public Y destination;

    public Connection(Y dest){
        logicalOp = new ArrayList<>();
        invert = new ArrayList<>();
        this.destination = dest;
    }
}
