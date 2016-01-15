package objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Arrays;
import javax.swing.JPanel;

public class Line extends JPanel
{
	public int xPosition, yPosition, numSquares, offset, wholeSize, boxSize, currentI, currentJ, eraseI = -2, eraseJ = -2, drawSignal = 2, lineWidth, arrayPosition = 0, colorCount = 0;
	public int numDotPairs, numDotsFound;
	
	// virtual board that stores what objects are in particular squares: 0 represents empty, 1 represents dot, 2 represents line, 3 represents dot and line
	public int[][] objectsInSquare; 
	
	//dotColor is initialized when a new line is called, boxColor changes with mouse drag information
	public Color[][] dotColor, boxColor;
	
	//stores what order to redraw the lines
	public Color[] repaintColor;
	public Color currentColor;
	public boolean movingRight, movingDown, clickedADot, foundDot;
	
	//this array stores the order that the boxes need to be redrawn in
	public int[] linePositions;
	
	private JPanel board;
	
	public Line(JPanel panel, int squares, int wholesize, int numPairs)
	{
		board = panel;
		numSquares = squares;
		numDotPairs = numPairs;
		wholeSize = wholesize;
		
		//set the window padding to 1/4 of the window size
		offset = wholeSize / 4;
        
		//set the size of the boxes and line width
		boxSize = wholeSize / numSquares / 2;
		lineWidth = boxSize / 4;
		
		//initialize the arrays sizes to the total number of squares that you have
		objectsInSquare = new int[numSquares][numSquares];
		dotColor = new Color[numSquares][numSquares];
		boxColor = new Color[numSquares][numSquares];
		
		//10 is just an arbitrary size for the color array
		repaintColor = new Color[10];
		
		//the size of the linePositions array is greater than the others
		//it needs to be 4 times as big as the total number of squares because there are 4 coordinates per line that you draw
		//it also needs even more space because you have to account for line breaks
		linePositions = new int[numSquares*numSquares*4 + 4*numDotPairs];
		
		//initialize it to an impossible value so there are no redraw errors
		Arrays.fill(linePositions, -1);
	}	

    public void setLineColor(int i, int j)
    {
    	//set the current color to the dot color
    	currentColor = dotColor[i][j];
    	//update the virtual board to a dot and a line
    	objectsInSquare[i][j] = 3;
    	//set the box color to the current color
    	boxColor[i][j] = currentColor;
    	//update the order for color redrawing
    	storeRepaintColor(currentColor);
    }
	
    public void setCoordinates(Point p)
    {
    	int xTemp = boxSize, yTemp = boxSize;
    	boolean xFound = false, yFound = false;
    	currentI = 0; currentJ = 0;
    	
    	//given a mouse click, set the current row and column position
    	//and set the xPosition and yPosition, used for setting the draw location
    	while (xFound == false)
    	{
    		if (xTemp + offset > p.getX())
    		{
    			while (yFound == false)
    			{
    				if (yTemp + offset > p.getY())
    				{
    					xPosition = xTemp - boxSize/2;
    					yPosition = yTemp - boxSize/2;
    					yFound = true;
    				}	
    				else
    				{
    					yTemp += boxSize;
    					currentI++;
    				}    				
    			}
    			xFound = true;
    		 }
    		 else
    		 {
    			 xTemp += boxSize;
    			 currentJ++;
    		 }
    	}
    	 
    	//if there is a dot at your current location, you have clicked on a dot
    	if(objectsInSquare[currentI][currentJ] == 1)
    		clickedADot = true;
    }	
    
