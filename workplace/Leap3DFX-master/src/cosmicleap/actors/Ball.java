/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap.actors;

import com.imi.Cosmic;
import com.imi.cosmic.CollisionComponent;
import com.imi.cosmic.CollisionComponentSphere;
import com.imi.cosmic.Entity;
import com.imi.cosmic.GameComponent;
import com.imi.cosmic.PhysicsComponent;
import com.imi.cosmic.PhysicsComponentManager;
import com.imi.physics.PhysicsMaterial;
import com.imi.physics.PointMassBody;
import com.imi.scene.Matrix;
import com.imi.scene.SceneInstanceSphere;
import com.imi.scene.SphereBounds;
import com.imi.tools.KeyValueCapsule;
import com.imi.vecmath.Vector3f;

/**
 *
 * @author Lou Hayt
 */
public class Ball implements GameComponent.GameObject {

    /** Serialization version number **/
    private static final long serialVersionUID = 1l;
    
    KeyValueCapsule params;
    
    static int ballCount = 0;
    Entity entity = new Entity("Ball"+ballCount++);
   
    public static Ball buildBall(Vector3f pos, float radius, float mass) {
        Ball ball = new Ball();
        GameComponent obj = new GameComponent(ball);
        // Collision
        obj.initializeCollisionComponent(new SphereBounds(pos, radius), new CollisionComponentSphere.PhysicsCollisionResponse());
        // Render
        Vector3f sphereColor = new Vector3f(1.0f, 1.0f, 0.6f);
        SceneInstanceSphere sphereInstance = new SceneInstanceSphere("Ball Sphere");
        sphereInstance.getScale().set(radius, radius, radius);
        sphereInstance.setColor(sphereColor);
        sphereInstance.assemble();
        obj.initializeRenderComponent(sphereInstance);
        // Physics
        PointMassBody pbody = new PointMassBody(obj.entity, new PhysicsMaterial(0.5f, 0.1f), Cosmic.scratch1, radius);
        pbody.getParticle().setMass(mass);
        pbody.setFloatsOnWater(false);
        pbody.setConstantForce(PhysicsComponentManager.gravity);
        PhysicsComponent physicsComp = new PhysicsComponent(pbody);
        obj.getEntity().addComponent(PhysicsComponent.class, physicsComp);
        // Processor
        obj.initializeProcessorComponent();
        // Sprite
//        obj.getEntity().addComponent(SpriteParticleComponent.class, new SpriteParticleComponent(getSpriteParticleGroup()));
        return ball;
    }
    
    
    public Ball() {
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

    @Override
    public boolean onExplosionPush(Vector3f explosionCenter, float explosionRadius, float force) {
        return false;
    }


}
