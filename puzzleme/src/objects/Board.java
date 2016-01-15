package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import maps.Maps;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import view.View;

/**
 *
 * @author Zhuocheng Yu
 */
public class Board extends JPanel implements MouseListener, MouseMotionListener
{
    //Mouse
    int xposition;
    int yposition;
    //Maps
    Maps maps = new Maps();
    List<Dot> fbf;
    List<Dot> nbn;
    //Dot
    int dot_counter;
    //Line
    Line line;
    //random variable
    private Random rand;
    //board variables
    private int WholeSize=600;
    private int BoardSize;
    private int Square_Number;
    private int Square_SideLength;
    private int numPairs;
    private int winSignal = 1;
    private Image image;
    private boolean init = true;
    public boolean reset;
    //Display stuff
    ImageIcon imageIcon;
    JLabel jLabel;
    JFrame frame;
    JPanel board;
    JPanel buttonPanel;
    JButton refresh;
    JButton previous;
    JButton next;
    //Maps index
    private int index=1;
    public static boolean nbnSelected;
    public static boolean fbfSelected;
    //View
    View view;
    //Color
    private Color color1=Color.blue,color2=Color.cyan;
    /*
     * constructor
     */
    public Board(View view)
    {
        this.view = view;
        addMouseListener(this);
        addMouseMotionListener(this);
        //Board Values
        if (fbfSelected)
        {
            Square_Number = 5;
        }
        else if (nbnSelected)
        {
            Square_Number = 9;
        }
        BoardSize=WholeSize/2;
        Square_SideLength = BoardSize / Square_Number;//the side length of one square inside the grid
        //Board Image
        image = new BufferedImage( WholeSize, WholeSize, BufferedImage.TYPE_INT_ARGB );
        //get the Maps values
        if (fbfSelected)
        {
        try 
        {
                index = 30;
        	maps.storeFBF(index);
        } 
        catch (FileNotFoundException ex) {} 
        catch (IOException ex){}
        fbf = maps.getFBF();
        }
        if (nbnSelected)
        {
        try 
        {
                index = 20;
        	maps.storeNBN(index);
        } 
        catch (FileNotFoundException ex) {} 
        catch (IOException ex) {}
        nbn = maps.getNBN();
        }
        board = this;
        //make a new Line
	line = new Line(board, Square_Number, WholeSize, numPairs);
    }
    /*
     * paint the grid for the board according to the user.
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(reset == true)
        	resetBoard(g);

        drawBoard(g);
        
        Draw_Dots(g);
        
        line.redrawGrid(g);
        
        line.redrawLines(g);
        
        if(line.eraseJ != -2)
        	line.eraseBox(g, line.eraseI, line.eraseJ);
        
        else
        {
        	if (line.drawSignal == 0)
        		line.drawHorizontal(g);
        	else if (line.drawSignal == 1)
        		line.drawVertical(g);
        } 
        
        line.redrawCorners(g);
        g.setColor(color1);
        g.drawRect(10,10,80,50);
        g.setColor(color2);
        g.drawString("Home", 34, 37);
    }
    /*
     * this function is call by the paintComponent to paint the background of the baord black
     */
    public void drawBoard(Graphics g)
    {
    	g.setColor(Color.black);
        g.fillRect(0, 0, WholeSize, WholeSize);
    }
    /*
     * reset the line and dot on the board
     */
    public void resetBoard(Graphics g)
    {
        // make a new line
    	line = new Line(board, Square_Number, WholeSize, numPairs);
        winSignal = 1;
    	init = true;
    	reset = false;
        if (fbfSelected)
        	numPairs = fbf.size() / 2;
        else if (nbnSelected)
        	numPairs = nbn.size() / 2;
    	Draw_Dots(g);
    }
    /*
     * this function use to check if the board is full with line or not.
     * user must fill the whole board to complete a certain level.
     */
    public boolean isFull()
    {
        for(int i = 0; i < Square_Number; i++)
        {
        for(int j = 0; j < Square_Number; j++)
        {
        if(line.objectsInSquare[i][j] < 2)
        return false;
        }
        }
        return true;
    }
    /*
     * draw the dots on the board according to the map coordinates from the text file.
     */
    public void Draw_Dots (Graphics graphics)
    {  
    	for(int j = 0; j < maps.size(); j++)
    	{
    		if (fbfSelected)
    		{
    			Dot d = fbf.get(j);
    			d.drawObject(graphics, convertToPixels(d.getRow()), convertToPixels(d.getCol()), Square_SideLength, Square_SideLength);
    			if(init == true)
    				storeDot(d);
    		}
    		else if (nbnSelected)
    		{
    			Dot d = nbn.get(j);
    			d.drawObject(graphics, convertToPixels(d.getRow()), convertToPixels(d.getCol()), Square_SideLength, Square_SideLength);
    			if(init == true)
    				storeDot(d);
    		}
    	}
    	init = false;
    }
    //read the data from a dot object and then store them. ( which mean it uses it to draw the objects on the board)
    public void storeDot(Dot d)
    {
    	int tempRow = d.getCol();
    	int tempCol = d.getRow();
    	Color tempColor = d.getColor();

    	line.objectsInSquare[tempRow][tempCol] = 1;
    	line.dotColor[tempRow][tempCol] = tempColor;	
    }
    /*
     * reset the map
     */
    public void resetMap() throws FileNotFoundException, IOException
    {
        if (fbfSelected)
        {
        	if (index >= maps.getNumberOfFiles())
        		return;
        	maps=new Maps();
        	maps.storeFBF(index);
        	fbf = maps.getFBF();
                numPairs = fbf.size()/2;
        }
        else if (nbnSelected)
        {   
             if (index >= maps.getNumberOfFilesNBN())
        		return;
            maps=new Maps();
            maps.storeNBN(index);
            nbn = maps.getNBN();
            numPairs = nbn.size()/2;
        }
    }
    
