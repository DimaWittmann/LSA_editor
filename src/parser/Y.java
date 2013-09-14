package parser;

/**
 *
 * @author wittmann
 */
public class Y extends Operator{
    
    public Y(int pos) {
        super(pos);
        type = Type.Y;
    }

    @Override
    public String toString() {
        return "Y"+id;
    }

    
    
}
