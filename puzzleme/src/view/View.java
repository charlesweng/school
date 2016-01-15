package view;


import LevelSelection2.LevelSelection2;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import levelmenu.LevelMenu;
import levelselection.LevelSelection;
import listeners.NextListener;
import listeners.PreviousListener;
import listeners.RefreshListener;
import menu.Menu;
import objects.Board;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Charles
 */
public class View implements Runnable{
    // needed JComponents
    JFrame frame;
    JPanel buttonPanel;
    JButton refresh, previous, next, startGame, instruction, exit;
    JLabel jLabel;
    ImageIcon imageIcon;
    // our main interfaces
    Board board;
    Menu menu;
    InstructionJPanel instructions;
    LevelMenu levelmenu;
    LevelSelection2 level2;
    LevelSelection level;
    
    // Music
    Music music;
    Music music1;
    // Dimension
    Dimension dimension;
    int windowSize;
    
    public static Timer timer;
    /*
     * constructor
     */
    public View()
    {
        //initialize image and jframe
        frame = new JFrame("Connecting The Dots");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.setResizable(false);
        //initialize listeners
        //level=new LevelSelectionMouseListener(this);
        //initialize music
        music = new Music();
        music1 = new Music();
        //initialize dimensions
        windowSize = 600;
        dimension = new Dimension(windowSize , windowSize);
    }
    
