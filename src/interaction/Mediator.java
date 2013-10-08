package interaction;
import GUI.WorkPanel;
import GUI.algorithm.AlgPanel;
import internal_representation.LSAmatrix;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import moore.Synthesizer;
import parser.ParseException;
import parser.Parser;

/**
 *
 * @author Wittmann
 */
public class Mediator {
    
    //TODO замінити модифікатор доступу до елементів і реалізувати делагацію потрібних методів
    public AlgPanel algPanel;
    public JFrame frame;
    public WorkPanel wp;
    public Parser parser;

    public Synthesizer synthesizer;

    public LSAmatrix matrix;
    
    public Mediator(){
        parser = new Parser();
        
        algPanel = new AlgPanel();
        
        synthesizer = new Synthesizer(parser);

        MenuListener menuListener = new MenuListener();
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
     * Зберегти поточний ЛСА в парсері
     */
    public void readLSA(){
        parser.LSA = wp.inputArea.getText();
    }
    
    public void parseLSA(){
        this.readLSA();
        try {
            matrix = this.parser.parse();
        } catch (ParseException ex) {
            Logger.getLogger(Mediator.class.getName()).log(Level.SEVERE, null, ex);
            writeInfo(ex.getMessage());
        }
    }
    
    /**
     * Вивести інформацію у вікні інформації
     * @param str інфа
     */
    public void writeInfo(String str){
        wp.outputArea.setText(str + wp.outputArea.getText());
    }
    
}
