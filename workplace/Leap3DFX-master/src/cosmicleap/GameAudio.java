/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.audio.CosmicAudio;
import com.imi.audio.CosmicAudioManager;
import com.imi.tools.CosmicResource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Loads game audio and provides audio interface for the game
 * @author Lou Hayt
 */
public class GameAudio 
{
    static String audioClass = "com.imi.audio.CosmicAudioStub";
    static CosmicAudioManager audioManager = null;
    static boolean audioOn = true;
        
    // TODO configuration file
    public static enum Sounds {
        MenuButton("audio/menuSelect.ogg", true);
//        TryAgain("audio/tryAgain.ogg", true),
//        Collect("audio/collect.ogg", true),
//        StartGame("audio/ting.ogg", true),
//        Hit("audio/hit.ogg", true);
        
        public CosmicResource resource;
        public CosmicAudio audio;
        public boolean enabled;
        Sounds(String path, boolean enabled) {
            this.resource = new CosmicResource(path);
            this.enabled = enabled;
        }
    }
    
//    public static enum Music {
//        MainTheme("audio/win.ogg", false);
//        
//        public CosmicResource resource;
//        public CosmicAudio audio;
//        public boolean enabled;
//        Music(String path, boolean enabled) {
//            this.resource = new CosmicResource(path);
//            this.enabled = enabled;
//        }
//    }
    
    public static void useWAV() {
        for(GameAudio.Sounds sound : GameAudio.Sounds.values()) 
        {
            String path = sound.resource.getRelativePath();
            sound.resource = new CosmicResource(path.replace(".ogg", ".wav"));
        }
    }
    
    public static void initSounds() {
//        CosmicAudio.clear();
        // Load Music
//        for(GameAudioApple.Music music : GameAudioApple.Music.values()) 
//        {
//            if (music.enabled)
//            {
//                System.out.println("Loading music: " + music);
//                music.audio = new CosmicAudio();
//                music.audio.initialize(music.resource);
//            }
//        }

        // Load sounds
        for(GameAudio.Sounds sound : GameAudio.Sounds.values()) 
        {
            if (sound.enabled)
            {
                System.out.println("Loading sound: " + sound);
                try {
                    sound.audio = (CosmicAudio) Class.forName(audioClass).newInstance();
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(GameAudio.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (sound.audio != null)
                    sound.audio.initialize(sound.resource);
            }
        }
    }

    // Audio events

    public static void playSound(GameAudio.Sounds sound) {
        if (sound.enabled && GameAudio.audioOn && sound.audio != null)
            sound.audio.playSound(1.0f, 1.0f, false);
    }

    // Utils
    
    public static boolean isAudioOn() {
        return audioOn;
    }

    public static void setAudioOn(boolean audioOn) {
        GameAudio.audioOn = audioOn;
    }
        
    public static void stopMusic() {
//        for(Music music : Music.values()) 
//            music.audio.stop();
    }

    public static String getAudioClass() {
        return audioClass;
    }

    public static void setAudioClass(String audioClass) {
        GameAudio.audioClass = audioClass;
    }

    public static CosmicAudioManager getAudioManager() {
        return audioManager;
    }

    public static void setAudioManager(CosmicAudioManager audioManager) {
        GameAudio.audioManager = audioManager;
    }
}
