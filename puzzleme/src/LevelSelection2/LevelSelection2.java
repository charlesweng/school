package LevelSelection2;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import objects.Board;
import view.View;

/**
 *
 * @author julio
 */
public class LevelSelection2 extends JPanel{
   View view;
   Board board;

   JButton buttons[]=new JButton[20];
   

   Font large = new Font("Arial", Font.BOLD, 24);
  
  //Declare Border attributes 
   private Border screen=BorderFactory.createLineBorder(Color.RED,5, true);
   private Border title=BorderFactory.createTitledBorder(screen, "Level Selection", TitledBorder.CENTER, TitledBorder.TOP, large,Color.WHITE);
   /*
    * Constructor
    */
   public LevelSelection2(View view,Board board) 
   {
       //initialize the view
       this.view=view;
       this.board=board;
       
       //set the properties for the JPanel
       setBackground(Color.black);
       setSize(590,550);
       setBorder(title);
       
       //set Layout and button.
       GridLayout layout=new GridLayout(6,5);
       setLayout(layout);
       addButton();
       initButtons2();
       //set the Panel visible
       setVisible(true);
       
   }
   
   /*
    * this function use to add buttons and set borders for each button.llll
    */
    private void addButton()  
    {
        //fillArray();
        fillButtons();
        for(int j=0;j<buttons.length;j++)
        {  
            //JButton button=new JButton(name[i]);
            buttons[j].setBackground(Color.blue);
            buttons[j].setForeground(Color.WHITE);
            Border thickBorder = new LineBorder(Color.black, 12);
            buttons[j].setBorder(thickBorder);
            if(j%2==0){
                Icon imgicon=new ImageIcon("dot11.png");
                buttons[j].setIcon(imgicon);
                add(buttons[j]);
            }
            else{
            Icon imgicon = new ImageIcon("dot22.png");
            buttons[j].setIcon(imgicon);
            add(buttons[j]);
            }
        }
        
    }
   
    /*
     * Store the buttons in an array.
     */
    public void fillButtons()//had nested loop before with the outer loop being the string list
    {
          for(int j=0;j<buttons.length;j++)
           {
             buttons[j]=new JButton();
             buttons[j].setText("Level"+" "+Integer.toString(j+1));
           }
        
    }
    /*
     * Initialize the actions that will perform by each buttons.
     * each action is to display the board.
     */
    
    public void initButtons2(){
        buttons[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {
                   board.setIndex(1);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
               
            });
           
            buttons[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(2);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
            
            buttons[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(3);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
            
           buttons[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(4);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
           
           buttons[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(5);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
           
           buttons[5].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(6);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });    
           
           buttons[6].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(7);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
           
           buttons[7].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(8);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });  
           
          buttons[8].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(9);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[9].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(10);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });  
          
          buttons[10].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(11);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[11].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(12);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
          
          buttons[12].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(13);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });  
          
          buttons[13].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(14);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[14].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(15);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });  
          
          buttons[15].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(16);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });  
          
          buttons[16].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(17);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[17].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(18);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
          
          buttons[18].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(19);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[19].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(20);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
    }
   
 }