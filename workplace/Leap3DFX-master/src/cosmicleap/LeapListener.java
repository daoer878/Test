/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.CosmicEngine;
import com.imi.vecmath.Vector3f;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Vector;

/**
 *
 * @author Lou Hayt
 */
public class LeapListener extends Listener {
    
    Controller controller = null;
    
//    final Pointable [] markers = new Pointable[10];
    final Vector3f [] markerPos = new Vector3f[10];
    
    public LeapListener() {
        for(int i = 0; i < 10; i++) {
            markerPos[i] = new Vector3f();
        }
    }
    
    @Override
    public void onFrame(Controller controller) {
        // Get the most recent frame 
        Frame frame = controller.frame();
//        printFrameInfo(frame);

        int index = 0;
        for(Pointable marker : frame.pointables())
        {
            if (marker.isValid())
            {
                Vector pos = marker.tipPosition();
                markerPos[index].set(pos.getX(), pos.getY(), pos.getZ());
            }
            else
                markerPos[index].set(0.0f, 0.0f, 0.0f);
            
            if (index < 10)
                index++;
            else
                break;
        }
            
    }

    public  void startLeapMotion() {
        if (controller != null)
        {
            // Just to be sure we don't add it twice 
            controller.removeListener(this);
            controller.addListener(this);
        }
        else
        {
            controller = new Controller(this); // Adds the listener
        }
    }
    
    public class LeapExitListener implements CosmicEngine.ExitListener {
        @Override
        public void onApplicationExit() {
            stopLeapMotion();
        }
    }
    
    public LeapExitListener createExitListener() {
        return new LeapExitListener();
    }
    
    public void stopLeapMotion() {
        if (controller != null) {
            controller.removeListener(this);
        }
    }

    public Vector3f[] getMarkerPos() {
        return markerPos;
    }
    
    private void printFrameInfo(Frame frame) {
        System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count());
    }
    
    @Override
    public void onInit(Controller controller) {
        System.out.println("Leap Motion - Initialized");
    }

    @Override
    public void onConnect(Controller controller) {
        System.out.println("Leap Motion - Connected");
    }

    @Override
    public void onDisconnect(Controller controller) {
        System.out.println("Leap Motion - Disconnected");
    }

    @Override
    public void onExit(Controller controller) {
        System.out.println("Leap Motion - Exited");
    }
}
