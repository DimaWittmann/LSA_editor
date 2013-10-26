package GUI;

import interaction.menu_listeners.DrawMenuListener;
import interaction.menu_listeners.FileMenuListener;
import interaction.menu_listeners.RunMenuListener;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author wittmann
 */
public class WorkPanel extends JPanel{
    public static int width = 860;
    public static int height = 480;
    
    //змінити на JTextPane реалізувати підсвічування і тд.
    public JTextArea inputArea;
    public JTextArea outputArea;
    
    
    public WorkPanel() {
        this.setLayout(new BorderLayout(5, 5));
        initPanel();
    }
    
    private JMenu createFileMenu(){
        JMenu fileMenu = new JMenu("File");
        
        FileMenuListener fileListener = new FileMenuListener();
        JMenuItem newItem = new JMenuItem(FileMenuListener.NEW);
        newItem.addActionListener(fileListener);
        
        JMenuItem saveItem = new JMenuItem(FileMenuListener.SAVE);
        saveItem.addActionListener(fileListener);
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        
        JMenuItem loadItem = new JMenuItem(FileMenuListener.LOAD);
        loadItem.addActionListener(fileListener);
        loadItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        
        JMenuItem saveXMLItem = new JMenuItem(FileMenuListener.SAVE_XML);
        saveXMLItem.addActionListener(fileListener);
        saveXMLItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, (ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK)));
        
        JMenuItem loadXMLItem = new JMenuItem(FileMenuListener.LOAD_XML);
        loadXMLItem.addActionListener(fileListener);
        loadXMLItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, (ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK)));
        
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(saveXMLItem);
        fileMenu.add(loadXMLItem);
        
        return fileMenu;
    }
    
    private JMenu createRunMenu() {
        JMenu runMenu = new JMenu("Run");
        
        RunMenuListener runListener = new RunMenuListener();
        
        JMenuItem runItem = new JMenuItem(RunMenuListener.RUN);
        runItem.addActionListener(runListener);
        JMenuItem validateItem = new JMenuItem(RunMenuListener.VALIDATE);
        validateItem.addActionListener(runListener);
        JMenuItem synthesize = new JMenuItem(RunMenuListener.SYNTHESIZE);
        synthesize.addActionListener(runListener);
        JMenuItem analyze = new JMenuItem(RunMenuListener.ANALYZE);
        analyze.addActionListener(runListener);
        
        runMenu.add(runItem);
        runMenu.add(validateItem);
        runMenu.add(synthesize);
        runMenu.add(analyze);
        
        return runMenu;
    }
    
    private JMenu createDrawMenu(){
        DrawMenuListener drawListener = new DrawMenuListener();
        
        JMenu drawMenu = new JMenu("Show");
        
        JMenuItem drawAlgoItem = new JMenuItem(DrawMenuListener.DRAW_ALGO);
        drawAlgoItem.addActionListener(drawListener);
        JMenuItem drawAutomatonItem = new JMenuItem(DrawMenuListener.DRAW_AUTOMATON);
        drawAutomatonItem.addActionListener(drawListener);
        
        drawMenu.add(drawAlgoItem);
        drawMenu.add(drawAutomatonItem);
        
        return drawMenu;
    }
    
    private void initPanel(){
        
        JMenuBar menuBar = new JMenuBar();
        
        
        JMenu fileMenu = createFileMenu();
        JMenu runMenu = createRunMenu();
        JMenu drawMenu = createDrawMenu();
        
        menuBar.add(fileMenu);
        menuBar.add(runMenu);
        menuBar.add(drawMenu);
        
        this.add(menuBar, BorderLayout.PAGE_START);
        
        inputArea = new JTextArea(5, 80);
        
        outputArea = new JTextArea();
        outputArea.setForeground(Color.RED);
        Font font = new Font("Times New Roman", Font.PLAIN, 14);
        outputArea.setFont(font);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.add(inputArea);
        splitPane.add(outputArea);
        
        this.add(splitPane, BorderLayout.CENTER);
        
    }
}
