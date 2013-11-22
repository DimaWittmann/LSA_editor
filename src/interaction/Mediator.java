package interaction;
import GUI.AutomatonTableFrame;
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
import xml.XMLCreator;

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
    public AutomatonTableFrame tableFrame;
    
    public Mediator(){
        parser = new Parser();
        synthesizer = new Synthesizer();
        
        algPanel = new AlgPanel();
        wp = new WorkPanel();
        cleanAutomaton();
        
        frame = new JFrame("Editor");
        frame.add(wp);
        frame.setMinimumSize(new Dimension(840, 480));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);     
    }
    /**
     * Знищуємо всю інформацію про старий автомат
     */
    public final void cleanAutomaton(){
        automatonTable = new AutomatonTable();
        automatonFrame = new AutomatonFrame();
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
        wp.outputArea.setText(wp.outputArea.getText() + str +"\n");
    }
    
    public void saveToXML(File file){
        automatonFrame.panel.updateLocation();

        XMLCreator xmlP = new XMLCreator(file);
        xmlP.writeToFile(this.automatonTable);
    }
    
    
    public void showConnections(){
        writeInfo(automatonTable.getConnectionsInfo());
    }

    public void showAutomatonTableFrame() {
        if(tableFrame == null){
            tableFrame = new AutomatonTableFrame();
        }
        tableFrame.setVisible(true);
    }
    
}
