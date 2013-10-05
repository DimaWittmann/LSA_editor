package GUI;

import interaction.MenuListener;
import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.*;

/**
 *
 * @author wittmann
 */
public class WorkPanel extends JPanel{
    
    public JTextArea inputArea;
    public JTextArea outputArea;
    protected MenuListener listener;
    
    
    public WorkPanel(MenuListener listener) {
        this.listener = listener;
        this.setLayout(new BorderLayout(5, 5));
        initPanel();
    }
    
    private void initPanel(){
        
        JMenuBar menuBar = new JMenuBar();
        
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(listener);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(listener);
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(listener);
        JMenuItem saveXMLItem = new JMenuItem("Save automaton to XML");
        saveXMLItem.addActionListener(listener);
        
        JMenu runMenu = new JMenu("Run");
        
        JMenuItem runItem = new JMenuItem("Run");
        runItem.addActionListener(listener);
        JMenuItem validateItem = new JMenuItem("Validate");
        validateItem.addActionListener(listener);
        JMenuItem drawAlgo = new JMenuItem("Draw algorithm");
        drawAlgo.addActionListener(listener);
        JMenuItem synthesize = new JMenuItem("Synthesize automaton");
        synthesize.addActionListener(listener);
        
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(saveXMLItem);
        
        runMenu.add(runItem);
        runMenu.add(validateItem);
        runMenu.add(drawAlgo);
        runMenu.add(synthesize);
        
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        
        this.add(menuBar, BorderLayout.PAGE_START);
        
        inputArea = new JTextArea(5, 80);
        
        outputArea = new JTextArea();
        outputArea.setForeground(Color.red);
        Font font = new Font("Times New Roman", Font.PLAIN, 14);
        outputArea.setFont(font);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(inputArea);
        splitPane.add(outputArea);
        
        this.add(splitPane, BorderLayout.CENTER);
        
    }
}
