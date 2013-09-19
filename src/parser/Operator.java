
package parser;

import java.util.Objects;

/**
 * Основа всіх операторів ЛСА
 * @author wittmann
 */
public class Operator {
    public Operator next;
    public int pos;
    public Operator pred;
    public String id;
    public Type type;

    public Operator(int pos) {
        this.id = null;
        this.pred = null;
        this.id = null;
        this.next = null;
        this.pos = pos;
    }
    
    enum Type {S, I, O, X, Y, E};

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if ( obj.getClass() == obj.getClass()){
            if(this.id.equals(((Operator)obj).id) && this.pos == ((Operator)obj).pos){
                return true;
            }
        }
        return false;
        
    }


    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.type);
        return hash;
    }
    
    public Operator next() {
        if(next.type == Type.O || next.type == Type.I){
            return next.next();
        }else{
            return next;
        }
    }
    
}

