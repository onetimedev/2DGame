package scc210game.engine.audio;


import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

import java.io.IOException;
import java.nio.file.Paths;

public class Audio {
    //String soundPath = "src/main/resources/sounds/walking.mp3";

    public void walkingSound(String soundPath) {
        Sound sound;
        SoundBuffer sB = new SoundBuffer();
        try {
            sB.loadFromFile(Paths.get(soundPath));
            System.out.println("HEEEEEEEEEEEEEEEEEEEEY");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        sound = new Sound();
        sound.setBuffer(sB);
        for(int i = 0; i < 100; i++) {
            sound.play();
            //System.out.println("PLLLLLLAAAAAAAAAAAAAAAY");
        }

    }




}



