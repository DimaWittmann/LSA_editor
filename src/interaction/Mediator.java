package interaction;
import interaction.menu_listeners.FileMenuListener;
import GUI.WorkPanel;
import GUI.algorithm.AlgPanel;
import GUI.automaton.AutomatonFrame;
import internal_representation.AutomatonTable;
import internal_representation.LSAmatrix;
import java.awt.Dimension;
import java.io.File;
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
    public AutomatonFrame automatonFrame;
    public AutomatonTable automatonTable;
    
    public Mediator(){
        parser = new Parser();
        synthesizer = new Synthesizer();
        automatonTable = new AutomatonTable();
        
        algPanel = new AlgPanel();
        wp = new WorkPanel();
        automatonFrame = new AutomatonFrame();
        
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
    
    public void showAutomatonFrame(){
        automatonFrame.panel.initPanels();
        automatonFrame.setVisible(true);
    }
    /**
     * Вивести інформацію у вікні інформації
     * @param str інфа
     */
    public void writeInfo(String str){
        wp.outputArea.setText(str +"\n" + wp.outputArea.getText());
    }
    
    public void saveXML(File file){
//        parseLSA();
//        synthesizer.findAllConnetions();
        automatonFrame.panel.updateLocation();
        synthesizer.saveToXML(file);
    }
    
}
