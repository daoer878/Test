/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.Cosmic;
import com.imi.cosmic.UI.CosmicUIContainer;
import com.imi.cosmic.UI.HUD;

/**
 * Loads the game ui asset, holds the main CosmicUIContainer for the game
 * @author Lou Hayt
 */
public class GameUI  {

    static boolean UITouch = false;
    static float [] zLayers;
    
    CosmicUIContainer gameHUD;
        
    public GameUI(String versionLabel)
    {
        // Get the game HUD ready
        zLayers = HUD.generateHUDZLayers(10, 0.0001f);
        Cosmic.getHUDComponent().getComponent().name = "CosmicLeapHUD";
        gameHUD = (CosmicUIContainer)Cosmic.getHUDComponent().getComponent();
//        gameHUD.set(CosmicUIContainer.load("ui/PvLHUD.cui"));
        gameHUD.initialize();    
        gameHUD.addToHUD();
       
//        CosmicUILabel vLabel = (CosmicUILabel)gameHUD.find("version");
//        vLabel.getTextPG().setText(versionLabel);
//        vLabel.getComponent().panIn();
    }

    public CosmicUIContainer getGameHUD() {
        return this.gameHUD;
    }
    
    public static boolean isUITouch() {
        return UITouch;
    }
}
