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
public class NextListener implements ActionListener{
    Board board;
    /*
     * constructor
     */
    public NextListener(Board board)
    {
        this.board = board;
    }
    /*
     * when the next button is pressed, the board is being reset to the next index
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //set to next index.
        board.nextIndex();
        //reset the map in a try and catch block.
        try {
            board.resetMap();
            board.reset = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NextListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        // repaint;
        board.repaint();
    }
}
