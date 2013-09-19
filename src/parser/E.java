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
    public String toString(){
        return this.pos + ": E";
    }

    @Override
    public Operator next() {
        return null;
    }
    
    
}
