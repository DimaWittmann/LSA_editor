/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
