/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.Cosmic;
import com.imi.cosmic.Entity;
import com.imi.cosmic.GameComponent.GameObject;
import com.imi.input.InputManager.InputClient;
import com.imi.scene.Matrix;
import com.imi.tools.CosmicResource;
import com.imi.tools.editor.EditorToolBar;
import com.imi.tools.editor.Palette;
import com.imi.tools.editor.Palette.PaletteObject;
import com.imi.tools.explorer.constructors.GameComponentConstructor;
import com.imi.tools.explorer.inspectors.GameComponentInspector;
import com.imi.tools.explorer.inspectors.GameComponentInspector.GameComponentInspectorGameObjectPanel;
import com.imi.utils.MathUtils;
import com.imi.vecmath.Vector3f;
import cosmicleap.actors.Ball;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 *
 * @author Lou Hayt
 */
public class DeveloperTools implements InputClient, GameComponentInspectorGameObjectPanel
{
    public DeveloperTools() {
        // Open the editor windows
//        CosmicExplorer.openWindow();
        EditorToolBar.openWindow();
      
        Palette.get().addObject(new BallPaint());
//        Palette.get().addObject(new PiratePaint());
        Palette.get().selectFirstObject();
        
        // Set custom inspector widgets
        GameComponentInspector.gameObjectPanels = this;
//        
//        // Set the game object types
        GameComponentConstructor.gameObjectTypes = new String [] {"cosmicleap.actors.Ball", };
    }
    
    @Override
    public void processKeyEvent(KeyEvent ke) {
        if (!CosmicLeap.isDeveloper())
            return;
        
        if (ke.getID() == KeyEvent.KEY_PRESSED)
        {
            if (ke.getKeyCode() == KeyEvent.VK_V)
            {
//                System.out.println("[V] V is for volume");
            }
        }
    }

    @Override
    public void processMouseEvent(MouseEvent me) {
        if (!CosmicLeap.isDeveloper())
            return;
    }

    @Override
    public JPanel getPanel(GameObject gameObject) {
        JPanel panel = null;
//        if (gameObject instanceof Pirate)
//        {
//            PirateInspector insp = new PirateInspector();
//            insp.initPirate((Pirate)gameObject);
//            panel = insp;
//        }
        return panel;
    }

    @Override
    public void refreshPanel(JPanel gameObjectPanel, GameObject gameObject) {
//        if (gameObject instanceof Pirate)
//            ((PirateInspector)gameObjectPanel).refresh((Pirate)gameObject);
    }
    
    public static class BallPaint extends PaletteObject {
        static Vector3f pos = new Vector3f();
        public BallPaint() {
            super("Ball", new CosmicResource("assets/textures/icons/actors/pirate.jpg"));
        }
        @Override
        public Entity place(Vector3f pos) {
            float radius = MathUtils.randomFloatInRange(0.5f, 3.0f);
            float mass = radius;
            Ball ball = Ball.buildBall(pos, mass, radius);
            CosmicLeap.getGame().addEntity(ball.getEntity());
            return ball.getEntity();
        }
    }

//    private static class PirateInspector extends JPanel {
//        public PirateInspector() {
//            super();
//        }
//        public void initPirate(Pirate pirate) {
//            refresh(pirate);
//        }
//        public void refresh(Pirate pirate) {
//        }
//    }
}
