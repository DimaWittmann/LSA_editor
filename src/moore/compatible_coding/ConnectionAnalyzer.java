package moore.compatible_coding;

import internal_representation.AutomatonTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import moore.Connection;


/**
 * Методами класу можна проводити аналіз і оптимізацію
 * зв'язків в графі автомату
 * @author Wittman
 */
public class ConnectionAnalyzer {

    //TODO розробити оптимізацію графу

    
    /**
     * Створити з зв'язків стани графа
     * @param connections
     * @return 
     */
    public static Map<Integer, ZState> createStates(List<Connection> connections){
        Map<Integer, ZState> states = new HashMap<>();
        
        for(Connection connection: connections){
            if(!states.containsKey(connection.from)){
                states.put(connection.from, new ZState(connection.from, connection.signalId));
            }
        }
        for(Connection connection: connections){
            states.get(connection.from).fromConnections.add(connection);
            states.get(connection.to).toConnections.add(connection);
        }
        return states;
    }
    
    
    /**
     * Ініціалізувати матрицю переходів графа
     * @param states
     * @return тримірна матриця переходів, перший вимір - звідки, другий - куди, 
     * третій - масив логічних умов переходу, або ж null якщо переходу немає
     */
    public static int [][][] initTransitionTable(Map<Integer,ZState> states){
        int [][][] table = new int [states.size()][states.size()][];
        
        for (Map.Entry<Integer, ZState> entry : states.entrySet()) {
            Integer integer = entry.getKey();
            ZState zState = entry.getValue();
            
            for (Connection connection : zState.fromConnections) {
                table[connection.from][connection.to] = Arrays.copyOf(connection.conditions, connection.conditions.length);
            }
        }
        return table;
        
    }

    
    /**
     * Повертає максимальний степінь вершини в графі
     * @param states
     * @return масив [power, in] ,де in - n змінних, id вершин з найбільшим степенем,
     * n може дорівнювати нулю
     */
    public static int[] getPower(Map<Integer,ZState> states){
        
        //TODO подумати чи потрібно повертати масив, чи можна обітися тільки степенем
        int power = 0;
        List<Integer> indexs = new ArrayList<>();
        int [][][] table = initTransitionTable(states);
        
        for (int i = 0; i < table.length; i++) {
            int curr_power = 0;
            for (int j = 0; j < table[i].length; j++) {
                if(table[i][j] != null){
                    if(i != j){
                        curr_power++;
                    }
                }else if(table[j][i] != null){
                    curr_power++;
                }            
            }
            
            if(curr_power > power){
                indexs.clear();
                indexs.add(i);
                power = curr_power;
            }else if(curr_power == power){
                indexs.add(i);
            }
            
        }
        int [] result = new int [indexs.size()+1];
        result[0] = power;
        for (int i = 0; i < indexs.size(); i++) {
            result[i+1] = indexs.get(i);
        }
        
        //перевірка на кількість вершин
        int new_result = (int) Math.round(Math.ceil(Math.log(states.size())/Math.log(2)));
        if(new_result > result[0]){
            result = new int[1];
            result[0] = new_result;
        }
        
        return result;
    }
    
    
    /**
     * Генерація шаблону суміжного кодування
     * @param power
     * @return 
     */
    public static List<SynchroState> initTamplate(int power){
        
        int count = (int)Math.ceil(Math.pow(2, power));
        List<SynchroState> template = new ArrayList<>(count);
        
        for (int i = 0; i < count; i++) {
            template.add(i, new SynchroState(i, power));
        }
        
        return template;
    }
    
    
    /**
     * Закодувування станів сумісним кодуванням
     * @param states
     */
    public static void encode(Map<Integer, ZState> states) throws Exception{
        
        int places = getPower(states)[0];
        int [][][] table = initTransitionTable(states);
        List<SynchroState> template = initTamplate(places);
        
        addToTemplate(template, states.get(0), 0);
        
        Map<Integer, ZState> trueStates = new HashMap<>(states);
        for (Map.Entry<Integer, ZState> entry : trueStates.entrySet()) {
            Integer id = entry.getKey();
            ZState zState = entry.getValue();
            
            if(zState.code == null){
                addToTemplate(template, zState);
            }
            
            ZState trueState = new ZState(zState);
            for(Connection conn:trueState.fromConnections){
                if(states.get(conn.to).code == null){
                     // пошук місця серед сусідів
                    int [] neibs = template.get(states.get(conn.from).code).getNeighbors();
                    for (int i = 0; i < neibs.length; i++) {
                        if(template.get(neibs[i]).state == null){
                            addToTemplate(template, states.get(conn.to), neibs[i]);   
                            break;
                        }
                    }
                }
                List<List> path = new ArrayList<>();
                int length = 0;
                int from = states.get(conn.from).code;
                int step = from;
                int to = states.get(conn.to).code;
                while(path.isEmpty()){
                    
                    createPath(from, step, to,
                            length, 0, places, new ArrayList<Integer>(), path, template);
                    length++;
                    if (length > places+1){
                        throw new  Exception("Problem with analysys. Don`t lounch it more than once on the same graph");
                    }
                    //TODO визначити можливість не виконання асерта
                }
                if(path.get(0).size()>2){  //отже - потрібні доп вершини
                    fillPath(path.get(0), template, states, places);
                }
            }
        }
    }  
    /**
     * Заповнення шаблона пустими вершинами для утворення шляху за умови довжини шляху більше 2
     * @param road
     * @param template
     * @param states
     * @param places 
     */
    private static void fillPath(List<Integer> road, List<SynchroState> template, Map<Integer, ZState> states, int places){
        
         //Перша і остання вершини вже існують
        for (int i = 1; i < road.size()-1; i++) {
            ZState state = new ZState(states.size(), null); //states.size() залишажмо місце для значущих вершин
            states.put(states.size(), state);
            addToTemplate(template, state, road.get(i));
        }
        
        Connection oldConn = new Connection(template.get(road.get(0)).state.id, template.get(road.get(road.size()-1)).state.id, null, null);
        template.get(road.get(road.size()-1)).state.toConnections.remove(oldConn); //знищимо старий шлях
        
        oldConn = template.get(road.get(0)).state.fromConnections.get(template.get(road.get(0)).state.fromConnections.indexOf(oldConn));
        oldConn.to = template.get(road.get(1)).state.id;
        template.get(road.get(1)).state.toConnections.add(oldConn);
        
        
        
        for (int i = 1; i < road.size()-1; i++) {
            int from = template.get(road.get(i)).state.id;
            int to = template.get(road.get(i+1)).state.id;
            
            int [] cond = new int[template.get(0).state.fromConnections.get(0).conditions.length];
            
            String signal = template.get(road.get(i)).state.signalId;
            
            Connection newConnection = new Connection(from, to, cond, signal);
            template.get(road.get(i)).state.fromConnections.add(newConnection);
            template.get(road.get(i+1)).state.toConnections.add(newConnection);
        }
        
    }
    /**
     * Розташування нового стану в визначено міці шаблону місці 
     * @param template
     * @param state
     * @param index 
     */
    private static void addToTemplate(List<SynchroState> template, ZState state, int index){
        template.get(index).state = state;
        state.code = new Integer(template.get(index).intCode);
    }
    /**
     * Пошук вільного міця в шаблоні і додавання туди стану
     * @param template
     * @param state 
     */
    private static void addToTemplate(List<SynchroState> template, ZState state){
        int i = 0;
        while(template.get(i).state != null){
            i++;
        }
        addToTemplate(template, state, i);
    }
    /**
     * Пошук ввільного шляху в шаблоні
     * @param from початок на шаблоні
     * @param step поточна позиція на шаблоні
     * @param to кінець на шаблоні
     * @param lMax довжина шуканого шляху
     * @param lCurr пройдено шляху
     * @param places розрядність номера
     * @param road поточний шлях по шаблону
     * @param roads завершені шляхи, тут і буде результат роботи процедури
     * @param template шаблон,, по якому відбувається пересування
     */
    private static void createPath(int from, int step, int to, int lMax, int lCurr, int places,
            ArrayList<Integer> road,final List<List> roads,final List<SynchroState> template){
        
        if(lMax == lCurr){
            if(step == to){
                road.add(step);
                roads.add(road);
            }
        }else{
            int [] neibs = SynchroState.getNeighbors(step, places);
            for (int neib: neibs){
                if((!road.contains(neib))){
                    if(template.get(neib).state == null || lMax == lCurr+1){
                        ArrayList<Integer> new_road = new ArrayList<>(road);
                        new_road.add(step);
                        createPath(from, neib, to, lMax, lCurr+1, places, new_road, roads, template);
                    }
                }
            }
        }

    }
    