    //This functions converts simple coordinate system to the actual pixels
    public int convertToPixels(int num)
    {
        return (num*Square_SideLength+WholeSize/4);
    }
    //This function converts actual pixels to simple coordinate system
    public int convertToCoord(int num)
    {
        return ((Math.abs(num-WholeSize/4))/Square_SideLength);
    }
    // decrement the index value to jump to the next level
    public void previousIndex(){
        if (index > 1)
             index--;
    }
    // increment the index value to jump to the next level
    public void nextIndex(){
        if (index < maps.getNumberOfFiles() && fbfSelected)
            index++;
        else if (index < maps.getNumberOfFilesNBN() && nbnSelected)
            index++;
    }
    /*
     * set the index by the user so it can directly jump to the level.
     */
    public void setIndex(int value){
      if (fbfSelected)
      {
        index=value;
        reset=true;
        try {

            resetMap();
            numPairs = fbf.size() / 2;
        } catch (FileNotFoundException ex) {} 
        catch (IOException ex) {}
//        reset = true;
        repaint();
      }
      else if (nbnSelected)
      {
        index=value;
        reset=true;
        try {
            resetMap();
            numPairs = nbn.size() / 2;
        } catch (FileNotFoundException ex) {} 
        catch (IOException ex) {}
        reset = true;
        repaint();
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) 
    {

    }
    /*
     * activate the color and set the color for the line when it is press to a dot.
     * activate the home button when it is click in a certain range, which it brings the user back to the home screen
     */
    @Override
    public void mousePressed(MouseEvent e) 
    {
        if (e.getPoint().x>10 && e.getPoint().x<90 && e.getPoint().y>10 && e.getPoint().y<60)
        {
        	view.run();
        	board = null;
        }
        try
        {
        	Point mouse;
        	mouse = e.getPoint();
        	line.setCoordinates(mouse);
        	if(line.clickedADot == true)
        	{
        		line.setLineColor(line.currentI, line.currentJ);
        	}
        }
        catch (ArrayIndexOutOfBoundsException E)
        {}
    }
    /*
     * when the user realase the mouse during the drawing process, the line break  
     */
    @Override
    public void mouseReleased(MouseEvent e) 
    {
    	line.clickedADot = false;
    	line.breakLine();
    	line.foundDot = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    /*
     * this use to draw the line from one dot to another.
     * if the line connect both dots the line break
     * if all dots are connected but the board is not filled, give user a message saying "Not Quite - You must fill up the board!"
     * if all dots are connected and the board is filled, give user a message saying " Congratulations - Level Complete!"
     */
    @Override
    public void mouseDragged(MouseEvent e) 
    {
    	Point mouse;
    	if(line.clickedADot == true)
    	{
    		if(line.foundDot == false)
    		{
    			mouse = e.getPoint();
    			line.comparePositions(mouse);
    		}
    		if(line.numDotsFound == line.numDotPairs && winSignal == 1 && isFull())
                {
        		JOptionPane.showMessageDialog(null, "Congratulations - Level Complete!");
                        winSignal = 0;
                }
    		if(line.numDotsFound == line.numDotPairs && winSignal == 1 && !isFull())
                {
        		JOptionPane.showMessageDialog(null, "Not Quite - You must fill up the board!");
                        winSignal = 0;
                }
    	}
    }
    /*
     * This is to high light the home button on the board.
     */
    @Override
    public void mouseMoved(MouseEvent e) 
    {
        if (e.getPoint().x>10 && e.getPoint().x<90 && e.getPoint().y>10 && e.getPoint().y<60)
        {
            color2=Color.red;
            repaint();
        }
        else
        {
        	color2=Color.cyan;
        	repaint();
        }
        
    }
    
}
