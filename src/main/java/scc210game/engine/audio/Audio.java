package scc210game.engine.audio;


import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

import java.io.IOException;
import java.nio.file.Paths;

public class Audio {
    //String soundPath = "src/main/resources/sounds/walking.mp3";

    public void playSound(String soundPath) {
        Sound sound = new Sound();
        SoundBuffer sB = new SoundBuffer();
        try {
            sB.loadFromFile(Paths.get(soundPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sound.setBuffer(sB);
        sound.setVolume(20.0f);
        sound.play();
    }
}



