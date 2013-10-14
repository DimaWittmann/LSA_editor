package GUI.automaton;

import static GUI.automaton.StatePanel.R;
import interaction.Application;
import interaction.StateMouseListener;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import moore.Connection;

/**
 *
 * @author Wittman
 */
public class AutomatonPanel extends JPanel{

    public  Map<Integer,StatePanel> statePanels;
    
    public AutomatonPanel() {
        statePanels = new HashMap<>();
        this.setLayout(null);
    }
    
    /**
     * Створення і встановлення на початковій позиції панелей відображення 
     * станів автомата 
     */
    public void initPanels(){
        
        List<String> ids = Application.mediator.automatonTable.ids;
        List<Point> points =  Application.mediator.automatonTable.points; 
        int x = R*2;
        int y = R*2;
        StateMouseListener mListener = new StateMouseListener();
        
        for (int i = 0; i < ids.size(); i++) {
            StatePanel panel = new StatePanel("Z"+String.valueOf(i), ids.get(i));
            panel.addMouseMotionListener(mListener);
            panel.addMouseListener(mListener);
            
            if(ids.size() != points.size()){
                panel.setLocation(x, y);
                x += R*8;
                if(x >  Application.mediator.automatonFrame.getWidth()){
                    x = R*2;
                    y += R*4;
                }
            }else{
                panel.setLocation(points.get(i));
            }
            statePanels.put(i, panel);
            this.add(panel);
        }
        
    }
    
    /**
     * Оновити позиції елементів в таблиці
     */
    public void updateLocation(){
        Application.mediator.automatonTable.points = new ArrayList<>();
        for (int i = 0; i < statePanels.size(); i++) {
             Application.mediator.automatonTable.points.add(statePanels.get(i).getLocation());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        List<Connection> connetions = Application.mediator.automatonTable.connections;
        List<String> conditions= Application.mediator.automatonTable.conditions;
        
        for(Connection conn: connetions){
            
            if (conn.from == conn.to){
                int x = statePanels.get(conn.from).getLocation().x - R;
                int y = statePanels.get(conn.from).getLocation().y - R;
                g.drawOval(x, y, R*2, R*2);
                String cond = "";
                for (int i = 0; i < conn.conditions.length; i++) {
                    if(conn.conditions[i] == 1){
                        cond += conditions.get(i)+" ";
                    }else if(conn.conditions[i] == 2){
                        cond += "~"+conditions.get(i)+" ";
                    }
                }
                g.drawString(cond, x, y + R);
            }else{
                int xFrom = statePanels.get(conn.from).getCenter().x;
                int yFrom = statePanels.get(conn.from).getCenter().y;

                int xTo = statePanels.get(conn.to).getCenter().x;
                int yTo = statePanels.get(conn.to).getCenter().y;
                
                int xLen = xFrom + (xTo- xFrom)/2;
                int yLen = yFrom + (yTo- yFrom)/2;
                
                int d = (int) Math.sqrt(((xTo- xFrom)*(xTo- xFrom)+(yTo- yFrom)*(yTo- yFrom)));
                
                
                int x12 = xTo - xFrom;
                int y12 = yTo - yFrom;
                
                double xp = - y12;
                double yp = x12;
                
                double lenP = Math.sqrt(xp*xp+yp*yp);
                xp = xp/lenP;
                yp = yp/lenP;
                
                int ctrlx =  (int) (xLen + xp*d/4);
                int ctrly =  (int) (yLen + yp*d/4);
                
                String cond = "";
                if(xFrom < xTo){
                    cond += ">>>";
                }else{
                    cond += "<<<";
                }
                for (int i = 0; i < conn.conditions.length; i++) {
                    if(conn.conditions[i] == 1){
                        cond += conditions.get(i)+" ";
                    }else if(conn.conditions[i] == 2){
                        cond += "~"+conditions.get(i)+" ";
                    }
                }
                if(xFrom < xTo){
                    cond += ">>>";
                }else{
                    cond += "<<<";
                }
                Font defaultFont = g.getFont();
                g.setFont(new Font(defaultFont.getFontName(), Font.BOLD, defaultFont.getSize()));
                g.drawString(cond, (int)(xLen + xp*d/8)-30, (int)(yLen + yp*d/8));
                
               
                Graphics2D g2 = (Graphics2D) g;
                QuadCurve2D q = new QuadCurve2D.Float();
                q.setCurve(xFrom, yFrom, ctrlx, ctrly, xTo, yTo);
                g2.draw(q);
            }
            
        }
    }
}
