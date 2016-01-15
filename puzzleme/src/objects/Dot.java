package objects;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author julio
 */
public class Dot
{
    
   //declare row,col and color
   private int row;
   private int col;
   private Color c;
   /*
    * constructor
    */
   public Dot(int x, int y, Color color)
   {
       //set the values
       row = x;
       col = y;
       c = color;
   }
   /*
    * return each value.
    */
   public int getRow(){return row;}
   public int getCol(){return col;}
   public Color getColor(){return c;}
   //draw the dots on the screen.
   public void drawObject(Graphics graphics, int top_leftX, int top_leftY, int length, int width )
    {
        graphics.setColor(c);
        graphics.fillOval(top_leftX, top_leftY, length, width);
    }

}