    //this function is essentially the same thing 4 times, so I only commented one of the 4
    public void comparePositions(Point p)
    {  
    	//the try catch is used to make sure the user doesn't draw outside of the board
        try
        {
    	//if the mouse moves in to the box to the right
    	if(p.getX() >= offset + xPosition + boxSize / 2)
    	{
    		//if your current box contains a dot, update it to a dot and a line
    		if(objectsInSquare[currentI][currentJ] == 1)
    			objectsInSquare[currentI][currentJ] = 3;
    		
    		//if the square to the right contains either a dot or is empty
    		if(objectsInSquare[currentI][currentJ + 1] < 2)
    		{
    			//if it has a dot
    			if(objectsInSquare[currentI][currentJ + 1] == 1)
    			{
    				//and that dot has the matching color
    				if(dotColor[currentI][currentJ + 1] == currentColor)
    				{
    					//this is a signal for the mouse listener in board
    					foundDot = true;
    					
    					//increment the number of dots found, used in calculating whether the level has been completed
    					numDotsFound++;
    					
    					//update the virtual board at that position to a dot and line
    					objectsInSquare[currentI][currentJ + 1] = 3;
    					
    					//drawSignal is used in paintComponent to determine whether to draw a horizontal or vertical line
    					drawSignal = 0;	
    					
    					//movingRight determines whether to draw to the left or to the right
    	    			movingRight = true;
    	    			
    	    			board.repaint();
    	    			//ideally xPosition would be incremented here but board.repaint isn't called immediately
    	    			//xPosition is used to determine the draw location, so if it increments before the redraw function, it would draw inaccurately 
    	    			
    	    			//this block of code updates the virtual line, which is used in redrawLines
    	    			linePositions[arrayPosition] = currentI;
    	    			arrayPosition++;
    	    			linePositions[arrayPosition] = currentJ;
    	    			arrayPosition++;
    	    			linePositions[arrayPosition] = currentI;
    	    			arrayPosition++;
    	    			linePositions[arrayPosition] = currentJ + 1;
    	    			arrayPosition++;
    	    			
    	    			//set the color of the box to the appropriate value
    	    			boxColor[currentI][currentJ + 1] = currentColor;
    	    			
    	    			//update the current position
    	    			currentJ++;
    				}
    			}
    			
    			//otherwise, if the square to the right is empty
    			else if(objectsInSquare[currentI][currentJ + 1] == 0)
    			{
    				//set the virtual grid in that position to a line
        			objectsInSquare[currentI][currentJ + 1] = 2;
        		
        			//signal to draw a horizontal line
        			drawSignal = 0;
        			
        			board.repaint();
    			
	    			linePositions[arrayPosition] = currentI;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentI;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ + 1;
	    			arrayPosition++;
	    			
	    			boxColor[currentI][currentJ + 1] = currentColor;
	    			
	    			currentJ++;
	    			
	    			movingRight = true;
    			}
    		}
    		
    		//otherwise, if the box to the right contains a line you need to check if you can erase it
    		else
    		{
    			//check if the line is the same color as the current color
    			if(boxColor[currentI][currentJ + 1] == currentColor)
    			{
    				//making sure you don't get any invalid information
    				if(linePositions[arrayPosition - 3] != -1)
    				{
    					//set the last known location to the previous x and y coordinate
    					int lastJ;
    					lastJ = linePositions[arrayPosition - 3];
    					
    					//if that last known location is the box you are trying to move into, you know you can erase that line
    					if(lastJ == currentJ + 1)
    					{
    						//if the square you are moving in to has a dot and a line, reset it to just a dot
    						if(objectsInSquare[currentI][currentJ + 1] == 3)
    							objectsInSquare[currentI][currentJ + 1] = 1;
    						
    						//eraseI and eraseJ are used as triggers in paintComponent and as starting points for the erase function
    						eraseI = currentI;
    						eraseJ = currentJ;
    						
    						//reset the value of the square you just were to empty
    						objectsInSquare[currentI][currentJ] = 0;
    						
    						//update the current grid coordinate
    						currentJ++;
    						
    						
    						//movingRight tells the erase function to erase to the right
    						movingRight = true;
    						
    						//update the current comparison coordinate
    						xPosition += boxSize;
    						
    						board.repaint();
    					}
    				}
    			}
    		}
    	}
    	
    	//same code as above except you are drawing to the left
    	else if(p.getX() <= offset + xPosition - boxSize / 2)
    	{
    		if(objectsInSquare[currentI][currentJ] == 1)
				objectsInSquare[currentI][currentJ] = 3;
    		
    		if (objectsInSquare[currentI][currentJ - 1] < 2)
    		{
    			if(objectsInSquare[currentI][currentJ - 1] == 1)
    			{
    				if(dotColor[currentI][currentJ - 1] == currentColor)
    				{
    					foundDot = true;
    					numDotsFound++;
    					objectsInSquare[currentI][currentJ - 1] = 3;
		    			drawSignal = 0;
		    			xPosition -= boxSize;
		    			board.repaint();
		    			
		    			linePositions[arrayPosition] = currentI;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentJ;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentI;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentJ - 1;
		    			arrayPosition++;
    			
		    			boxColor[currentI][currentJ - 1] = currentColor;
		    			
		    			currentJ--;
		    			movingRight = false;
    			
    				}
    			}
  
    			else if(objectsInSquare[currentI][currentJ - 1] == 0)
    			{
        			objectsInSquare[currentI][currentJ - 1] = 2;
        			
        			drawSignal = 0;
	    			xPosition -= boxSize;
	    			board.repaint();
	    			
	    			linePositions[arrayPosition] = currentI;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentI;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ - 1;
	    			arrayPosition++;
			
	    			boxColor[currentI][currentJ - 1] = currentColor;
	    			
	    			currentJ--;
	    			movingRight = false;	
    			}
    			
    		}
    		
    		else
    		{
    			if(boxColor[currentI][currentJ - 1] == currentColor)
    			{
    				if(linePositions[arrayPosition - 3] != -1)
    				{
    					if(objectsInSquare[currentI][currentJ - 1] == 3)
							objectsInSquare[currentI][currentJ - 1] = 1;
    					int lastJ;
    					lastJ = linePositions[arrayPosition - 3];
    					if(lastJ == currentJ - 1)
    					{
    						eraseI = currentI;
    						eraseJ = currentJ - 1;
    						objectsInSquare[currentI][currentJ] = 0;
    						currentJ--;
    						movingRight = false;
    						xPosition -= boxSize;
    						board.repaint();
    					}
    				}
    			}
    		}
    	}
    	
    	//same code as above except you are drawing downwards
    	if(p.getY() >= offset + yPosition + boxSize / 2)
    	{
    		if(objectsInSquare[currentI][currentJ] == 1)
				objectsInSquare[currentI][currentJ] = 3;
    		
    		if (objectsInSquare[currentI + 1][currentJ] < 2)
    		{
    			if(objectsInSquare[currentI + 1][currentJ] == 1)
    			{
    				if(dotColor[currentI + 1][currentJ] == currentColor)
    				{
    					foundDot = true;
    					numDotsFound++;
    					objectsInSquare[currentI + 1][currentJ] = 3;
		    			drawSignal = 1;
		    			board.repaint();
		    			
		    			linePositions[arrayPosition] = currentI;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentJ;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentI + 1;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentJ;
		    			arrayPosition++;
		    			
		    			boxColor[currentI + 1][currentJ] = currentColor;
		    			
		    			currentI++;
		    			movingDown = true;
    				}
    			}
    			
    			else if(objectsInSquare[currentI + 1][currentJ] == 0)
    			{
        			objectsInSquare[currentI + 1][currentJ] = 2;
        		
        			drawSignal = 1;
	    			board.repaint();
	    			
	    			linePositions[arrayPosition] = currentI;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentI + 1;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ;
	    			arrayPosition++;
	    			
	    			boxColor[currentI + 1][currentJ] = currentColor;
	    			
	    			currentI++;
	    			movingDown = true;
    			}
    		}
    		
    		else
    		{
    			if(boxColor[currentI + 1][currentJ] == currentColor)
    			{
    				if(linePositions[arrayPosition - 4] != -1)
    				{
    					if(objectsInSquare[currentI + 1][currentJ] == 3)
							objectsInSquare[currentI + 1][currentJ] = 1;
    					int lastI;
    					lastI = linePositions[arrayPosition - 4];
    					if(lastI == currentI + 1)
    					{
    						eraseI = currentI;
    						eraseJ = currentJ;
    						objectsInSquare[currentI][currentJ] = 0;
    						currentI++;
    						movingDown = true;
    						yPosition += boxSize;
    						board.repaint();
    					}
    				}
    			}
    		}
    	}
    	
    	//same code except you are drawing upwards
    	else if(p.getY() <= offset + yPosition - boxSize / 2)
    	{
    		if(objectsInSquare[currentI][currentJ] == 1)
				objectsInSquare[currentI][currentJ] = 3;
    		
    		if (objectsInSquare[currentI - 1][currentJ] < 2)
    		{
    			if(objectsInSquare[currentI - 1][currentJ] == 1)
    			{
    				if(dotColor[currentI - 1][currentJ] == currentColor)
    				{
    					foundDot = true;
    					numDotsFound++;
    					objectsInSquare[currentI - 1][currentJ] = 3;
		    			drawSignal = 1;
		    			yPosition -= boxSize;
		    			board.repaint();
		    			
		    			linePositions[arrayPosition] = currentI;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentJ;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentI - 1;
		    			arrayPosition++;
		    			linePositions[arrayPosition] = currentJ;
		    			arrayPosition++;
		    			
		    			boxColor[currentI - 1][currentJ] = currentColor;
		    			
		    			currentI--;
		    			movingDown = false;
    				}
    			}
    			
    			else if(objectsInSquare[currentI - 1][currentJ] == 0)
    			{
    				objectsInSquare[currentI - 1][currentJ] = 2;
    				drawSignal = 1;
	    			yPosition -= boxSize;
	    			board.repaint();
	    			
	    			linePositions[arrayPosition] = currentI;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentI - 1;
	    			arrayPosition++;
	    			linePositions[arrayPosition] = currentJ;
	    			arrayPosition++;
	    			
	    			boxColor[currentI - 1][currentJ] = currentColor;
	    			
	    			currentI--;
	    			movingDown = false;
    			}
    		}
    		
    		else
    		{
    			if(boxColor[currentI - 1][currentJ] == currentColor)
    			{
    				if(linePositions[arrayPosition - 4] != -1)
    				{
    					if(objectsInSquare[currentI - 1][currentJ] == 3)
							objectsInSquare[currentI - 1][currentJ] = 1;
    					int lastI;
    					lastI = linePositions[arrayPosition - 4];
    					if(lastI == currentI - 1)
    					{
    						eraseI = currentI - 1;
    						eraseJ = currentJ;
    						objectsInSquare[currentI][currentJ] = 0;
    						currentI--;
    						movingDown = false;
    						yPosition -= boxSize;
    						board.repaint();
    					}
    				}
    			}
    		}
    	}
        }
        //don't throw any messages if the mouse goes outside the board
        catch (ArrayIndexOutOfBoundsException e)
        {
        }
    }
    
