package parser;

/**
 *
 * @author wittmann
 */
public class E extends Operator{

    
    public E(int pos) {
        super(pos);
        type = Operator.Type.E;
    }
    
    @Override
    public String toString(){
        return "E";
    }
}
