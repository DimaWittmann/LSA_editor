package internal_representation.minimizator;

/**
 *
 * @author wittman
 */
public class BiNode {
    public BiNode father;
    public BiNode [] children;

    
    public String value;
    public LogicElement value_type;
    
    private int odd = 0;

    public BiNode(String value, LogicElement value_type) {
        this.value = value;
        this.value_type = value_type;
        this.children = new BiNode[2];

    }
    
    public BiNode addChild(BiNode child){
        if(this.value_type.compareTo(child.value_type) < 0){
            child.father = this.father;
            return child.addChild(this);
        }else{
            if(children[0] == null){
                children[0] = child;
                child.father = this;
            }else
            if(children[1] == null){
                children[1] = child;
                child.father = this;
            }else{
                
                if(this.value_type.compareTo(child.value_type) == 0){
                    child.addChild(children[1]);
                    child.father = this;
                    children[1] = child;
                }else{
                    children[1] = children[1].addChild(child);
                }
                //TODO реалізувати складання оптимальної суперпозиції
                odd ^= 1;
            }
            return this;
        }
    }

    @Override
    public String toString() {
        String result = "";
        if(children[0]!=null){
            result += "(" + children[0].toString() + ")";   
        }
        
        result += this.value;
        
        if(children[1]!=null){
            result += "(" + children[1].toString() + ")";   
        }
        return result;
    }
    
    
    
    
}
