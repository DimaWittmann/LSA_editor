/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author wittmann
 */
public class I extends Operator{
    public O start;

    public I(int pos) {
        super(pos);
        this.start = null;
        type = Type.I;
    }
    
    @Override
    public String toString(){
        return "I"+id;
    }

    
    
}