    /*
     * initialize the font and style of previous, next and refresh button.
     */
    public void initBoard()
    {
        frame.getContentPane().removeAll();

        board.setPreferredSize(dimension);
        //initializing buttons refresh, next, and previous
        buttonPanel= new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3));
        refresh = new JButton("Refresh");
        refresh.setFont(new Font("Pericles",Font.BOLD,12));
        refresh.setForeground(Color.WHITE);
        refresh.setOpaque(false);
        refresh.setContentAreaFilled(false);
        refresh.setBorderPainted(false);
        previous = new JButton("Previous Level");
        previous.setFont(new Font("Pericles",Font.BOLD,12));
        previous.setForeground(Color.WHITE);
        previous.setOpaque(false);
        previous.setContentAreaFilled(false);
        previous.setBorderPainted(false);
        next = new JButton("Next Level");
        next.setFont(new Font("Pericles",Font.BOLD,12));
        next.setForeground(Color.WHITE);
        next.setOpaque(false);
        next.setContentAreaFilled(false);
        next.setBorderPainted(false);
        buttonPanel.add(previous);
        buttonPanel.add(refresh);
        buttonPanel.add(next);
        buttonPanel.setBackground(Color.black);
    }
    /*
     * add action listener for buttons
     */
    public void updateBoard()
    {
        //update the image by drawing/erasing lines
        refresh.addActionListener(new RefreshListener(board));
        previous.addActionListener(new PreviousListener(board));
        next.addActionListener(new NextListener(board));
 
    }
    public void drawBoard()
    {
        //set the container one on south and one on center
        Container container = frame.getContentPane();
        container.setLayout( new BorderLayout() );
        container.add( buttonPanel,BorderLayout.SOUTH);
        container.add(board, BorderLayout.CENTER);
        //set the frame size and make it visible
        frame.setSize(windowSize, windowSize+100);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    /*
     * initialize the layout for the main menu when user first enter the game.
     */
    public void initMenu()
    {
        frame.setPreferredSize(dimension);
        menu = new Menu();
        menu.setOpaque(true);
        menu.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //set title and gridlayout
        JLabel gameTitle = new JLabel("Connect The Dots");
        gameTitle.setFont(new Font("Pericles",Font.BOLD,32));
        gameTitle.setForeground(Color.WHITE);
        gbc.gridy = 0;
        menu.add(gameTitle,gbc);
        
        gbc.insets = new Insets(350,1,1,1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridy = 1;
        //add a start game button
        startGame = new JButton("Start Game");
        startGame.setFont(new Font("Pericles",Font.BOLD,12));
        startGame.setForeground(Color.WHITE);
        startGame.setOpaque(false);
        startGame.setContentAreaFilled(false);
        startGame.setBorderPainted(false);
        menu.add(startGame,gbc);
        
        gbc.insets = new Insets(10,1,1,1);
        gbc.gridy = 2;
        //add a Instruction button
        instruction = new JButton("Instructions");
        instruction.setFont(new Font("Pericles",Font.BOLD,12));
        instruction.setForeground(Color.WHITE);
        instruction.setOpaque(false);
        instruction.setContentAreaFilled(false);
        instruction.setBorderPainted(false);
        menu.add(instruction,gbc);
        
        gbc.gridy = 3;
        //add an Exit button
        exit = new JButton("Exit");
        exit.setFont(new Font("Pericles",Font.BOLD,12));
        exit.setForeground(Color.WHITE);
        exit.setOpaque(false);
        exit.setContentAreaFilled(false);
        exit.setBorderPainted(false);
        menu.add(exit,gbc);
        
    }
    //implement the button listener and mouse listener
    public void updateMenu()
    {
        startGame.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {

                  //initial the level menu
                  initLevelMenu();
                  viewLevelMenu();
                  removeMenuButtons();
           }       
        });
        startGame.addMouseListener(new MouseListener()
        {
            Font originalFont = null;
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
             
            @Override
            public void mouseEntered(MouseEvent e) {
                originalFont = startGame.getFont();
                Map attributes = originalFont.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                startGame.setFont(originalFont.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startGame.setFont(new Font("Pericles",Font.BOLD,12));
            }

            
        
        });
        instruction.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                  //initialize the Instruction menu
                  initInstructions();
                  viewInstructions();
                  
           }       
        });
        /*
         * add mouse Listener for the instruction button.
         */
        instruction.addMouseListener(new MouseListener()
        {
            Font originalFont = null;
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                originalFont = instruction.getFont();
                Map attributes = originalFont.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                instruction.setFont(originalFont.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                instruction.setFont(new Font("Pericles",Font.BOLD,12));
            }

            
        
        });
        //add a mouse listener for the exit button
        exit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                frame.dispose();
                System.exit(0);
            }
        });
        exit.addMouseListener(new MouseListener()
        {
            Font originalFont = null;
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                originalFont = exit.getFont();
                Map attributes = originalFont.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                exit.setFont(originalFont.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exit.setFont(new Font("Pericles",Font.BOLD,12));
            }

            
        
        });
    }
    /*
     * set the star game , instructions, and exit buttons to NULL
     */
    public void removeMenuButtons()
    {
        startGame = null;
        instructions = null;
        exit =  null;
    }
    /*
     * make the menu visble
     */
    public void viewMenu()
    {
        frame.getContentPane().removeAll();
        frame.setContentPane(menu);
        frame.setSize(windowSize,windowSize);
        frame.setVisible(true);
        frame.setResizable(false);
    }
    /*
     * make a new InstructionJPanel and set the dimension of it.
     */
    public void initInstructions()
    {
        instructions = new InstructionJPanel(this);
        frame.setPreferredSize(dimension);
    }
    /*
     * set the instruction visible and not resizable 
     */
    public void viewInstructions()
    {
        frame.getContentPane().removeAll();
        frame.setContentPane(instructions);
        frame.pack();
        frame.setSize(windowSize,windowSize);
        frame.setResizable(false);
    }
    /*
     * make a new level Menu and set the size of it.
     */
    public void initLevelMenu()
    {
        levelmenu = new LevelMenu(this);
        frame.setPreferredSize(dimension);
    }
     /*
     * set the levelmenu visible and not resizable 
     */
    public void viewLevelMenu()
    {
        frame.getContentPane().removeAll();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane(levelmenu);
        frame.pack();
        frame.setSize(windowSize,windowSize);
        frame.setResizable(false);
    }
    /*
     * make a new board and set the size of it.
     * also, remove all the content of the current panel.
     */
    public void initLevelSelection(){
        board = new Board(this);
        frame.getContentPane().removeAll();
        level=new LevelSelection(this, board);
    }   
    /*
     * set the JPanel content with only the level JPanel
     * also, remove all the content of the current panel.
     */
    public void viewLevelSelection()
    {
        frame.getContentPane().removeAll();
        frame.setContentPane(level);
        frame.setResizable(false);
    }
    /*
     * reset Board and make  new levelselections2 with the new board.
     * also, remove all the content of the current panel.
     */
    public void initLevelSelection2(){
        board = new Board(this);
        frame.getContentPane().removeAll();
        level2=new LevelSelection2(this,board);
    }
    /*
     * put the levelselection2 in the JPanel
     * also, remove all the content of the current panel.
     */
    public void viewLevelSelection2(){
        frame.getContentPane().removeAll();
        frame.setContentPane(level2);
        frame.setResizable(false);
    }
    /*
     * initialize the menu ,update the menu and view it.
     */
    @Override
    public void run()
    {
        initMenu();
        updateMenu();
        viewMenu();
    }
    /*
     * return the board object
     */
    public Board getBoard()
    {
        return board;
    }

}
