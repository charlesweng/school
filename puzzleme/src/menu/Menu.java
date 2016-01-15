package menu;




import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;



/**
 *
 * @author Charles
 */
public class Menu extends JPanel implements ActionListener{
    //declare attributes for painting
    String text;
    Image image;
    Timer timer;
    long startTime;
    
    //declare animation attributes
    int delay = 20;
    int x1, y1, y2, x2, y3, x3, y4, x4, y5, x5, y6, x6, y7, x7, y8, x8, y9, x9, y10, x10;
    int size1, size2, size3, size4, size5, size6, size7, size8, size9, size10;
    int color1, color2, color3, color4;
    int lsx1, lsy1, lex1, ley1; // lsx = line start x coordinate, lsy = line start y coordinate, lex = line end x coordinate, ley = line end y coordinate
    //declare some helping attributes for the animation.
    boolean larger1, larger2, larger3, larger4;
    boolean changeColor1, changeColor2;
    boolean changedDelay;
    /*
     * constructor
     */
    public Menu()
    {
       this.setBackground(Color.black);
       image = new ImageIcon(getClass().getResource("dots.png")).getImage();
       //set the timer
       timer = new Timer(delay, this);
       setSize(600, 600);
       circle1();
       circle2();
       circle3();
       circle4();
       circle5();
       circle6();
       //start the timer
       startTime = System.nanoTime();
       timer.start();
    }
    /*
     * set the value for circle 1
     */
    public void circle1()
    {
       size1 = this.getWidth()/15;
       x1 = this.getWidth()/10;
       y1 = this.getHeight()/2;
    }
    /*
     * set the value for circle 2
     */
    public void circle2()
    {
       size2 = this.getWidth()/7;
       x2 = (int) (this.getWidth()/7.6);
       y2 = this.getHeight()/3;
    }
    /*
     * set the value for circle 3
     */
    public void circle3()
    {
        size3 = this.getWidth()/7;
        x3 = (int) (this.getWidth()/3.5);
        y3 = (int) (this.getHeight()/2.9);
        color1 = 0;
        color2 = 128;
    }
    /*
     * set the value for circle 4
     */
    public void circle4()
    {
        size4 = this.getWidth()/8;
        x4 = (int) (this.getWidth() - this.getWidth()/2.1);
        y4 = this.getHeight()/2;
        color3 = 128;
        color4 = 0;
    }
    /*
     * set the value for circle 5
     */
    public void circle5()
    {
        size5 = this.getWidth()/7;
        x5 = (int) (this.getWidth()-this.getWidth()/2.8);
        y5 = this.getHeight()/3;
    }
    /*
     * set the value for circle 6
     */
    public void circle6()
    {
        size6 = this.getWidth()/6;
        x6 = (int) (this.getWidth()-this.getWidth()/4.5);
        y6 = (int) (this.getHeight()/2.2);
    }
    /*
     * set the value for line 1
     */
    public void line1()
    {
        lsx1 = x3+size3/2;
        lsy1 = y3+size3/2;

    }
    /*
     * override the painrComponent function and set the animation attributes
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw image centered.
        long currentTime = System.nanoTime();
        if ( (currentTime-startTime) >= 31*Math.pow(10,9) && changedDelay == false)
        {
            delay = 4;
            timer.setDelay(delay);
            changedDelay = true;
        }
        if ( (currentTime-startTime) >= 30*Math.pow(10,9) && changedDelay == true)
        {
            delay = 12;
            timer.setDelay(delay);
            changedDelay = false;
        }
        animate1();
        animate2();
        animate3();
        animate4();
        animate5();
        animate6();
        g.setColor(new Color(220, 20, 60));
        g.fillOval(x1, y1, size1, size1);
        g.setColor(new Color(148,0,211));
        g.fillOval(x2, y2, size2, size2);
        g.setColor(new Color(0, color1, color2));
        g.fillOval(x3, y3, size3, size3);
        g.setColor(new Color(0, color3, color4));
        g.fillOval(x4, y4, size4, size4);
        g.setColor(new Color(0, 128, 128));
        g.fillOval(x5, y5, size5, size5);
        g.setColor(Color.YELLOW);
        g.fillOval(x6, y6, size6, size6);
        g.setColor(new Color(0, color1, color2));
//        Graphics2D g2d = (Graphics2D)g;
//        g2d.setStroke(new BasicStroke(this.getWidth()/20));
//        g2d.drawLine
    }
    /*
     * these functions are to change values of the attributes once it reach some point. 
     */
    //animation 1
    public void animate1()
    {
            if (size1 == this.getWidth()/7)
                larger1 = false;
            else if (size1 == this.getWidth()/15)
            {
                larger1 = true;
                x1 = this.getWidth()/10;
                y1 = this.getHeight()/2;
            }
            if (larger1)
            {
            if (size1%2==0)
            {
                x1--;
                y1--;
            }
            size1++;
            }else
            {
            if (size1%2==0)
            {
                x1++;
                y1++;
            }
            size1--;

            }
    }
    //animation 2
    public void animate2()
    {
            if (size2 == this.getWidth()/7)
            {
                larger2 = false;
                x2 = (int) (this.getWidth()/7.6);
                y2 = this.getHeight()/3;
            }
            else if (size2 == 1)
                larger2 = true;
            if (larger2)
            {
            if (size2%2==0)
            {
                x2--;
                y2--;
            }
            size2++;
            }else
            {
            if (size2%2==0)
            {
                x2++;
                y2++;
            }
            size2--;

            }
    }
    //animation 3
    public void animate3()
    {
        if (color2 == 0)
            changeColor1 = false;
        else if (color2 == 128)
            changeColor1 = true;
        if (changeColor1)
        {
            color2--;
            color1++;
        }
        else
        {
            color2++;
            color1--;
        }
    }
    //animation 4
    public void animate4()
    {
        if (color3 == 0)
            changeColor2 = false;
        else if (color3 == 128)
            changeColor2 = true;
        if (changeColor2)
        {
            color3--;
            color4++;
        }
        else
        {
            color4--;
            color3++;
        }
    }
    //animation 5
    public void animate5()
    {
            if (size5 == this.getWidth()/7)
            {
                larger3 = false;
                x5 = (int) (this.getWidth()-this.getWidth()/2.9);
                y5 = this.getHeight()/3;
            }
            else if (size5 == this.getWidth()/15)
            {
                larger3 = true;

            }
            if (larger3)
            {
            if (size5%2==0)
            {
                x5--;
                y5--;
            }
            size5++;
            }else
            {
            if (size5%2==0)
            {
                x5++;
                y5++;
            }
            size5--;

            }
    }
    //animation 6
    public void animate6()
    {
            if (size6 == this.getWidth()/6)
            {
                larger4 = false;
                x6 = (int) (this.getWidth()-this.getWidth()/4.5);
                y6 = (int) (this.getHeight()/2.2);
            }
            else if (size6 == 1)
                larger4 = true;
            if (larger4)
            {
            if (size6%2==0)
            {
                x6--;
                y6--;
            }
            size6++;
            }else
            {
            if (size6%2==0)
            {
                x6++;
                y6++;
            }
            size6--;

            }
    }

     /*
     * repaint the image at a certain time to create animation.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

