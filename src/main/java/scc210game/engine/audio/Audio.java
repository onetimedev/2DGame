package scc210game.engine.audio;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;
import scc210game.game.utils.MapHelper;

import java.io.IOException;
import java.nio.file.Paths;

public class Audio {
    Sound sound = new Sound();
    private int biomeTile;

    public void playSound(String soundPath, boolean loop) {
        SoundBuffer sB = new SoundBuffer();

        try {
            sB.loadFromFile(Paths.get(soundPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(sound.getStatus() != SoundSource.Status.PLAYING) {
            sound.setBuffer(sB);
            sound.setVolume(20.0f);
            sound.play();
        }
    }
    public void changeBiome(int tT) {
        if(biomeTile != tT) {
            biomeTile = tT;
            stopSound();
        }
    }
    public void stopSound() {
        sound.stop();
    }
}