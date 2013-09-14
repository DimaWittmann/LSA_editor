/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public String toString(){
        return "O"+id;
    }

    @Override
    public Operator next() {
        return end;
    }
    
    
}
