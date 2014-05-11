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
import com.imi.cosmic.GameComponent.GameObject;
import com.imi.cosmic.PhysicsComponent;
import com.imi.cosmic.PhysicsComponentManager;
import com.imi.physics.PhysicsMaterial;
import com.imi.physics.PointMassBody;
import com.imi.scene.Matrix;
import com.imi.scene.SceneInstanceSphere;
import com.imi.scene.SphereBounds;
import com.imi.tools.KeyValueCapsule;
import com.imi.vecmath.Vector3f;
import cosmicleap.ParticleFX;

/**
 *
 * @author Lou Hayt
 */
public class Bomb implements GameObject {
    static int bulletCount = 0;
    Entity entity = new Entity("Bullet"+bulletCount++);
    KeyValueCapsule params;
    
    float timer = 0.0f;
    float timeLength = 3.0f;
   
    public static Bomb buildBomb(Matrix transform, float radius, float mass, float velocity) {
        Bomb bullet = new Bomb();
        GameComponent obj = new GameComponent(bullet);
        // Collision
        obj.initializeCollisionComponent(new SphereBounds(transform.getTranslation(Cosmic.scratch1), radius), new CollisionComponentSphere.PhysicsCollisionResponse());
        // Render
        Vector3f sphereColor = new Vector3f(1.0f, 1.0f, 0.6f);
        SceneInstanceSphere sphereInstance = new SceneInstanceSphere("Bullet Sphere");
        sphereInstance.getScale().set(radius, radius, radius);
        sphereInstance.setColor(sphereColor);
        sphereInstance.assemble();
        obj.initializeRenderComponent(sphereInstance);
        // Physics
        PointMassBody pbody = new PointMassBody(obj.entity, new PhysicsMaterial(0.5f, 0.1f), Cosmic.scratch1, radius);
        pbody.getParticle().setMass(mass);
        Vector3f velocityVector = transform.getLocalZNormalized();
        velocityVector.scale(velocity);
        pbody.getParticle().setVelocity(velocityVector);
        pbody.setFloatsOnWater(false);
        pbody.setConstantForce(PhysicsComponentManager.gravity);
        PhysicsComponent physicsComp = new PhysicsComponent(pbody);
        obj.getEntity().addComponent(PhysicsComponent.class, physicsComp);
        // Processor
        obj.initializeProcessorComponent();
        // Sprite
//        obj.getEntity().addComponent(SpriteParticleComponent.class, new SpriteParticleComponent(getSpriteParticleGroup()));
        return bullet;
    }
    
    
    public Bomb() {
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
        this.timer += deltaTime;
        if (timer > timeLength)
            timedOut();
    }
    
    private void timedOut() {
        // boom!
        float radius = 5.0f;
        float force = 1.0f;
        Vector3f pos = entity.collisionComponent.getBoundingSphere().getCenter();
        PhysicsComponentManager.inst.fireExplosion(pos, radius, force);
        PhysicsComponentManager.markTerrain(pos, radius, PhysicsComponentManager.terrainDarkColor);
        ParticleFX.explosion(pos);
        
        entity.removeFromParent();
    }
    
    @Override
    public void colliding(CollisionComponent other) {
//        if (other.getEntity().gameComponent == null)
//            return;
//        // Kill Leps
//        if (other.getEntity().gameComponent.getGameObject() instanceof Lep)
//        {
//            ((Lep)other.getEntity().gameComponent.getGameObject()).shrinkAndDie();
//        }
    }
    
    @Override
    public boolean cancelCollisionResponse(Entity en) {
//        if (en.gameComponent.getGameObject() instanceof Character)
//            return true;
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
