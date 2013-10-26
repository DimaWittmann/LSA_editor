package moore.compatible_coding;

/**
 *
 * @author Wittman
 */
public class SynchroState {
    
    public ZState state;
    public boolean [] code;
    public int intCode;
    public int places;


    public SynchroState(int number, int places) {
        
        code = intToCode(number, places);
        intCode = number;
        this.places = places;
    }
    
    public int [] getNeighbors(){
        return getNeighbors(intCode, places);
    }
    
    public static int [] getNeighbors(int number, int places){
        int [] neighbors = new int[places];
        boolean [] code = intToCode(number, places);
        for (int i = 0; i < places; i++) {
            code[i] = !code[i];
            neighbors[i] = codeToInt(code);
            code[i] = !code[i];
        }
        return neighbors;
    }
    
    public static int codeToInt(boolean [] code){
        
        int value = 0;
        for (int i = 0; i < code.length; i++) {
            if(code[i]){
                value += Math.pow(2, i);
            }
        }
        return value;
    }
    
    public static boolean[] intToCode(int number, int places){
        
        boolean [] code = new boolean[places];
        
        int currNumber = number;
        int i = 0;
        
        while(currNumber > 0){
            int currBit = currNumber % 2;
            currNumber = currNumber >> 1;
            
            if(currBit == 1){
                code[i] = true;
            }
            
            i++;
        }
        return code;
    }
    
    public static String codeToString(boolean [] code){        
        String value = "";
        for (int i = code.length-1; i >= 0; i--) {
            if(code[i]){
                value += "1";
            }else{
                value += "0";
            }
        }
        return value;
    }
    public static boolean[] stringToCode(String value){
        boolean [] code = new boolean[value.length()];
        int j = 0;
        for (int i = value.length()-1; i >= 0; i--) {
            if(value.charAt(i) == '1'){
                code[i] = true;
            }else{
                code[i] = false;
            }
        }
        
        return code;
    }
}
