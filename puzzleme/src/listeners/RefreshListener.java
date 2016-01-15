/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.Board;

/**
 *
 * @author charles
 */
public class RefreshListener implements ActionListener{
    Board board;
    /*
     * constructor
     */
    public RefreshListener(Board board)
    {
        this.board = board;
    }
    /*
     * reset the map andline.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            //reset the map without incrementing or decrementing the index.
            board.resetMap();
            board.reset = true;
            board.repaint();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RefreshListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RefreshListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
