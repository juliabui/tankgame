package TankGame.src.game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private Clip bgMusic;
    private Map<String, Clip> soundEffects;

    public AudioManager() {
        soundEffects = new HashMap<>();
    }

    public void loadBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("out/production/csc413-tankgame-juliabui/images/Music.mid"));
            bgMusic = AudioSystem.getClip();
            bgMusic.open(audioStream);
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public void playBackgroundMusic() {
        if (bgMusic != null) {
            bgMusic.start();
        }
    }

    public void stopBackgroundMusic() {
        if (bgMusic != null) {
            bgMusic.stop();
        }
    }


    public void loadSoundEffect(String name, String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            soundEffects.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String name) {
        Clip clip = soundEffects.get(name);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
            System.out.println("Playing sound effect: " + name);
        } else {
            System.out.println("Sound effect not found: " + name);
        }
    }

    public void close() {
        if (bgMusic != null) {
            bgMusic.close();
        }
    }
}
