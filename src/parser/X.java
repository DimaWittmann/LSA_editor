package parser;

/**
 * 
 * @author wittmann
 */
public class X extends Operator{
    
    public X(int pos) {
        super(pos);
        type = Type.X;
    }
    
    @Override
    public Operator next() {
        return next(true);
    }
    
    public Operator next(boolean cond) {
        if (cond){
            if(next.type == Type.O || next.type == Type.I){
                return next.next();
            }else{
                return next;
            }
        }else{
            return next.next;
        }
        
    }
}
