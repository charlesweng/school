package view;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author julio
 */
public class Music implements Runnable 
{
     //declare attributes
     AudioInputStream input;
     Clip clip;
   
    /*
     * run the music if it is not playing
     * loop trough it again if it stop
     */
    @Override
    public void run() 
    {
        boolean played = false;
            if ( !played )
            {
                try {
                    String name="CRYSIS.wav";
                    URL url = this.getClass().getResource(name);
                    input = AudioSystem.getAudioInputStream(url);
                    input.mark(8416512);
                    clip = AudioSystem.getClip();
                    clip.open(input);
                    clip.loop(1);
                    played = true;

                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {}
            }
        //loop again the music if it stop
        while (true)
        {

           if (clip.getFramePosition() >= input.getFrameLength())
               clip.loop(1);

        }
     }
}

