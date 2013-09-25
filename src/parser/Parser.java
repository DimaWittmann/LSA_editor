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
    public String fileName;
    public String LSA;
    
    public Parser() {
        
        initWargnings();
        start = null;
        LSA = null;
    }
    
    public LSAmatrix parse(String text) throws ParseException{
        LSA = text;
        LSAmatrix matrix = null;
        start = getTokens(text);
        linkTokens(start);
        matrix = toMatrix(start);
        return matrix;
    }
    
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
        System.out.println(text);
        boolean operator = true;
        
        String label = "";
        Operator curr = null; //поточний оператор
        Operator pred = null; // попередній
        Operator first = null;
        Operator last = null;
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
        
        if( first == null){
            throw new ParseException(warnings.get("S not found"));
        }
        if(last == null ){
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
    
    public LSAmatrix toMatrix(Operator start){
        ArrayList<Operator> operational = new ArrayList<>();
        
        Operator curr = start;
        while(curr.pred != null){
        	curr = curr.pred;
        }
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
                matrix.operationalTop[op.pos][((X)op).next(true).pos] = 1;
                matrix.operationalTop[op.pos][((X)op).next(false).pos] = 2;
            }else if(op.type == E){
            }else{
                matrix.operationalTop[op.pos][op.next().pos] = 1;
            }
            
            i++;
        }
        
        return matrix;
    }
    
    /**
     * Генерація графа з матриці
     * @param matrix
     * @return початок графа
     */
    public Operator fromMatrix(LSAmatrix matrix){
        ArrayList<Operator> operators = new ArrayList<>();
        Operator start = null;
        
        int i = 0;
        Operator pred = null;
        for(String id:matrix.ids){
            switch(id.charAt(0)){
                case 'S':
                    start = new S(i);
                    operators.add(start);
                    pred = start;
                    break;
                case 'X':
                    X x = new X(i);
                    operators.add(x);
                    x.pred = pred;
                    x.id = id.substring(1);
                    pred.next = x;
                    pred = x;
                    break;
                case 'Y':
                    Y y = new Y(i);
                    operators.add(y);
                    y.pred = pred;
                    y.id = id.substring(1);
                    pred.next = y;
                    pred = y;
                    break;
                case 'E':
                    E e = new E(i);
                    operators.add(e);
                    pred.next = e;
                    e.pred = pred;
                    break;
            }
            i++;
            
        }
        
        int numJumps = 0;
        pred = null;
        ArrayList<Operator> added = new ArrayList<>();
        for (Operator curr: operators){
            switch(curr.type){
                case S:
                    break;
                case Y:
                    Operator step = null;
                    for (int j = 0; j < matrix.operationalTop.length; j++) {
                        if(matrix.operationalTop[curr.pos][j] == 1){
                            step = operators.get(j);
                            break;
                        }
                    }
                    if (curr.next() != step){    //додавання бузумовного переходу
                        numJumps++;
                    
                        O out = new O(0);
                        out.id = String.valueOf(numJumps);

                        I in = new I(0);
                        in.id = String.valueOf(numJumps);

                        insertNextOperator(curr, out);
                        out.end = in;
                        
                        insertPredOperator(step, in);
                    }
                    
                    break;
                case X:
                    numJumps++;
                    
                    O out = new O(0);
                    out.id = String.valueOf(numJumps);
                    
                    I in = new I(0);
                    in.id = String.valueOf(numJumps);
                    
                    insertNextOperator(curr, out);
                    out.end = in;
                    
                    Operator stepTrue = null;
                    Operator stepFalse = null;
                    for (int j = 0; j < matrix.operationalTop.length; j++) {
                        if(matrix.operationalTop[curr.pos][j] == 1){
                            stepTrue = operators.get(j);
                        }else if(matrix.operationalTop[curr.pos][j] == 2){
                            stepFalse = operators.get(j);
                        }
                    }
                    
                    assert (stepFalse == out.next);   //FIXME not implemented yet
                    assert (stepTrue != null);
                    //TODO зробити можливість переходу по false на умову чи безумовний перехід
                    
                    insertPredOperator(stepTrue, in);
                    
                    break;
                case E:

                    break;
            }
            if(!added.contains(curr)){ //FIXME не використовується
                added.add(curr);
            }
            pred = curr;
        }
        this.start = start;
        return start;
    }
    
    public String createLSA(){
        String text = "";
        Operator curr = start;
        while(curr != null){
            text += " "+curr.toString();
            curr = curr.next;
        }
        return text;
    }
    /**
     * Додати оператор попереду іншого
     * @param left оператор, до якого додається
     * @param center новий елемент
     */
    private void insertNextOperator(Operator left ,Operator center){
        left.next.pred = center;
        center.next = left.next;
        left.next = center;
        center.pred = left;
    }
    /**
     * Додати оператор позаду іншого
     * @param right оператор, до якого додається
     * @param center новий елемент
     */
    private void insertPredOperator(Operator right ,Operator center){
        right.pred.next = center;
        center.pred = right.pred;
        right.pred = center;
        center.next = right;
    }
    
    public static void main(String [] args){
       
        Controller controller = new Controller();
    }
    
}
