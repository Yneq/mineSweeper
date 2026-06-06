package projectBoom;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {

    // 🎵 1. 播放「一般短音效」（獲勝、失敗、爆炸）
    public static void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start(); // 播完一次就自己結束
            } else {
                System.out.println("找不到音效檔案：" + filePath);
            }
        } catch (Exception e) {
            System.out.println("音效播放失敗：" + filePath);
        }
    }

    // 🔁 2. 播放「背景音樂」（會無限循環播放）
    public static void playBackgroundMusic(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (soundFile.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                // ⭐ 核心魔法：設定無限循環播放！
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
                
                clip.start(); // 開始播放背景音樂
            } else {
                System.out.println("找不到背景音樂檔案：" + filePath);
            }
        } catch (Exception e) {
            System.out.println("背景音樂播放失敗：" + filePath);
        }
    }
}