
package parser;

import GUI.algorithm.OpPanel;
import java.util.Objects;

/**
 * Основа всіх операторів ЛСА
 * @author wittmann
 */
public abstract class Operator {
    public Operator next;
    public int pos;
    public Operator pred;
    public String id;
    public Type type;
    public OpPanel panel;

    //TODO додати в конструктор обов'язкове зазначання id Operator(int pos, String id)
    public Operator(int pos) {
        this.id = null;
        this.pred = null;
        this.id = null;
        this.next = null;
        this.pos = pos;
    }
    
    public enum Type {S, I, O, X, Y, E};

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if ( this.getClass() == obj.getClass()){
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
        if(next == null){
            return null;
        }
        if(next.type == Type.S){
            return null;
        }
        if(next.type == Type.O || next.type == Type.I){
            return next.next();
        }else{
            return next;
        }
    }
    @Override
    public String toString() {
        return type.toString()+id;
    }
    
    public abstract boolean isConnection();
}

