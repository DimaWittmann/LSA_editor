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
            if(next.isConnection()){
                return next.next();
            }else{
                return next;
            }
        }else{
            return next.next;
        }
        
    }

    @Override
    public boolean isConnection() {
        return false;
    }
}