    public void redrawLines(Graphics g)
    {
    	int j = 0, tempCount = 0;
    	//set the color to the first color in the stored array
    	g.setColor(repaintColor[0]);
    	
    	//loop through the entire amount of stored lines
    	//arrayPosition is divided by 4 because every line is technically 4 individual coordinates
    	for(int i = 0; i < arrayPosition / 4; i++)
    	{
    		int startX, startY, endX, endY;
    		
    		//if you have reached a break in the line, you know you have to move on to the next color
    		if(linePositions[j] == -2)
    		{
    			tempCount++;
    			g.setColor(repaintColor[tempCount]);
    		}
    		
    		//startX, startY, endX, endY are the starting and ending positions of the lines
    		startX = boxSize * linePositions[j + 1] + boxSize / 2;
    		startY = boxSize * linePositions[j] + boxSize / 2;
    		endX = boxSize * linePositions[j + 3] + boxSize / 2;
    		endY = boxSize * linePositions[j + 2] + boxSize / 2;
   	
    		//if the starting x location isn't the same as the ending x position, you know you have to draw horizontally
    		if(startX != endX)
    		{
    			//if you have a valid starting location
    			if(startX > 0)
    			{
    				//if the startX is less than the endX, you draw to the right
	    			if(startX < endX)
	    				g.fillRect(offset + startX, offset + startY - lineWidth / 2, boxSize, lineWidth);
	    			//otherwise you draw to the left
	    			else
	    				g.fillRect(offset + startX - boxSize, offset + startY - lineWidth / 2, boxSize, lineWidth);
    			}
    		}
    		//otherwise you are drawing vertically
    		else
    		{
    			//checking validity
    			if(startY > 0)
    			{
    				//draw down
	    			if(startY < endY)
	    				g.fillRect(offset + startX - lineWidth / 2, offset + startY, lineWidth, boxSize);
	    			//draw up
	    			else
	    				g.fillRect(offset + startX - lineWidth / 2, offset + startY - boxSize, lineWidth, boxSize);
    			}
    		}
    		//increment j by 4 because you have just looked at 4 coordinates
    		j += 4;
    	}
    }
    
