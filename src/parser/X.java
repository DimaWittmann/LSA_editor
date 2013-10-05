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
        Operator next;
        if (cond){
            next = this.next;
        }else{
            next = this.next.next;
        }
        
        if(next.isConnection()){
            return next.next();
        }else{
            return next;
        }
        
    }

    @Override
    public boolean isConnection() {
        return false;
    }
}