    /**
     * Вибірка всіх переходів з станів
     * @param states
     * @return 
     */
    public static AutomatonTable getConnections(Map<Integer, ZState> states){
        List<Connection> connections = new ArrayList<>(states.size());
        List<String> ids = new ArrayList<>();
        List<boolean []> codes = new ArrayList<>();
        
        AutomatonTable table = new AutomatonTable();
        table.connections = connections;
        table.ids = ids;
        table.codes = codes;
        
        for (Map.Entry<Integer, ZState> entry : states.entrySet()) {
            
            ZState zState = entry.getValue();
            
            if(zState.signalId != null){
                ids.add(zState.signalId);
            }else{
                ids.add("0");
            }
            
            boolean[] code =SynchroState.intToCode(zState.code, getPower(states)[0]);
            codes.add(code);
            
            for(Connection conn : zState.fromConnections){
                if(!connections.contains(conn)){
                    
                    connections.add(conn);
                }
            }
            
        }
        return table;
    }
    
    /**
     * Зменшення, за можливості, степерів вершини
     * @param states
     * @return максимальна степінь в оптимізованому алгоритмі
     */
    public static int optimize(Map<Integer,ZState> states){
        //FIXME не реалізовано
        int power = 0;
        for (ZState state : states.values()) {
            if(state.fromConnections.size() > 1){
                for (int i = 0; i < state.fromConnections.size(); i++) {
                    Connection curr = state.fromConnections.get(i);
                    Connection same = null;   // Зв'язок, що відмінний однією логічною умовою
                    //TODO реалізувати пошук переходів з різною логічною умовою і без зворотніх повернень
                    for (int j = i+1; j < state.fromConnections.size(); j++) {
                        
                        int diffConditions = 0;
                        for (int k = 0; k < curr.conditions.length; k++) {
                            if(curr.conditions[k] == state.fromConnections.get(j).conditions[k]){
                                diffConditions++;
                            }
                        }
                        
                        if(diffConditions == 1){
                            
                        }
                    }
                    
                }
            }
            
            
            //TODO пошук входів за однією умовою без зворотнього шляху
        }
        
        
        return power;
    }
    
    
    
}
