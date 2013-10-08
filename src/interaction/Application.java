package interaction;

/**
 * 
 * @author Wittman
 */
public class Application {
    
    /**
     * Центр всього додатку
     */
    public static Mediator mediator;
    
    public static void main(String [] args){
        mediator = new Mediator();
    }
}
