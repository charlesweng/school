/*
 * Brief Overview Of How To Solve The Easy Phone Dialing Problem
 * ============================================================
 * 1. Represent the input in a form of an array
 * 2. Represent the keypad in a form of a matrix (double array)
 * 3. Loop over the the input and easily calculate whether all the numbers 
 * Another Way of Solving This
 * ============================================================
 * 1. Represent input the same way
 * 2. Creating a single ArrayList of digits on the keypad
 *  a. Each key contains north, northeast, east, southeast, south, southwest, west, northwest that points to another key on the keypad
 * 3. Loop over the input and use the Arraylist as a sort of a map 
 * are adjacent next to each other
 *
 * Basically - Adjacency Matrix vs Adjacency List 
 */

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
public class EasyPhoneDialing {
  //used for returning an invalid phone number
  private static final int[] INVALID_NUMBER = {};
 /* We can also treat 0 as a special case and just add extra if statments in the function
 */
  //adjacency matrix
  private static int keypad[][] = {
                    // 0,1,2,3,4,5,6,7,8,9
                /*0*/{ 1,0,0,0,0,0,0,1,1,1},
                /*1*/{ 0,1,1,0,1,1,0,0,0,0},
                /*2*/{ 0,1,1,1,1,1,1,0,0,0},
                /*3*/{ 0,0,1,1,0,1,1,0,0,0},
                /*4*/{ 0,1,1,0,1,1,0,1,1,0},
                /*5*/{ 0,1,1,1,1,1,1,1,1,1},
                /*6*/{ 0,0,1,1,0,1,1,0,1,1},
                /*7*/{ 1,0,0,0,1,1,0,1,1,0},
                /*8*/{ 1,0,0,0,1,1,1,1,1,1},
                /*9*/{ 1,0,0,0,0,1,1,0,1,1},
                     };
  //adjacency list
  private Map<Integer ,ArrayList<Integer>> keypad1;
  
  //used for formatting, extrapolated the var just in case we wanted to add more
  private static String regex = "[\\s\\-()\\.]";


  // My own parsing func (Java has its own)
  public static boolean validate(String phoneNumber)
  {
    return (phoneNumber.length() == 10 && phoneNumber.matches("-?\\d+?"));
  }
  public static int[] parse(String phoneNumber)
  {
    //annoying regex stuff
    String formattedNum = phoneNumber.replaceAll(regex,"");
    //should probably do some error checking for invalid numbers here
    if (!validate(formattedNum))
    {
      System.err.println("Invalid Number. Try Again.");  
      return INVALID_NUMBER;
    }
    //parsing
    int[] parsedPhoneNum = new int[formattedNum.length()];
    for (int i = 0; i < formattedNum.length(); i++)
    {
      parsedPhoneNum[i] = Integer.parseInt(Character.toString(formattedNum.charAt(i)));
    }
    return parsedPhoneNum;
  }

  // bread and butter of this assignment
  public static boolean checkIfEasy(int[] inputNum)
  {
    //initial for loop through the input
    boolean easy = true;
    for (int i = 0; i < inputNum.length-1; i++)
    {
      if (keypad[inputNum[i]][inputNum[i+1]]==0)
      {
        System.out.println(inputNum[i]+" is not adjacent to "+inputNum[i+1]);
        return false;
      }
    }
    return true;
  }
  private ArrayList<Integer> makeAdjacencyList(int[] values)
  {
    ArrayList<Integer> adjNum = new ArrayList<Integer>();
    for (int i = 0; i < values.length;i++)
    {
      adjNum.add(values[i]);
    }
    return adjNum;
  }
  public void makeKeyPadList()
  {
    keypad1 = new HashMap<Integer,ArrayList<Integer>>();
    keypad1.put(0,makeAdjacencyList(new int[]{0,7,8,9}));
    keypad1.put(1,makeAdjacencyList(new int[]{1,2,4,5}));
    keypad1.put(2,makeAdjacencyList(new int[]{1,2,3,4,5,6}));
    keypad1.put(3,makeAdjacencyList(new int[]{2,3,5,6}));
    keypad1.put(4,makeAdjacencyList(new int[]{1,2,4,5,7,8}));
    keypad1.put(5,makeAdjacencyList(new int[]{1,2,3,4,5,6,7,8,9}));
    keypad1.put(6,makeAdjacencyList(new int[]{2,3,5,6,8,9}));
    keypad1.put(7,makeAdjacencyList(new int[]{0,4,5,7,8}));
    keypad1.put(8,makeAdjacencyList(new int[]{0,4,5,6,7,8,9}));
    keypad1.put(9,makeAdjacencyList(new int[]{0,5,6,8,9}));
  }
  
  public Map<Integer,ArrayList<Integer>> getKeyPad()
  {
    return keypad1;
  }
  
  public boolean easyPhoneNumber(int[] input)
  {
    for (int i = 0; i < input.length-1; i++)
    {

      if (!keypad1.get(input[i]).contains(input[i+1]))
      {
        System.out.println(input[i]+" is not adjacent to "+input[i+1]);
        return false;
      }
    }
    return true;
  }

  //Main Program
  public static void main(String[] args) {
    boolean more = true; 
    Scanner input = new Scanner(System.in);
    while (more)
    {

      //initial parsing and error checking
      System.out.println("Enter your phone number: ");
      String inputNum = input.nextLine();
      int[] phoneNum = EasyPhoneDialing.parse(inputNum); 
      if (phoneNum.length == 0)
      {
        continue;
      }
      System.out.println();
      
      //adjacency matrix method
      System.out.println("Adjacency Matrix Method");
      System.out.println("------------------------");
      System.out.println("Adjacency Matrix Structure");
      for (int i = 0; i < EasyPhoneDialing.keypad.length;i++)
      {
        for (int j = 0; j < EasyPhoneDialing.keypad[i].length;j++)
          System.out.print(EasyPhoneDialing.keypad[i][j]+" ");
        System.out.println();
      }
      System.out.println("------------------------");
      if (checkIfEasy(phoneNum))
        System.out.println("Easy Phone Number");
      else
        System.out.println("Hard Phone Number");
      System.out.println();

      //adjacency list method
      EasyPhoneDialing phoneObj = new EasyPhoneDialing();
      phoneObj.makeKeyPadList();
      System.out.println("Adjacency List Method");
      System.out.println("------------------------");
      System.out.println("Adjacency List Structure");
      Map<Integer,ArrayList<Integer>> keypad1 = phoneObj.getKeyPad();
      for (int i = 0; i < keypad1.keySet().size();i++)
      {
        System.out.print(i+" -> ");
        ArrayList<Integer> adjList = keypad1.get(i);
        for (int j = 0; j < adjList.size();j++)
        {
          System.out.print(adjList.get(j)+" ");
        }
        System.out.println();
      }
      System.out.println("------------------------");
      if (phoneObj.easyPhoneNumber(phoneNum))
        System.out.println("Easy Phone Number");
      else
        System.out.println("Hard Phone Number");
      System.out.println();

      System.out.println();
      //UI continuation stuff
      while (true)
      {
        System.out.println("Do you want to enter more phone numbers? (Y/N)"); 
        String yesorno = input.nextLine(); 
        if (yesorno.matches("[Yy][Ee][Ss]|[Yy]"))
        { 
          break; 
        }
        else if (yesorno.matches("[Nn][Oo]|[Nn]"))
        { 
          more = false; 
          break; 
        }
      }//while true
    }//while more
  }//main
}//class
