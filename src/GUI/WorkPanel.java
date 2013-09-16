package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import parser.Parser;

/**
 *
 * @author wittmann
 */
public class WorkPanel extends JPanel{
    
    JTextArea inputArea;
    JTextArea outputArea;
    Parser parser;
    
    public WorkPanel() {
        this.setLayout(new BorderLayout(5, 5));
        this.parser = new Parser();
        initPanel();
    }
    
    private void initPanel(){
        
        JMenuBar menuBar = new JMenuBar();
        
        MenuListener listener = new MenuListener();
        
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(listener);
        
        JMenu runMenu = new JMenu("Run");
        JMenuItem runItem = new JMenuItem("Run");
        runItem.addActionListener(listener);
        
        fileMenu.add(newItem);
        runMenu.add(runItem);
        
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        
        this.add(menuBar, BorderLayout.PAGE_START);
        
        inputArea = new JTextArea(5, 80);
        
        outputArea = new JTextArea();
        
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(inputArea);
        splitPane.add(outputArea);
        
        this.add(splitPane, BorderLayout.CENTER);
        
        
    }
    
    
    class MenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Run":
                    String text = WorkPanel.this.inputArea.getText();
                    
            }
        }
        
    }

}
