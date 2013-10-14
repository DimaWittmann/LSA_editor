package internal_representation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import moore.Connection;

/**
 *
 * @author Wittman
 */
public class AutomatonTable {
    public List<Connection> connections;
    public List<String> conditions;
    public List<String> ids;
    public List<Point> points;
    
    public AutomatonTable() {
        connections = new ArrayList<>();
        conditions = new ArrayList<>();
        ids = new ArrayList<>();
        points = new ArrayList<>();
    } 
}
