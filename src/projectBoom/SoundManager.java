package projectBoom;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {

    public static void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start(); // 播完一次
            } else {
                System.out.println("找不到音效檔案：" + filePath);
            }
        } catch (Exception e) {
            System.out.println("音效播放失敗：" + filePath);
        }
    }

    public static void playBackgroundMusic(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
                
                clip.start(); // 背景音樂loop
            } else {
                System.out.println("找不到背景音樂檔案：" + filePath);
            }
        } catch (Exception e) {
            System.out.println("背景音樂播放失敗：" + filePath);
        }
    }
}