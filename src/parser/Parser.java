package parser;

import internal_representation.Connection;
import internal_representation.LSAmatrix;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static parser.Operator.Type.*;

/**
 *
 * @author wittmann
 */
public class Parser {
    
    public Map<String, String> warnings;

    ArrayList<Y> Yids;
    ArrayList<X> Xids;
    ArrayList<I> Iids;
    ArrayList<O> Oids;
    
    public Parser() {
        this.Oids = new ArrayList<>();
        this.Iids = new ArrayList<>();
        this.Xids = new ArrayList<>();
        this.Yids = new ArrayList<>();
        initWargnings();
    }
    
    
    /**
     * 
     * @param text вхідні символи
     * @return токени, послідовно зв'язані один за однис у порядку їх
     * об'явлення в text
     */
    public Operator getTokens(String text){
        boolean operator = true;
        String label = "";
        Operator curr = null; //поточний оператор
        Operator pred = null; // попередній
        Operator first = null;
        int pos = 0;
        
        for (int i=0;i<text.length();i++){
            char c = text.charAt(i);
            
            if(!operator){
                if (c != ' ' && c != '\n'){
                    label += c;
                }else{
                    operator = true;
                    curr.id = label;
                    label = "";
                }
            }else{
                //TODO позбавитися повторюваності коду
                switch(c){
                    
                    case 'S':
                        curr = new S(pos);
                        if (first != null){
                            System.err.println("Start must be only once");
                        }
                        first = curr;
                        pos++;
                        break;
                    case 'I':
                        
                        pred = curr;
                        curr = new I(pos);
                        curr.pred = pred;
                        pred.next = curr;
                        
                        pos++;
                        operator = false;
                        break;
                    case 'O':
                        
                        pred = curr;
                        curr = new O(pos);
                        curr.pred = pred;
                        pred.next = curr;
                        
                        pos++;
                        operator = false;
                        break;
                    case 'X':
                        
                        pred = curr;
                        curr = new X(pos);
                        curr.pred = pred;
                        pred.next = curr;
                        
                        pos++;
                        operator = false;
                        break;
                    case 'Y':
                        
                        pred = curr;
                        curr = new Y(pos);
                        curr.pred = pred;
                        pred.next = curr;
                        
                        pos++;
                        operator = false;
                        break;
                    case 'E' :
                        pred = curr;
                        curr = new E(pos);
                        curr.pred = pred;
                        pred.next = curr;
                        return first;                
                    
                }
            }
            
        }        
        //Якщо ми сюди дійшли - то відсутній оператор кінця
        System.err.println(warnings.get("E not found"));
        return null;
    }
    
    /**
     * Перевірка всьго алгоритму на валідність.
     * Створення зв'язків безумовних і умовних переходів. 
     * @param start початковий токен
     */
    public void linkTokens(Operator start){
        //TODO Реалізувати можливості декількох виходів для одного входу
        
        if(start.type != Operator.Type.S){
            System.err.println(warnings.get("firstS"));
        }
        
        Operator curr = start;

        while (curr.next != null){
            
            
            switch (curr.type){
                case E:
                case S:
                    break;
                case Y:
                    if (Yids.contains((Y)curr)){
                        System.err.println(warnings.get("uniqe id")+ curr.toString());
                    }else{
                        Yids.add((Y)curr);
                    }
                    break;
                case X:
                    if (Xids.contains((X)curr)){
                        System.err.println(warnings.get("uniqe id") + curr.toString());
                    }else if(curr.next.next == null){
                        System.err.println(warnings.get("after X") + curr.toString());
                    }else{
                        Xids.add((X)curr);
                    }
                    break;
                case O:
                    if (Oids.contains((O)curr)){
                        System.err.println(warnings.get("uniqe id") + curr.toString());
                    }else{
                        Oids.add((O)curr);
                        if (((O)curr).end == null){
                            Operator end = findI(((O)curr));
                            if (end == null){
                                System.err.println(warnings.get("Link not found") 
                                        + curr.toString());
                            }else{
                                ((I)end).start = (O)curr;
                                ((O)curr).end = (I) end;
                            }
                        }
                    }
                    break;
                case I:
                    
                    if (Iids.contains((I)curr)){
                        System.err.println(warnings.get("uniqe id") + curr.toString());
                    }else{
                        Iids.add((I)curr);
                        if (((I)curr).start == null){
                            Operator srt = findO(((I)curr));
                            if (srt == null){
                                System.err.println(warnings.get("Link not found") 
                                        + curr.toString());
                            }else{
                                ((O)srt).end = (I)curr;
                                ((I)curr).start = (O) srt;
                            }
                        }
                    }
                    break;
            }
            curr = curr.next;
            
        }
        
    }
    
