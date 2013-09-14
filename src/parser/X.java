/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author wittmann
 */
public class X extends Operator{

    public Boolean signal;
    
    public X(int pos) {
        super(pos);
        signal = true;
        type = Type.X;
    }

    @Override
    public String toString() {
        return "X"+id;
    }

    @Override
    public Operator next() {
        if (signal){
            return next;
        }else{
            return next.next;
        }
        
    }

    
    
    
    
    
}
