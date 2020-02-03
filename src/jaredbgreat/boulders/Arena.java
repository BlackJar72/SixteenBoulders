package jaredbgreat.boulders;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import jaredbgreat.boulders.util.MaterialMaker;

/**
 * A representation of the physical field the game takes place in, 
 * including the ground and walls / fences at the edge, both geomatries 
 * and physics logic relevant to its effect on game play.  Mostly like 
 * if will not include internal obstacle but may have them as children 
 * in an member data structure.
 * 
 * @author Jared Blackburn
 */
public class Arena {
    private final TextureKey fenceTexKey = new TextureKey("Textures/fence.png");
    private final TextureKey grassTexKey = new TextureKey("Textures/grass.png");
    private final Vector3f gravity = new Vector3f(0f, -9.8f, 0f);
    private final Node penNode;
    private RigidBodyControl penphy;
    private final Geometry ground, nwall, swall, ewall, wwall;
    private RigidBodyControl gphy, nphy, sphy, ephy, wphy;
    
    
    public Arena(AssetManager assetman, Node rootNode) {
        penNode = new Node("penNode");
        Vector3f loc = new Vector3f(0f, -0.5f, 0f);
        Box b = new Box(33f, 0.5f, 33f);
        b.scaleTextureCoordinates(new Vector2f(8, 8));
        //TangentBinormalGenerator.generate(b);
        Material mat = MaterialMaker.makeTiledtMaterial(assetman, grassTexKey); 
        
        ground = new Geometry("field", b);
        ground.setLocalTranslation(loc);
        ground.setMaterial(mat);
        ground.setName("field");
        penNode.attachChild(ground);
        
        b = new Box(32.5f, 1.999f, 0.5f);
        //TangentBinormalGenerator.generate(b);
        b.scaleTextureCoordinates(new Vector2f(8, 1));
        
        mat = MaterialMaker.makeTiledtMaterial(assetman, fenceTexKey);
        
        loc = new Vector3f(0f, 2f, 32f);
        swall = new Geometry("wall", b);
        swall.setLocalTranslation(loc);
        swall.setMaterial(mat);
        swall.setName("wall");
        penNode.attachChild(swall);
        
        loc = new Vector3f(0f, 2f, -32f);
        nwall = new Geometry("wall", b);
        nwall.setLocalTranslation(loc);
        nwall.setMaterial(mat);
        nwall.setName("wall");
        penNode.attachChild(nwall);
        
        b = new Box(0.5f, 2f, 32.5f);
        b.scaleTextureCoordinates(new Vector2f(8, 1));
        //TangentBinormalGenerator.generate(b);
        loc = new Vector3f(32f, 2f, 0f);
        ewall = new Geometry("wall", b);
        ewall.setLocalTranslation(loc);
        ewall.setMaterial(mat);
        ewall.setName("wall");
        penNode.attachChild(ewall);
        
        loc = new Vector3f(-32f, 2f, 0f);
        wwall = new Geometry("wall", b);
        wwall.setLocalTranslation(loc);
        wwall.setMaterial(mat);
        wwall.setName("wall");
        penNode.attachChild(wwall);
        
        rootNode.attachChild(penNode);
    }
    
    
    public Node getNode() {
        return penNode;
    }
    
    
    public Node initPhysics(BulletAppState physics) {
        
        gphy = new RigidBodyControl(0f);
        nphy = new RigidBodyControl(0f);
        sphy = new RigidBodyControl(0f);
        ephy = new RigidBodyControl(0f);
        wphy = new RigidBodyControl(0f);
        
        ground.addControl(gphy);
        nwall.addControl(nphy);
        swall.addControl(sphy);
        ewall.addControl(ephy);
        wwall.addControl(wphy);
        
        physics.getPhysicsSpace().add(gphy);
        physics.getPhysicsSpace().add(nphy);
        physics.getPhysicsSpace().add(sphy);
        physics.getPhysicsSpace().add(ephy);
        physics.getPhysicsSpace().add(wphy);
        
        physics.getPhysicsSpace().setGravity(gravity);
        
        return getNode();
    }
    
    
}
