package scc210game.engine.audio;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

import java.io.IOException;
import java.nio.file.Paths;

public class Audio {
    Sound sound = new Sound();

    public void playSound(String soundPath, boolean loop) {
        SoundBuffer sB = new SoundBuffer();
        try {
            sB.loadFromFile(Paths.get(soundPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        sound.setBuffer(sB);
        sound.setVolume(20.0f);
        sound.setLoop(loop);
        sound.play();
    }
    public void stopSound() {
        sound.stop();
    }
}



