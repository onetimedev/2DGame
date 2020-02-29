package scc210game.engine.audio;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.audio.SoundSource;

import java.io.IOException;
import java.nio.file.Path;

public class Audio {
    Sound sound = new Sound();

    public void playSound(Path soundPath, boolean loop) {
        SoundBuffer sB = new SoundBuffer();

        try {
            sB.loadFromFile(soundPath);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        if(sound.getStatus() != SoundSource.Status.PLAYING) {
            sound.setBuffer(sB);
            sound.setVolume(20.0f);
            sound.play();
        }
    }

    public void stopSound() {
        this.sound.stop();
    }
}