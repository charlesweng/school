package maps;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aura Roy
 */
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.URL;
import java.util.*;
import objects.Dot;


public class Maps {
    //declare attributes of arrayList and map
    ArrayList<Dot> dotPositions;
    ArrayList<Dot> dotPositionsNBN;
    Map<String, Color> colorMap;
    //declare list attributes and file attributes
    int size=0;
    int numberOfFiles = 0;
    int numberOfFilesNBN = 0;
    //declare a newline attributes to seperate the line.
    public static String newline = System.getProperty("line.separator");
    /*
     * constructor
     */
    public Maps()
    {
        dotPositions = new ArrayList<Dot>();
        dotPositionsNBN = new ArrayList<Dot>();
        colorMap = new HashMap<>();
        colorMap.put("WHITE", Color.WHITE);
        colorMap.put("CYAN", Color.CYAN);
        colorMap.put("MAGENTA", Color.MAGENTA);
        colorMap.put("PINK", Color.PINK);
        colorMap.put("GRAY", Color.GRAY);
        colorMap.put("BLUE", Color.BLUE);
        colorMap.put("GREEN", Color.GREEN);
        colorMap.put("YELLOW", Color.YELLOW.brighter());
        colorMap.put("ORANGE", Color.ORANGE);
        colorMap.put("RED", Color.RED);
        
        
    }
    /*
     * this function return the number of file(Five by Five) that are being read.
     */
    public int getNumberOfFiles()
    {
        int i = 1;
        URL url = this.getClass().getResource("FBF"+i+".txt");
        while (url != null)
        {
            //increment the number of file accordingly.
            numberOfFiles++;
            i++;
            url = this.getClass().getResource("FBF"+i+".txt");
        }
        return numberOfFiles;
    }
     /*
     * this function return the number of file(Nine by Nine) that are being read.
     */
    public int getNumberOfFilesNBN()
    {
        int i = 1;
        URL url = this.getClass().getResource("NBN"+i+".txt");
        while (url != null)
        {
            //increment the number of NBN file accordingly
            numberOfFilesNBN++;
            i++;
            url = this.getClass().getResource("NBN"+i+".txt");
        }
        return numberOfFilesNBN;
    }
    /*
     * this function store a FBF file's information into a dot class.
     */
    public void storeFBF(int index) throws FileNotFoundException, IOException
    {
        //This takes the file from the current directory
        InputStream is = getClass().getResourceAsStream("FBF"+index+".txt");
        InputStreamReader isr = null;
        String fileString = "";
		try {
                        isr = new InputStreamReader(is);
                        int content;
			while ((content = isr.read()) != -1) {
                                fileString += (char) content;
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
           fileString = fileString.replaceAll(newline, "\n");
           String[] lines = fileString.split("\n");
           //for every line, parse the information and store them into a new dot object
           for (String line: lines)
           {
               line = line.replaceAll("\n", "");
               String[] term = line.split(" ");
               int x = Integer.parseInt(term[0]);
               int y = Integer.parseInt(term[1]);
               String c = term[2];
               Color z = colorMap.get(c);
               Dot dot = new Dot(x, y, z);
               dotPositions.add(dot);
               size++;
           }
    }
    /*
     * store a nbn file's information into a dot object.
     */
    public void storeNBN(int indexNBN) throws FileNotFoundException, IOException
    {
        InputStream is = getClass().getResourceAsStream("NBN"+indexNBN+".txt");
        InputStreamReader isr = null;
        String fileString = "";
		try {
                        isr = new InputStreamReader(is);
 			int content;
			while ((content = isr.read()) != -1) {
                                fileString += (char) content;
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
           fileString = fileString.replaceAll(newline, "\n");
           String[] lines = fileString.split("\n");
            //for every line, parse the information and store them into a new dot object
           for (String line: lines)
           {
               line = line.replaceAll("\n", "");
               String[] term = line.split(" ");
               int x = Integer.parseInt(term[0]);
               int y = Integer.parseInt(term[1]);
               String c = term[2];
               Color z = colorMap.get(c);
               Dot dot = new Dot(x, y, z);
               dotPositionsNBN.add(dot);
               size++;
           }
    }
    /*
     * return the size
     */
    public int size(){
        return size;
    }
    /*
     * print out the current row , col , and color information that are store in the dot classes
     */
    public void print()
    {
        for ( Dot d: dotPositions)
        {
            System.out.print(d.getRow()+" ");
            System.out.print(d.getCol()+" ");
            System.out.println(d.getColor());
        }
    }
    
    /*
     * return FBF list
     */
     public List<Dot> getFBF()
    {
        return dotPositions;
    }
     /*
      * return NBN list
      */
     public List<Dot> getNBN()
    {
        return dotPositionsNBN;
    }

}