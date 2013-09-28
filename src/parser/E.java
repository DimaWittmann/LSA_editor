package parser;

/**
 *
 * @author wittmann
 */
public class E extends Operator{

    
    public E(int pos) {
        super(pos);
        type = Operator.Type.E;
        id ="";
    }

    @Override
    public Operator next() {
        return null;
    }
    @Override
    public boolean isConnection() {
        return false;
    }
    
}
