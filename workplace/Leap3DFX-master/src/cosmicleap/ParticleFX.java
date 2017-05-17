/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cosmicleap;

import com.imi.cosmic.TextureManager;
import com.imi.jist3d.render.Texture;
import com.imi.jist3d.render.impl.JGL;
import com.imi.scene.particles.Particle;
import com.imi.scene.particles.ParticleGroup;
import com.imi.scene.particles.ParticleGroupFactory;
import com.imi.tools.TextureLoader;
import com.imi.utils.CosmicDebug;
import com.imi.vecmath.Matrix4f;
import com.imi.vecmath.Vector3f;

/**
 *
 * @author Lou Hayt
 */
public class ParticleFX {
    
    static Matrix4f scratch = new Matrix4f();    
    public static void explosion(Vector3f position) {
//        System.out.println("Explosion particleFX " + System.nanoTime());
        position.y += 2.5f;
        scratch.setTranslation(position);
        Particle p = getExplosionParticleGroup().emitParticle(scratch);
    }
    
    private static ParticleGroup explosionParticleGroup = null;
    public static ParticleGroup getExplosionParticleGroup() {
        if (explosionParticleGroup == null)
        {
            // Create a particle group
            String texturePath = TextureLoader.writeTexture(new Texture(JGL.TEXTURE_2D, "textures/toonExplosion.png", false));
            Texture texture = TextureManager.getTexture(texturePath);
            explosionParticleGroup = ParticleGroupFactory.makeParticleGroup(true, ParticleGroupFactory.QUAD_ANIMATED);
            explosionParticleGroup.setTexture(texture);
            explosionParticleGroup.setTexturePath(texturePath);
            explosionParticleGroup.lifetime = 16;
            explosionParticleGroup.numFramesPerEmit = 60;
            explosionParticleGroup.initialize(10);
            explosionParticleGroup.billboardState = true;
            explosionParticleGroup.startScale.set(10.0f, 10.0f);
            explosionParticleGroup.finalScale.set(10.0f, 10.0f);
            explosionParticleGroup.emitVec.set(0.0f, 1.0f, 0.0f);
            explosionParticleGroup.emitLocation.set(0.0f, 0.0f, 0.0f);
            explosionParticleGroup.applySpeedScaleToVelocity = false;
            explosionParticleGroup.gravity = false;
            explosionParticleGroup.applyVelocity = false;
            explosionParticleGroup.attract = false;
            explosionParticleGroup.attractLocation.set(0.0f, 10.0f, 0.0f);
            explosionParticleGroup.groundCenter = false;
            explosionParticleGroup.getAppearance().hasAlpha = true;
            explosionParticleGroup.getShape().setName("ParticleExplosion");
            explosionParticleGroup.start();
            explosionParticleGroup.stopEmitter();
            // Render... not a sub entity to avoid serializing it
            CosmicDebug.debugRoot.addChild(explosionParticleGroup.getShapeNode());
        }
        return explosionParticleGroup;
    }
}
