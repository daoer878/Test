/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap.actors;

import com.imi.Cosmic;
import com.imi.cosmic.Entity;
import com.imi.cosmic.ProcessorComponent;
import com.imi.cosmic.ProcessorComponentUpdate;
import com.imi.utils.CosmicDebug;
import com.imi.utils.DeltaUpdatable;
import com.imi.utils.MathUtils;
import com.imi.vecmath.Vector3f;
import cosmicleap.CosmicLeap;

/**
 *
 * @author Lou Hayt
 */
public class LeapMarkers implements DeltaUpdatable {
    
    Entity entity = new Entity("LeapMarkers");
    
    Vector3f [] markerWorldPos = new Vector3f[10];
    
    Vector3f camPos = new Vector3f();
    Vector3f camFwd = new Vector3f();
    Vector3f marker = new Vector3f();

    public LeapMarkers() {
        for(int i = 0; i < markerWorldPos.length; i++)
        {
            markerWorldPos[i] = new Vector3f();
            CosmicDebug.createDebugTrackingSphere(markerWorldPos[i], MathUtils.generateRandomPositiveVector(), 0.1f);
        }
        
        ProcessorComponent pc = new ProcessorComponentUpdate(this, true);
        entity.addComponent(ProcessorComponent.class, pc);
    }
    
    @Override
    public void update(float deltaTime) {
        
        // Update position in front of camera
        Cosmic.getMainCamera().getCameraPosition(camPos);
        Cosmic.getMainCamera().getCameraForward(camFwd);
        camFwd.scale(-2.0f);
        camFwd.y -= 1.0f;
        camPos.add(camFwd);               
        
        // Move position with sensor offset
        Vector3f [] markerPos = CosmicLeap.getLeap().getMarkerPos();
        for(int i = 0; i < 10; i++)
        {
//            System.out.println("pos " + markerPos[i]);
            marker.set(markerPos[i]);
            Cosmic.getMainCamera().transformPoint(marker);
            marker.scale(0.005f);
//            System.out.println("Offset:/ " + marker);
            markerWorldPos[i].set(camPos);
            markerWorldPos[i].add(marker);
        }
    }

    public Entity getEntity() {
        return entity;
    }
}
