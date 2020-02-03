package jaredbgreat.boulders.entity;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import jaredbgreat.boulders.states.AppStatePlayGame;
import jaredbgreat.boulders.entity.controls.PlayerControl;
import jaredbgreat.boulders.util.MaterialMaker;

/**
 *
 * @author Jared Blackburn
 */
public class Player extends AbstractEntity {
    PlayerControl player;
    BetterCharacterControl control;
    Geometry visual;
    private static int score;
    boolean alive;
    
    public Player(AppStatePlayGame playgame, Node parent, BulletAppState physics) {     
        spatial = new Node();
        spatial.setLocalTranslation(0f, 0f, -2f);
        visual = makeGeom(playgame.getApplication().getAssetManager(), new Vector3f().set(0, -0.5f, 0f));
        control = new BetterCharacterControl(1.25f, 4.0f, 150f);
        spatial.addControl(control);
        physics.getPhysicsSpace().add(control);
        control.setJumpForce(new Vector3f(0f, 1250f, 0f));
        control.setGravity(new Vector3f(0f, -9.8f, 0f));  
        player = new PlayerControl(playgame, control); 
        spatial.addControl(player);
        ((SimpleApplication)playgame.getApplication()).getRootNode().attachChild(spatial);
        alive = true;
        score = 0;
    }
    

    public static int getScore() {
        return score;
    }
    

    public static void addScore(int points) {
        score += points;
    }
    

    public static void resetScore() {
        score = 0;
    }
    
    
    public void addSpatial(Spatial child) {
        ((Node)spatial).attachChild(child);
    }
    
    
    public PlayerControl getControl() {
        return player;
    }
    
    
    private Geometry makeGeom(AssetManager assetman, Vector3f loc ) {
        // FIXME/TODO: Replace the stand-in sphere with real models.
        loc.addLocal(0f, 1f, 0f);
        Mesh box = new Box(Vector3f.ZERO, 0.5f, 0.5f, 0.5f);
        Geometry geom = new Geometry("player", box);
        Material mat = MaterialMaker.makeBasicMaterial(assetman, ColorRGBA.Blue);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }
    
    
    public void setFirstPerson() {
        Node node = (Node)spatial;
        if(node.hasChild(visual)) {
            node.detachChild(visual);
        }
        player.setFirstPerson();
    }
    
    
    public void setThirdPerson() {        
        ((Node)spatial).attachChild(visual);
        player.setThirdPerson();
    }
    
    
    public void die() {
        //spatial.removeControl(control);
        //spatial.removeControl(player);
        Node node = (Node)spatial;
        if(node.hasChild(visual)) {
            node.detachChild(visual);
        }
        alive = false;
    }
    
    
    public boolean isAlive() {
        return alive;
    }
    
    
    
}
