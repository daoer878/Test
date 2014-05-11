package cosmicleap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.imi.cosmic.Entity;
import cosmicleap.CosmicLeap.CosmicLeapApp;
import cosmicleap.actors.LeapMarkers;

/**
 *
 * @author Lou Hayt
 */
public class TestApp1 implements CosmicLeapApp {

    Entity level = new Entity("Level");
    
    @Override
    public void initialize() {
        
        // Lets take a look at the splash screen
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(TestApp1.class.getName()).log(Level.SEVERE, null, ex);
//        }

        Island.loadIsland();

        GameAudio.useWAV();
        GameAudio.initSounds();

        
        LeapMarkers markers = new LeapMarkers();
        level.addEntity(markers.getEntity());
    }
    
    @Override
    public Entity getLevelEntity() {
        return level;
    }
}
