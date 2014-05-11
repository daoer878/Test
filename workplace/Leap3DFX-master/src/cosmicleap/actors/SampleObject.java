/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap.actors;

import com.imi.cosmic.CollisionComponent;
import com.imi.cosmic.Entity;
import com.imi.cosmic.GameComponent;
import com.imi.tools.KeyValueCapsule;
import com.imi.vecmath.Vector3f;

/**
 *
 * @author Lou Hayt
 */
public class SampleObject  implements GameComponent.GameObject {
    static int objCount = 0;
    Entity entity = new Entity("SampleObject"+objCount++);
    KeyValueCapsule params;
   
    public SampleObject() {
        init(new KeyValueCapsule(getClass().getSimpleName()));
    }
     
    private void init(KeyValueCapsule params) {
        this.params = params;
        refreshParams();
    }
    
    @Override
    public void refreshParams() {
    }

    @Override
    public void update(float deltaTime) {
    }
    
    @Override
    public void colliding(CollisionComponent other) {
    }
    
    @Override
    public boolean cancelCollisionResponse(Entity en) {
        return false;
    }
    
    @Override
    public boolean onExplosionPush(Vector3f explosionCenter, float explosionRadius, float force) {
        return true;
    }
    
    @Override
    public void setEntity(Entity en) {
        if (entity != null)
            throw new RuntimeException("Setting an entity on a game object with an existing entity");
        entity = en;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public KeyValueCapsule getParams() {
        return params;
    }

}
