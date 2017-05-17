/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.Cosmic;
import com.imi.collision.PickResult;
import com.imi.cosmic.Entity;
import com.imi.input.InputManager;
import com.imi.scene.Matrix;
import com.imi.tools.editor.EditorControls;
import cosmicleap.actors.Bomb;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * Game input client
 * @author Lou Hayt
 */
public class GameInput implements InputManager.InputClient, PickResult.PickResultListener {
    
    public GameInput() {
    }
    
    @Override
    public void processMouseEvent(MouseEvent me) 
    {
        boolean selectedTool = EditorControls.isSelectedTool(); // true if the editor tool bar is on and the View mode is not selected
        if (!GameUI.isUITouch() && SwingUtilities.isLeftMouseButton(me) && me.getID() == MouseEvent.MOUSE_PRESSED && !selectedTool)
        {
            Cosmic.pickEnvironment(this);
        }
        
//        int scroll = MouseMonitor.getMouseScroll(me);
//        ((CameraBehaviorThirdPerson)CameraBehaviorThirdPerson.getMainCamera()).zoomChange(scroll);
    }
    
    Matrix mat = new Matrix();
    
    @Override
    public void onPick(PickResult pr) {
        if (pr != null && pr.getCollisionComponent() != null && pr.getCollisionComponent().getEntity() != null)
        {
            Entity entity = pr.getCollisionComponent().getEntity();
//            System.out.println("Picked " + entity.getName() + " at " + pr.getPosition());
//            CosmicDebug.createDebugSphere(pr.getPosition(), null, 1.0f);
//            GameAudio.playSound(GameAudio.Sounds.Hit);
            
            Cosmic.getMainCamera().getCameraMatrix(mat);
            Bomb bomb = Bomb.buildBomb(mat, 0.5f, 1.0f, -1.0f);
            CosmicLeap.game.addEntity(bomb.getEntity());
        }
    }
    
    @Override
    public void processKeyEvent(KeyEvent ke) {
        
        if (ke.getID() == KeyEvent.KEY_PRESSED)
        {
            if (ke.getKeyCode() == KeyEvent.VK_1)
            {
                System.out.println("Switching to free camera");
                CosmicLeap.initializeFreeCamera();
                
            }
            if (ke.getKeyCode() == KeyEvent.VK_2)
            {
//                System.out.println("Switching to tumble crew camera");
//                CosmicLeap.initializeCrewCamera();
            }
        }
    }
}
