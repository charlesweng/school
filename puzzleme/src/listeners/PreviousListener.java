/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import objects.Board;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charles
 */
public class PreviousListener implements ActionListener{
    Board board;
    /*
     * constructor
     */
    public PreviousListener(Board board)
    {
        this.board = board;
    }
    /*
     * reset the board to the previous index when the previous button is pressed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //go to previous Index
        board.previousIndex();
        //reset the board in a try and catch block
        try {
            board.resetMap();
            board.reset = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NextListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        //repaint
        board.repaint();
    }
}
