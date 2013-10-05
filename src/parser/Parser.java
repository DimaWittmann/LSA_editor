package parser;
import interaction.Controller;
import internal_representation.LSAmatrix;
import java.util.*;
import static parser.Operator.Type.*;

/**
 *
 * @author wittmann
 */
public class Parser {
    
    public Map<String, String> warnings;
    public Operator start;
    public Operator first;
    //TODO Зробить нагляд за inputArea для оновлення LSA
    public String LSA;
    public LSAmatrix matrix;
    
    public Parser() {
        initWargnings();
    }
    /**
     * Повний аналіз програми і генерація матриці
     * @param text програма
     * @return внутрішнє представлення графа
     * @throws ParseException 
     */
    public LSAmatrix parse(String text) throws ParseException{
        LSA = text;
        first = getTokens(text);
        linkTokens(first);
        matrix = toMatrix(first);
        return matrix;
    }
    /**
     * Повний аналіз програми і генерація матриці, 
     * що зберігається в LSA
     * @return внутрішнє представлення графа
     * @throws ParseException 
     */
    public LSAmatrix parse() throws ParseException{
        if (LSA != null){
            return parse(LSA);
        }else{
            return null;
        }
        
    }
    
    /**
     * 
     * @param text вхідні символи
     * @return токени, послідовно зв'язані один за однис у порядку їх
     * об'явлення в text
     * @throws parser.ParseException
     */
    public Operator getTokens(String text) throws ParseException{
        LSA = text;

        boolean operator = true;
        
        text += " ";  // шоб точно розпарсило останній опратор
        String label = "";
        Operator curr = null; //поточний оператор
        Operator pred = null; // попередній
        Operator last = null;
        first = null;
        start = null;

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
            	boolean nextSw = false;
	            pred = curr;
	            switch(c){
	                case 'S':
	                    curr = new S(pos);
	                    if (start != null){
	                        throw new ParseException(warnings.get("Sduplicate"));
	                    }
	                    start = curr;
	                    break;
	                case 'I':
	                    curr = new I(pos);
	                    pos--;
	                    break;
	                case 'O':
	                    curr = new O(pos);
	                    pos--;
	                    break;
	                case 'X':
	                    curr = new X(pos);
	                    break;
	                case 'Y':
	                    curr = new Y(pos);
	                    break;
	                case 'E' :
	                    if(last != null){
	                        throw new ParseException(warnings.get("Eduplicate"));
	                    }
	                    curr = new E(pos);
	                    last = curr;
	
	                    break;
	                default:
	                	nextSw = true;
	            }
	            
	            if(! nextSw){
	            	if(first == null){
                            first = curr;
	            	}
	            	curr.pred = pred;
		            if (pred != null){
		            	pred.next = curr;
		            }
		            pos++;
		            operator = false;
	            }else{
	            	switch (c){
                            case ' ':
                            case '\n':
                                break;
                            default:
                                throw new ParseException(warnings.get("illegalChar") + c);
                        }
	            }
	        }
        }        
        
        if( start == null){
            throw new ParseException(warnings.get("S not found"));
        }
        if( last == null ){
            throw new ParseException(warnings.get("E not found"));
        }
        return first;
    }
    
    /**
     * Перевірка всьго алгоритму на валідність.
     * Створення зв'язків безумовних і умовних переходів.
     * @param first початковий токен
     * @return старт ЛСА
     * @throws parser.ParseException 
     */
    public Operator linkTokens(Operator first) throws ParseException{
        //TODO Реалізувати можливості декількох виходів для одного входу
        
        ArrayList<O> Oids = new ArrayList<>();
        ArrayList<I> Iids = new ArrayList<>();
        
        Operator curr = first;

        while (curr.next != null){
            switch (curr.type){
                case X:
                    if(curr.next.next == null){
                        throw new ParseException(warnings.get("after X") + curr.toString());
                    }
                    break;
                case O:
                    if (Oids.contains((O)curr)){
                        throw new ParseException(warnings.get("uniqe id") + curr.toString());
                    }else{
                        Oids.add((O)curr);
                        if (((O)curr).end == null){
                            Operator end = findI(((O)curr), Iids);
                            if (end == null){
                                throw new ParseException(warnings.get("Link not found")+ curr.toString());
                            }else{
                                ((I)end).start = (O)curr;
                                ((O)curr).end = (I) end;
                            }
                        }
                    }
                    break;
                case I:
                    
                    if (Iids.contains((I)curr)){
                        throw new ParseException(warnings.get("uniqe id") + curr.toString());
                    }else{
                        Iids.add((I)curr);
                        if (((I)curr).start == null){
                            Operator srt = findO(((I)curr), Oids);
                            if (srt == null){
                                throw new ParseException(warnings.get("Link not found") 
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
        return start;
    }
    
    //TODO Подумати, як краще спростити і з'єднати два методи
    private Operator findI(O O, ArrayList<I> Iids){
        
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
    
    private Operator findO(I I, ArrayList<O> Oids){
        
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
        warnings.put("uniqe id","All ids must be uniqe for same types of operator: ");
        warnings.put("after X", "After X must be at least two operators: ");
        warnings.put("Link not found", "Can`t found end/start for: ");
        warnings.put("E not found", "LSA must be with a one end operator");
        warnings.put("S not found", "LSA must be with a one start operator");
        warnings.put("Sduplicate","Start must be only once");
        warnings.put("Eduplicate","End must be only once");
        warnings.put("illegalChar", "Unknown identifier is present: ");
    }
    /**
     * Генерація матриці переходів з графа і відображення позиції - id.
     * @return LSAmatrix клас преставлення алгоритму
     */
    public LSAmatrix toMatrix(){

        return toMatrix(this.first);

    }
    /**
     * Генерація матриці переходів з графа і відображення позиції - id.
     * @param first перший токен графа
     * @return LSAmatrix клас преставлення алгоритму
     */
    public LSAmatrix toMatrix(Operator first){
        ArrayList<Operator> operational = new ArrayList<>();
        
        Operator curr = first;
        while(curr != null){
            if (curr.type == S||curr.type == Y||curr.type == E||curr.type == X){
                operational.add(curr);
            }
            curr = curr.next;
        }
        LSAmatrix matrix = new LSAmatrix(operational.size());
        
        int i=0;
        for (Operator op : operational){
            matrix.ids[i] = op.type+op.id;
            if (op.type == X){
                matrix.transitions[op.pos][((X)op).next(true).pos] = 1;
                matrix.transitions[op.pos][((X)op).next(false).pos] = 2;
            }else{
                if(op.next() != null){
                    matrix.transitions[op.pos][op.next().pos] = 1;
                }
            }
            i++;
        }
        
        this.matrix = matrix;
        return matrix;
    }
    
    public String createLSA(){
        String text = "";
        Operator curr = first;
        while(curr != null){
            text += " "+curr.toString();
            curr = curr.next;
        }
        return text;
    }

    public static void main(String [] args){
       
        Controller controller = new Controller();
    }
    
}
