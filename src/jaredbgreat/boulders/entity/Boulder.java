package jaredbgreat.boulders.entity;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioKey;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import jaredbgreat.boulders.states.AppStatePlayGame;
import jaredbgreat.boulders.entity.controls.BoulderControl;
import jaredbgreat.boulders.util.MaterialMaker;

/**
 *
 * @author jared
 */
public class Boulder extends AbstractEntity {
    private static final TextureKey TEXTURE = new TextureKey("Textures/marble.png");
    private static final AudioKey STONY_CLICK = new AudioKey("Sounds/StonyClick.wav");
    private static final AudioKey WOODY_THUD  = new AudioKey("Sounds/ThudDrumWoody.wav");
    private static final AudioKey GROUND_THUD = new AudioKey("Sounds/ThudDrumLow.wav");
    AudioNode stonyClick;
    AudioNode thudClick;
    AudioNode woodClick;
    private static Material mat;
    private static int num = 0;
    BoulderControl control;
    RigidBodyControl phyControl;
    
    
    public Boulder(AppStatePlayGame playgame, AssetManager assetman, Vector3f loc, 
                Node parent, BulletAppState physics) {
        spatial = makeSpatial(assetman, loc);
        phyControl = new RigidBodyControl(50f);
        spatial.addControl(phyControl);
        control = new BoulderControl(playgame, phyControl, this);
        spatial.addControl(control);
        physics.getPhysicsSpace().add(phyControl);
        parent.attachChild(spatial);
        makeSounds(assetman);
        num++;
    }
    
    
    private Spatial makeSpatial(AssetManager assetman, Vector3f loc) {
        // FIXME/TODO: Replace the stand-in sphere with real models.
        if(mat == null) {
            mat = MaterialMaker.makeTexturedtMaterial(assetman, TEXTURE);
        }
        loc.addLocal(0f, 0.5f, 0f);
        Mesh sphere = new Sphere(16, 16, 0.5f);
        Geometry geom = new Geometry("boulder", sphere);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }
    
    
    public void die() {
        Node parentNode = spatial.getParent();
        spatial.removeControl(control);
        spatial.removeControl(phyControl);
        parentNode.detachChild(spatial);
        Player.addScore(1);
        control = null;
        phyControl = null;
        spatial = null;
        num--;
    }
    
    
    public static int getNum() {
        return num;
    }
    
    
    private void makeSounds(AssetManager assetman) {
        stonyClick = makeSound(assetman, "Sounds/StonyClick.wav");
        thudClick  = makeSound(assetman, "Sounds/ThudDrumLow.wav");
        woodClick  = makeSound(assetman, "Sounds/ThudDrumWoody.wav");
        control.setSounds(stonyClick, woodClick, thudClick);
    }
    
    
    private AudioNode makeSound(AssetManager assetman, String location) {
        AudioNode out = new AudioNode(assetman, location, DataType.Buffer);
        out.setPositional(true);
        out.setLooping(false);
        out.setVolume(5);
        spatial.getParent().attachChild(out);
        return out;
    }
    
}
