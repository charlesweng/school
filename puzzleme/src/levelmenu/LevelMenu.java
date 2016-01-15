/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelmenu;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import levelselection.LevelSelection;
import objects.Board;
import view.View;

/**
 *
 * @author Administrator
 */
public class LevelMenu extends JPanel implements ActionListener, MouseListener,MouseMotionListener {
    //declare attributes
    private View view;
    private LevelSelection level;
    //declare a timer to do an animation.
    private Timer Counter;
    private int delay=20, BoardSize=300,Square_SideLength,numofsquare,delta=0,move=0;
    //declare some attributes to help painting and repainting.
    private Font font = new Font("Elephant",Font.BOLD,24);
    private int x,y;
    private Color color1=Color.black;
    private Color color2=Color.black;
    
    /*
     * Constructor
     */
    public LevelMenu(View view){
        addMouseMotionListener(this);
        addMouseListener(this);
        this.view = view;
        Counter=new Timer(delay,this);
        Counter.start();
        setBackground(Color.black);
        setFont(font);
       
    }
    /*
     * return size of a board
     */
    public int getBoardSize()
    {
        return BoardSize;
    }
    /*
     * convert pixel point into coordinate point.
     */
     public int convertToCoord(int num)
    {
        return ((Math.abs(num))/Square_SideLength);
    }
     @Override
     /*
      * override the paintComponent
      */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.blue);
        g.drawRect(10,150,250,250);
        g.drawRect(330,150,250,250);
        g.setColor(color1);
        g.fillRect(11,151,249,249);
        g.setColor(color2);
        g.fillRect(331,151,249,249);
        g.setColor(Color.blue);
        g.drawString("Choose a Level",move,30);
        g.drawString("Five By Five", 53, 150+250/2);
        g.drawString("Nine By Nine", 370, 150+250/2);
        g.setColor(Color.red);
        
    }
    /*
     * make the title selection move across the screen and then bounce back
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (move==400)
        {
            delta=-1;
        }
        if (move==0)
        {
            delta=1;
        }
        move+=delta;
        repaint();
    }

  
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    /*
     * when mouse is Pressed within the level box, the color will change.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // if the mouse is within the level box range, it lights up.
        if (e.getPoint().x>10 && e.getPoint().y>150 && e.getPoint().x<260 && e.getPoint().y<150+230)
        {
             color1=Color.yellow;
        }
        else 
        {
            color1=Color.black;
        }
        if (e.getPoint().x>330 && e.getPoint().y>150 && e.getPoint().x<330+250 && e.getPoint().y<150+230)
        {
             color2=Color.yellow;
        }
        else 
        {
            color2=Color.black;
        }
        repaint();

        
    }
    
    /*
     * when the mouse is release jump to a levelselection memu.
     */
@Override
    public void mouseReleased(MouseEvent e) {
        // if the mouse is release within a certain range activate the view class.
        if (e.getPoint().x>10 && e.getPoint().y>150 && e.getPoint().x<260 && e.getPoint().y<150+230)
        {
            Board.fbfSelected=true;
            Board.nbnSelected=false;
                view.initLevelSelection();
                view.viewLevelSelection();
        }
        else 
        {
            color1=Color.black;
            color2=Color.black;
            repaint();
        }
        if (e.getPoint().x>330 && e.getPoint().y>150 && e.getPoint().x<330+250 && e.getPoint().y<150+230)
        {
            Board.fbfSelected=false;
            Board.nbnSelected=true;
             view.initLevelSelection2();
             view.viewLevelSelection2();
        }
        else 
        {
            color1=Color.black;
            color2=Color.black;
            repaint();
        }
       
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
     * similar to mousePress, when a mouse move within the mouse Box, the color change.
     */

    @Override
    public void mouseMoved(MouseEvent e) {
         // if the mouse is within the level box range, it lights up.
          if (e.getPoint().x>10 && e.getPoint().y>150 && e.getPoint().x<260 && e.getPoint().y<150+230)
        {
             color1=Color.yellow;
        }
        else 
        {
            color1=Color.black;
        }
        if (e.getPoint().x>330 && e.getPoint().y>150 && e.getPoint().x<330+250 && e.getPoint().y<150+230)
        {
             color2=Color.yellow;
        }
        else 
        {
            color2=Color.black;
        }
        repaint();
    }
}
