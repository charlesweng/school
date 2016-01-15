/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package levelselection;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
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
public class LevelSelection extends JPanel{
    //declare attributes
    View view;
    Board board;
    JButton buttons[]=new JButton[30];
    Font large = new Font("Arial", Font.BOLD, 24);
    private Border screen=BorderFactory.createLineBorder(Color.YELLOW,5, true);
    private Border title=BorderFactory.createTitledBorder(screen, "Level Selection", TitledBorder.CENTER, TitledBorder.TOP, large,Color.WHITE);
    private Border title2=BorderFactory.createTitledBorder(screen, "Connect", TitledBorder.CENTER, TitledBorder.TOP, large,Color.WHITE);
   /*
    * constructor
    */
   public LevelSelection(View view, Board board) 
   {
       //set the view attributes
       this.view = view;
       this.board = board;
       setBackground(Color.black);
       setSize(590, 550);
       setBorder(title);
       //set gridlayout and buttons
       GridLayout layout=new GridLayout(6,5);
       setLayout(layout);
       addButton();
       initButtons();

       setVisible(true);
       
   }
    /*
     * this use to add button on the levelSelection menu.
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
                Icon imgicon=new ImageIcon("dot.png");
                buttons[j].setIcon(imgicon);
                add(buttons[j]);
            }
            else{
            Icon imgicon = new ImageIcon("dot2.png");
            buttons[j].setIcon(imgicon);
            add(buttons[j]);
            }
        }
        
    }
  
    /*
     * initialize the level name for each button.
     */
    public void fillButtons()//had nested loop before with the outer loop being the string list
    {
          //each level has a specific number associate with it.
          for(int j=0;j<buttons.length;j++)
           {
             buttons[j]=new JButton();
             buttons[j].setText("Level"+" "+Integer.toString(j+1));
           }
        
    }
    /*
     * try to initialize the action listener for each button.
     */
    private void initButtons()
    {
        /*
         * initialize action listener in a try and catch block
         */
        try {
            levelSelect();
        } catch (FileNotFoundException ex) {
            System.out.println("failed");
        } catch (IOException ex) {
            System.out.println("failed");
        }
    }
    /*
     * this is call by initButtons to initialize value for each board and set them visible.
     */
     public void levelSelect() throws FileNotFoundException, IOException{
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
          
          buttons[20].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(21);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[21].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(22);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
          
          buttons[22].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(23);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });  
          
          buttons[23].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(24);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[24].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(25);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
          
          buttons[25].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(26);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[26].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(27);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });
          
          buttons[27].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(28);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
          
          buttons[28].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(29);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            }); 
          
          buttons[29].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
               {  
                   board.setIndex(30);
                   view.initBoard();
                   view.drawBoard();
                   view.updateBoard();
                   setBorder(null);
               }
            });    
          
     }
}        