    //TODO Подумати, як краще спростити і з'єднати два методи
    private Operator findI(O O){
        
        for(I I:Iids){
            if(I.id.equals(O.id)){
                return I;
            }
        }
        
        Operator curr = O.next;
        
        while(curr != null){
            if(curr.type == Operator.Type.I && O.id.equals(curr.id)){
                return curr;
            }
            curr = curr.next;
        }
        return null;
    }
    
    private Operator findO(I I){
        
        for(O O:Oids){
            if(O.id.equals(I.id)){
                return O;
            }
        }
        
        Operator curr = I.next;
        
        while(curr != null){
            if(curr.type == Operator.Type.O && I.id.equals(curr.id)){
                return curr;
            }
            curr = curr.next;
        }
        
        return null;
    }
    
    
    /**
     * Ініціалізація словника попереджень і помилок
     */
    private void initWargnings(){
        warnings = new HashMap<>();
        warnings.put("firstS","First operator must be S");
        warnings.put("uniqe id","All ids must be uniqe for same types of operator ");
        warnings.put("after X", "After X must be at least two operators ");
        warnings.put("Link not found", "Can`t found end/start for");
        warnings.put("E not found", "LSA must end with a E");
    }
    
    public LSAmatrix toMatrix(Operator start){
        ArrayList<Operator> operational = new ArrayList<>();
        
        Operator curr = start;
        while(curr.next != null){
            if (curr.type == S||curr.type == Y||curr.type == E){
                    operational.add(curr);
            }
            curr = curr.next;
        }
        
        LSAmatrix matrix = new LSAmatrix(operational.size());
        matrix.operationalTop = new ArrayList<>(operational);
        
        for (int i=0; i< operational.size();i++){
            curr = operational.get(i);
            Operator next = curr.next;
            
            switch(next.type){ 
                case Y:
                    addConnection(matrix, curr, new Connection((Y)next));                    
                    break;
                case X:
                    
                    break;
                
            }
        }
        
        return null;
    }
    
    private void findRoad(LSAmatrix matrix, Y from, Operator curr,
            ArrayList<X> predX, ArrayList<Boolean> predInvert){

        Operator next = curr.next;
        switch(next.type){
            case Y:
                Connection conn = new Connection((Y) next);
                conn.invert = new ArrayList<>(predInvert);
                conn.logicalOp = new ArrayList<>(predX);

                addConnection(matrix, curr, conn);
                break;
            case X:
                
                predX.add((X)next);
                predInvert.add(Boolean.TRUE);
                findRoad(matrix, from, next, predX , predInvert);
                
                
                break;
        }
    }
    /**
     * Додавання з'єднання до матриці
     * @param matrix
     * @param to
     * @param conn 
     */
    private void addConnection(LSAmatrix matrix, Operator to, Connection conn){
        if (matrix.connections.containsKey(to)){
                matrix.connections.get(to).add(conn);
            }else{
                matrix.connections.put(to, new ArrayList<Connection>());
                matrix.connections.get(to).add(conn);
            }
    }
    
    public static void main(String [] args){
        
        Parser p = new Parser();
        String text = "S Y1 X1 O1 Y2 O2 I1 X2 O3 Y3 O4 I3 I2 Y4 I4 E";
        Operator start = p.getTokens(text);
        p.linkTokens(start);
        Operator curr = start;
        
        
        while(curr != null){
            System.out.println(curr);
            curr = curr.next();
        }
    }
    
}