    //function that adds colors to the array used for redrawing
    public void storeRepaintColor(Color c)
    {
    	repaintColor[colorCount] = c;
    	colorCount++;
    }
    
    //this function is used to identify when redrawLines needs to move to the next color
    public void breakLine()
    {
    	for(int i = 0; i < 4; i++)
    	{
    		//-2 is used as a signal value
    		linePositions[arrayPosition] = -2;
    		arrayPosition++;
    	}
    }
    
    //this function is necessary because otherwise there would be small boxes cut out of the lines
    //it basically checks if there is a line in every box then just draws a square in the middle of the boxes with lines
    public void redrawCorners(Graphics g)
    {
    	for(int i = 0; i < numSquares; i++)
    	{	
    		for(int j = 0; j < numSquares; j++)
    		{	
    			if(objectsInSquare[i][j] > 1)
    			{
    				g.setColor(boxColor[i][j]);
    				g.fillRect(offset + j*boxSize + boxSize/2 - lineWidth/2, offset + i*boxSize + boxSize/2 - lineWidth/2, lineWidth, lineWidth);
    			}
    		}
		}
    }
    
    //when repaint is called, the grid needs to be redrawn
    public void redrawGrid(Graphics g)
    {
    	g.setColor(Color.white);
    	
    	int tempX = boxSize, tempY = boxSize;
    	for (int i = 0; i <= numSquares; i++)
    	{
    		g.fillRect(offset + i*tempX, offset, 1, wholeSize/2);
         	g.fillRect(offset, offset + i*tempY, wholeSize/2 - 2, 1);
    	}
    }
    
