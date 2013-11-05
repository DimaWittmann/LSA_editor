package GUI;

import interaction.Application;
import java.awt.Dimension;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Wittman
 */
public class AutomatonTableFrame extends JFrame{

    public AutomatonTableFrame() throws HeadlessException {
        super("Automaton table");
        
        JTable table = new JTable(Application.mediator.automatonTable);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        
        this.add(scrollPane);
        this.setSize(new Dimension(860, 480));
    }

    
}
