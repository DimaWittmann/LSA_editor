package GUI;

import java.awt.*;
import internal_representation.LSAmatrix;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.util.logging.*;
import javax.swing.*;
import parser.*;

/**
 *
 * @author wittmann
 */
public class WorkPanel extends JPanel{
    
    JTextArea inputArea;
    JTextArea outputArea;
    Parser parser;
    
    
    public WorkPanel(Parser p) {
        
        this.setLayout(new BorderLayout(5, 5));
        this.parser = new Parser();
        initPanel();
    }
    
    private void initPanel(){
        
        JMenuBar menuBar = new JMenuBar();
        
        MenuListener listener = new MenuListener(parser);
        
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(listener);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(listener);
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(listener);
        
        JMenu runMenu = new JMenu("Run");
        
        JMenuItem runItem = new JMenuItem("Run");
        runItem.addActionListener(listener);
        JMenuItem validateItem = new JMenuItem("Validate");
        validateItem.addActionListener(listener);
        
        fileMenu.add(newItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        
        runMenu.add(runItem);
        runMenu.add(validateItem);
        
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
    
    
    class MenuListener implements ActionListener{
        Parser p;
        public MenuListener(Parser p ){
            this.p = p;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(e.getActionCommand()){
                case "Run":
                    try {
                        p.linkTokens(p.getTokens(inputArea.getText()));
                        LSAmatrix m = p.toMatrix(p.first);
                        outputArea.setText(m.toString());
                    } catch (ParseException ex) {
                        Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        outputArea.setText(ex.getMessage());
                    }
                    break;
                case "Validate":
                    System.out.println("validate");
                    try {
                        p.linkTokens(p.getTokens(inputArea.getText()));
                        LSAmatrix m = p.toMatrix(p.first); 
                        String messages = m.validateMatrix();
                        if(messages.equals("")){
                            outputArea.setText("Matrix is validated\n" + outputArea.getText());
                        }else{
                            outputArea.setText("Some problems with vertexes: \n" 
                                    + messages + outputArea.getText());
                        }
                        outputArea.setText(m.showRoads() + outputArea.getText());
                        
                    } catch (ParseException ex) {
                        Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        outputArea.setText(ex.getMessage());
                    }
                    break;
                case "New":
                    p = new Parser();
                    p.fileName = "no name";
                    JFrame frame = (JFrame) SwingUtilities.getRoot(WorkPanel.this);
                    frame.setTitle(p.fileName);
                    inputArea.setText("");
                    outputArea.setText("");
                    break;
                case "Save":
                    JFileChooser fc = new JFileChooser();
                    int res = fc.showSaveDialog(WorkPanel.this);
                    
                    if(res == JFileChooser.APPROVE_OPTION){
                        ObjectOutputStream oos = null;
                        try {
                            
                            File file = fc.getSelectedFile();
                            frame = (JFrame) SwingUtilities.getRoot(WorkPanel.this);
                            frame.setTitle(file.getName());
                            FileOutputStream fos = new FileOutputStream(file);
                            p.fileName = file.getAbsolutePath();
                            oos = new ObjectOutputStream(fos);
                            oos.writeObject(p.parse());
                            oos.flush();
                        } catch ( IOException | ParseException ex) {
                            Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                oos.close();
                            } catch (IOException ex) {
                                Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    break;
                case "Load":
                    fc = new JFileChooser();
                    res = fc.showOpenDialog(WorkPanel.this);
                    if(res == JFileChooser.APPROVE_OPTION){
                        ObjectOutputStream oos = null;
                        try {
                            File file = fc.getSelectedFile();
                            frame = (JFrame) SwingUtilities.getRoot(WorkPanel.this);
                            frame.setTitle(file.getName());
                            FileInputStream fis = new FileInputStream(file);
                            p.fileName = file.getAbsolutePath();
                            ObjectInputStream ois = new ObjectInputStream(fis);
                            LSAmatrix matrix = (LSAmatrix) ois.readObject();
                            p.fromMatrix(matrix);
                            String text = p.createLSA();
                            inputArea.setText(text);
                            outputArea.setText("");
                            
                        } catch ( ClassNotFoundException | IOException ex) {
                            Logger.getLogger(WorkPanel.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                    }
                    break;
                    
            }
        } 

}

}
