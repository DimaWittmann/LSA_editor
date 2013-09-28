package interaction;
import GUI.WorkPanel;
import java.awt.Dimension;
import javax.swing.JFrame;
import parser.Parser;

/**
 *
 * @author Wittmann
 */
public class Controller {
    
    public AlgController algController;
    public JFrame frame;
    public WorkPanel wp;
    public Parser parser;
    public MenuListener menuListener;
    
    public Controller(){
        parser = new Parser();
        
        algController = new AlgController(this);
        
        menuListener = new MenuListener(this);
        
        wp = new WorkPanel(menuListener);
        
        frame = new JFrame("Editor");
        frame.add(wp);
        frame.setMinimumSize(new Dimension(840, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }
    
    public void setOptionalTitle(String title){
        frame.setTitle("Editor | " + title);
    }
    
    /**
     * Зберегти оновити стан парсеру до поточних символів
     */
    public void readLSA(){
        parser.LSA = wp.inputArea.getText();
    }
    
    public void writeInfo(String str){
        wp.outputArea.setText(str + wp.outputArea.getText());
    }
    
}
