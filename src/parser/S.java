/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

/**
 *
 * @author wittmann
 */
public class S extends Operator{

    
    public S(int pos) {
        super(pos);
        pred = null;
        type = Type.S;
    }
    
    @Override
    public String toString(){
        return "S";
    }
    
}