    //this function is used for erasing lines
    public void eraseBox(Graphics g, int iPoint, int jPoint)
    {
    	//remove the linePositions data for the last line drawn
    	for(int i = 1; i < 5; i++)
    		linePositions[arrayPosition - i] = -1;
    	
    	//decrement by 4 because of the 4 line coordinates
    	arrayPosition -= 4;
    	
    	//reset the erase signals
    	eraseI = -2;
    	eraseJ = -2;
    	board.repaint();
    }
    
    //draws a horizontal line based on current xPosition
    public void drawHorizontal(Graphics g)
    {
    	g.fillRect(offset + xPosition, offset + yPosition - lineWidth / 2, boxSize, lineWidth);
    	//if you are moving right, increment the xPosition, otherwise it is done for you in comparePositions
    	if(movingRight)
    		xPosition += boxSize;
    	//reset the draw signal
    	drawSignal = 2;
    }

    //draws a vertical line based on current yPosition
    public void drawVertical(Graphics g)
    {
    	g.fillRect(offset + xPosition - lineWidth / 2, offset + yPosition, lineWidth, boxSize);
    	//if you are moving down, increment the yPosition, otherwise it is done for you in comparePositions
    	if(movingDown)
    		yPosition += boxSize;
    	//reset the drawSignal
    	drawSignal = 2;
    }
}

