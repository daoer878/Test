/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import assets.JarLoader;
import com.imi.Cosmic;
import com.imi.CosmicEngine;
import com.imi.audio.CosmicAppletAudioManager;
import com.imi.cosmic.CameraBehavior;
import com.imi.cosmic.CameraBehaviorFirstPerson;
import com.imi.cosmic.CollisionComponentManager;
import com.imi.cosmic.ComponentManager;
import com.imi.cosmic.Engine;
import com.imi.cosmic.Entity;
import com.imi.cosmic.EntityManager;
import com.imi.cosmic.PhysicsComponentManager;
import com.imi.cosmic.RenderComponentManager;
import com.imi.cosmic.UI.SplashScreen;
import com.imi.cosmic.animation.AnimationComponentManager;
import com.imi.input.InputManager;
import com.imi.jist3d.render.RendererManagerBase;
import com.imi.tools.CosmicResource;
import com.imi.utils.CosmicDebug;
import com.imi.vecmath.Vector3f;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JApplet;

/**
 *
 * @author Lou
 */
public class CosmicLeap extends CosmicEngine.AppBase {

    public static Entity game, level;
    public static GameUI gameUI;
    public static LeapListener leap;
        
    public static boolean developer = false; // command line argument
    
    static String appName = null;
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 600;
    static final Vector3f cameraInitPos = new Vector3f(0.0f, 100.0f, 10.0f);
    
    SplashScreen splashScreen = new SplashScreen();
    
    public interface CosmicLeapApp {
        public void initialize();
        public Entity getLevelEntity();
    }
    
    public CosmicLeap() {
        //Cosmic.writeTextureSideCars = false; // JNLP deployment
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Cosmic.args = args;
        CosmicLeap.parseArgs(args);
        CosmicEngine.run(new CosmicLeap(), SCREEN_WIDTH, SCREEN_HEIGHT, false);
    }
    
    static void parseArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            // developer flag for tools -d true
            if (args[i].equals("-d")) {
                if (i + 1 < args.length) {
                    developer = Boolean.parseBoolean(args[i + 1]);
                    System.out.println("Dev tools: " + developer);
                }
            }
            // app run flag -t com.package.AppClassName
            if (args[i].equals("-t")) {
                if (i + 1 < args.length) {
                    appName = args[i + 1];
                    System.out.println("Run app: " + appName);
                }
            }
        }
    }

    @Override
    public void loadAppletParameters(JApplet applet) {
        System.out.println("Load applet parameters for: " + applet);
//        JavaScript.init(applet);
        // Just to make sure
        developer = false;
        Cosmic.masterDebug = false;
        Cosmic.printFPS = false;
    }
    
    @Override
    public void initialize() {
        // Load
        System.out.println("Loading CosmicLeap Application...");

        game = new Entity("GameManager");
        gameUI = new GameUI("v0.01");
        leap = new LeapListener();
        leap.startLeapMotion();
        CosmicEngine.addExitListener(leap.createExitListener());
        
        if (appName == null)
            System.out.println("ERROR - No app name is set!");
        else
        {
            try {
                // initialize the test
                Class testClass = Class.forName(appName);
                CosmicLeapApp application = (CosmicLeapApp) testClass.newInstance();
                application.initialize();
                startGame(application.getLevelEntity());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
                Logger.getLogger(CosmicLeap.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Add the game entity
        EntityManager.inst.addEntity(game);

        // Game input
        InputManager.inst.addInputClient(new GameInput());
        // Enable developer mode
        if (developer) {
            InputManager.inst.addInputClient(new DeveloperTools());
        }

        // Remove splash screen
        splashScreen.remove();
//        JavaScript.doneLoading();       
        
    }

    public static CameraBehavior initializeFreeCamera() {
        CameraBehaviorFirstPerson camera = Cosmic.createFirstPersonCameraBehavior();
        camera.setLeftMouseClickToViewEnabled(false);
//        camera.setRightMouseClickToViewEnabled(false);
//        camera.setDPadEnabled(false);
        camera.setEnabled(true); // input
        CameraBehavior existingCamera = Cosmic.getMainCameraBehavior();
        if (existingCamera == null)
            camera.setCameraPosition(cameraInitPos);
        else
        {
            camera.setCameraPosition(existingCamera.getCamera().getCameraPosition(Cosmic.scratch1));
            existingCamera.setEnabled(false);
        }
        CameraBehavior.setMainCamera(camera);
        if (RenderComponentManager.inst.getHUD() != null)
            RenderComponentManager.inst.getHUD().setCameraBehavior(camera);
        return camera;
    }
    
    @Override
    public void initializeManagers() {

        // Make it so resources loaded from the jar will be taken from the assets package
        CosmicResource.loaderClass = JarLoader.class;

        // Set title and window icon
        Cosmic.setTitle("CosmicLeap", "icons/frameIcon.png"); // TODO configuration file?
        RendererManagerBase.setClearColor(0.0f, 0.0f, 0.0f); // Background color

        // Initialize the HUD
        Cosmic.initHUD(initializeFreeCamera());

        // Splash screen
        splashScreen.show();

        // Set audio
        GameAudio.audioClass = "com.imi.audio.CosmicAppletAudio"; // Applets currently don't use OpenAL or ogg files, only wav files
        GameAudio.audioManager = new CosmicAppletAudioManager();

//        System.out.println("Initialize Processor Manager");
//        ProcessorComponentManager.inst.initialize(); // (start the off-render-thread thread)
        System.out.println("Initialize Collision Manager");
        CollisionComponentManager.inst.initialize();
        System.out.println("Initialize Physics Manager");
        PhysicsComponentManager.inst.initialize();
        System.out.println("Initialize Animation Manager");
        ComponentManager acm = new AnimationComponentManager("DefaultAnimationManager");
        acm.initialize();
        Engine.putManager(acm);
        System.out.println("Initialize CosmicDebug");
        CosmicDebug.initEntity();
    }

    public static void startGame(Entity myLevel) {
        game.removeAllSubEntities();
        level = myLevel;
        game.addEntity(myLevel);
    }
    
    public static Entity getGame() {
        return game;
    }

    public static GameUI getGameUI() {
        return gameUI;
    }

    public static Entity getLevel() {
        return level;
    }

    public static LeapListener getLeap() {
        return leap;
    }

    public static boolean isDeveloper() {
        return developer;
    }
}
