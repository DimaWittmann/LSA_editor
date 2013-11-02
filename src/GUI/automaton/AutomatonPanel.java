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
import moore.compatible_coding.SynchroState;

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
        
        this.removeAll();
        List<String> ids = Application.mediator.automatonTable.ids;
        List<Point> points =  Application.mediator.automatonTable.points;
        List<boolean[]> codes = Application.mediator.automatonTable.codes;
        int x = R*2;
        int y = R*2;
        StateMouseListener mListener = new StateMouseListener();
        
        for (int i = 0; i < ids.size(); i++) {
            StatePanel panel = new StatePanel("Z"+String.valueOf(i), ids.get(i));
            if(codes != null){
                panel.setCode(SynchroState.codeToString(codes.get(i)));
            }
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
                fillArrow(new Point(statePanels.get(conn.from).getLocation().x-10, statePanels.get(conn.from).getLocation().y+R),
                        new Point(statePanels.get(conn.from).getLocation().x, statePanels.get(conn.from).getLocation().y+R), g);
                
            }else{
                double xFrom = statePanels.get(conn.from).getCenter().x;
                double yFrom = statePanels.get(conn.from).getCenter().y;

                int xTo = statePanels.get(conn.to).getCenter().x;
                int yTo = statePanels.get(conn.to).getCenter().y;
                
                double xLen = xFrom + (xTo- xFrom)/2;
                double yLen = yFrom + (yTo- yFrom)/2;
                
                double d = Math.sqrt(((xTo- xFrom)*(xTo- xFrom)+(yTo- yFrom)*(yTo- yFrom)));

                double x12 = xTo - xFrom;
                double y12 = yTo - yFrom;
                
                int degree = -20;
                
                double len = Math.sqrt(x12*x12+y12*y12);
                double xF = ((x12/len*Math.cos(Math.PI/180*20) - y12/len*Math.sin(Math.PI/180*20))*R);
                double yF = ((x12/len*Math.sin(Math.PI/180*20) + y12/len*Math.cos(Math.PI/180*20))*R);
 //               g.fillOval(xFrom+xF, yFrom+yF, 5, 5);    //точка виходу з кола
                
                double xI = (int) (((-x12)/len*Math.cos(Math.PI/180*degree) - (-y12)/len*Math.sin(Math.PI/180*degree))*R);
                double yI = (int) (((-x12)/len*Math.sin(Math.PI/180*degree) + (-y12)/len*Math.cos(Math.PI/180*degree))*R);
//                g.fillOval(xTo+xI, yTo+yI, 5, 5);
                
                
                xFrom += xF;
                yFrom += yF;
                xTo += xI;
                yTo += yI;
                x12 = xTo - xFrom;
                y12 = yTo - yFrom;
                
                double xp = - y12;
                double yp = x12;
                
                double lenP = Math.sqrt(xp*xp+yp*yp);
                xp = xp/lenP;
                yp = yp/lenP;
                
                double ctrlx =  xLen + xp*d/4;
                double ctrly =  yLen + yp*d/4;
                
                String cond = "";
                if(xFrom < xTo){
                    cond += ">";
                }else{
                    cond += "<";
                }
                for (int i = 0; i < conn.conditions.length; i++) {
                    if(conn.conditions[i] == 1){
                        cond += conditions.get(i)+" ";
                    }else if(conn.conditions[i] == 2){
                        cond += "~"+conditions.get(i)+" ";
                    }
                }
                if(xFrom < xTo){
                    cond += ">";
                }else{
                    cond += "<";
                }
                Font defaultFont = g.getFont();
                g.setFont(new Font(defaultFont.getFontName(), Font.BOLD, defaultFont.getSize()));
                g.drawString(cond, (int)(xLen + xp*d/8)-15, (int)(yLen + yp*d/8));
                
                
                double xA = xTo - ctrlx;    //малюємо стрілочку
                double yA = yTo - ctrly;
                double lenA = Math.sqrt(xA*xA+yA*yA);
                xA = xA/lenA*(lenA-10);
                yA = yA/lenA*(lenA-10);
                xA += ctrlx;                //початок стрілки
                yA += ctrly;
                
                fillArrow(new Point((int)xA, (int)yA), new Point(xTo, yTo), g);

                
                Graphics2D g2 = (Graphics2D) g;
                QuadCurve2D q = new QuadCurve2D.Float();
                q.setCurve(xFrom, yFrom, ctrlx, ctrly, xTo, yTo);
                g2.draw(q);
            }
            
        }
    }
    
    /**
     * Намалювати рінобедрений трикутник і залити його
     * @param from середина основи трикутника
     * @param to вершина трикутника
     * @param g графічний контекст
     */
    public static void fillArrow(Point from, Point to, Graphics g){
        double xArrow = to.x - from.x;
        double yArrow = to.y - from.y;
        double lenArrow = Math.sqrt(xArrow*xArrow + yArrow*yArrow);
        xArrow /= lenArrow;
        yArrow /= lenArrow;
        double xW = - yArrow;
        double yW = xArrow;
        xW *= 5;
        yW *= 5;
        xW += from.x;
        yW += from.y;

        double xW1 = yArrow;
        double yW1 = -xArrow;
        xW1 *= 5;
        yW1 *= 5;
        xW1 += from.x;
        yW1 += from.y;

        int [] xarray = {(int)xW, to.x, (int)xW1};
        int [] yarray = {(int)yW, to.y,(int) yW1};
        g.fillPolygon(xarray, yarray, 3);
    }
}
