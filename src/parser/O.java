package parser;

/**
 *
 * @author wittmann
 */
public class O extends Operator{
    public I end;
    
    public O(int pos) {
        super(pos);
        this.end = null;
        type = Type.O;
    }
    


    @Override
    public Operator next() {
        return end.next();
    }
    
    
}
