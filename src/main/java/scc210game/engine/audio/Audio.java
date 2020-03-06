package scc210game.engine.audio;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;

import java.io.IOException;
import java.io.InputStream;

public class Audio {
    Sound sound = new Sound();
    private int biomeTile;

    public void playSound(InputStream soundFile, boolean loop) {
        SoundBuffer sB = new SoundBuffer();

        try {
            sB.loadFromStream(soundFile);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if (this.sound.getStatus() != SoundSource.Status.PLAYING) {
            this.sound.setBuffer(sB);
            this.sound.setVolume(20.0f);
            this.sound.play();
        }
    }

    public void changeBiome(int tT) {
        if (this.biomeTile != tT) {
            this.biomeTile = tT;
            this.stopSound();
        }
    }

    public void stopSound() {
        this.sound.stop();
    }
}