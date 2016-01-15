import view.Music;
import view.View;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Charles
 */
public class Main extends Thread{

    public static void main(String args[])
    {
        //set A boolean value to see if the game started or not.
        boolean gameStarted = false;
        //Declare attributes for view and Musics
        long startTime = System.nanoTime();
        View view = new View();
        Music sound = new Music();
        Thread game = new Thread(view);
        Thread music = new Thread(sound);
        music.start();
        //if the game is not started, start the game.
        while (true)
        {
            if (!gameStarted)
            {
                game.start();
                gameStarted = true;
            }
               
        }

    }
}
