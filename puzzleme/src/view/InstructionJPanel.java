/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Gooki-G46
 */
public class InstructionJPanel extends JPanel implements ActionListener,MouseListener,MouseMotionListener{
    //Timer attributes
    private Timer timer;
    private Font font = new Font("Elephant",Font.BOLD,20);
    private Font font2 = new Font("Elephant",Font.BOLD,12);
    //drawing Attributes
    private int level=1;
    private int width=12;
    private Color backButton=Color.BLACK;
    //view
    View view;
    InstructionJPanel(View view){
        //set View
        this.view = view;
        //set JPanel properties
        setBackground(Color.black);
        setSize(600,600);
        //set timer
        timer=new Timer(150,this);
        timer.start();
        //add listener
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    /*
     * draw all the instructor and create a simple animation 
     */
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //start the drawing
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString("1.Click Start Game to begin", 30, 30);
        g.drawRect(60 ,60, 84, 24);
        g.setFont(font2);
        g.drawString("Start Game",62,75);
        g.setFont(font);
        g.drawString("2.Choose A Level",30 , 130);
        g.setFont(font2);
        g.drawString("Five By Five", 65, (100/2+155));
        g.drawString("Nine By Nine", 245, (100/2+155));
        g.drawRect(60,155,100,100);
        g.drawRect(240,155,100,100);
        g.setFont(font);
        g.drawString("3.Choose a SubLevel",30,150+125+20);
        g.setFont(font2);
        g.setColor(Color.blue);
        g.fillRect(65,150+125+40,100,50);
        g.setColor(Color.white);
        g.drawString("Level  "+level, 100/2+39, 150+125+25+50/2+20);
        g.setFont(font);
        g.drawString("4.Start Playing",30,150+125+125+20);
        g.setColor(Color.blue);
        g.drawRect(30, 150+125+125+50, 50, 50);
        g.drawRect(30+50, 150+125+125+50, 50, 50);
        g.drawRect(30+50+50, 150+125+125+50, 50, 50);
        g.drawRect(30+50+50+50, 150+125+125+50, 50, 50);
        g.drawRect(30+50+50+50+50, 150+125+125+50, 50, 50);
        g.setColor(Color.red);
        g.fillOval(30,150+125+125+50,50,50);
        g.fillOval(30+50+50+50+50,150+125+125+50,50,50);
        g.fillRect(30+25, 150+125+125+50+20, width, 12);
        g.setColor(backButton);
        g.fillRect(500, 500, 50, 30);
        g.setColor(Color.white);
        g.setFont(font2);
        g.drawString("Back",508,520);
        g.drawRect(500, 500, 50, 30);
        
    }
    /*
     * animation for the connecting line and level selections
     */
    @Override
    public void actionPerformed(ActionEvent e) {
          if (level<=29){
              level++;
          }
          else
              level=1;
          if(width< 200){
              width+=50;
          }
          else
              width=0;
          repaint();
     }

    @Override
    public void mouseClicked(MouseEvent e) {

    }
    /*
     * go back to the start menu when the back button is being pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getPoint().x>500 && e.getPoint().x<550 && e.getPoint().y>500 && e.getPoint().y<530 ){
            view.run();
           
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }
    /*
     * highlight the back button when the mouse is within the range.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if(e.getPoint().x>500 && e.getPoint().x<550 && e.getPoint().y>500 && e.getPoint().y<530 ){
            backButton=Color.cyan;
           
        }
        else{
            backButton=Color.black;
        }
         repaint();
    }
}
